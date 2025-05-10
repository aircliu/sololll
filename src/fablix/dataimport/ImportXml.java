package fablix.dataimport;

import fablix.dataimport.models.Movie;
import fablix.dataimport.models.Star;
import fablix.dataimport.models.StarsInMovies;
import fablix.dataimport.xml.handlers.ActorHandler;
import fablix.dataimport.xml.handlers.CastHandler;
import fablix.dataimport.xml.handlers.DirectorFilmsHandler;
import fablix.dataimport.xml.models.Actor;
import fablix.dataimport.xml.models.DirectorFilms;
import fablix.dataimport.xml.models.Dirfilms;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportXml {
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
    private static final String loginUser= "mytestuser";
    private static final String loginPass= "My6$Password";

    private static List<Movie> transformMovies(List<DirectorFilms> directorFilms) {
        return dedupeMovies(directorFilms.stream().flatMap(df -> df.films.filmList.stream().filter(film -> film.fid != null && !film.fid.isBlank()).map(film -> {
            Movie movie = new Movie();
            movie.id = film.fid.toUpperCase();
            movie.title = film.t;
            movie.year = film.year;
            movie.director = df.director.dirname;
            return movie;
        })).collect(Collectors.toList()));
    }

    private static List<Movie> dedupeMovies(List<Movie> movies) {
        return new ArrayList<>(movies.stream().collect(Collectors.toMap(movie -> movie.id, movie -> movie, (movie1, movie2) -> {
            System.out.println("Duplicated movie: " + movie1.id + ", discarding: " + movie2);
            return movie1;
        })).values());
    }

    private static Map<String, Set<String>> transformMovieGenres(List<DirectorFilms> directorFilms) {
        return directorFilms.stream().flatMap(df -> (df.films.filmList.stream())).filter(df -> {
            if (df.fid == null || df.fid.isBlank()) {
                System.out.println("film fid is null " + df);
                return false;
            }
            return true;
        }).collect(Collectors.groupingBy(
                film -> film.fid.toUpperCase(),
                Collectors.flatMapping(film -> {
                    if (film.cats == null || (film.cats.cat.isEmpty()) && film.cats.cattext == null) {
//                        System.out.println("cats is empty on film: " + film.fid);
                        return Stream.empty();
                    } else if (film.cats.cat.isEmpty()) {
                        return Stream.of(film.cats.cattext.trim().toUpperCase());
                    } else {
                        return film.cats.cat.stream().map(cat -> cat.trim().toUpperCase());
                    }
                }, Collectors.toSet())
        ));
    }

    private static List<Star> transformStars(List<Actor> actors) {
        return actors.stream().filter(actor -> actor.stagename != null && !actor.stagename.isBlank()).map(a -> {
            Star star = new Star();
            star.id = a.stagename.trim().toUpperCase();
            star.name = (a.firstname == null && a.familyname == null) ? a.stagename.trim() : (a.firstname + " " + a.familyname).trim();
            star.birthYear = a.dob;
            return star;
        }).collect(Collectors.toList());
    }

    private static List<Star> transformStarsFromDirfilms(List<Dirfilms> dirfilms) {
        return dirfilms.stream().flatMap(df -> df.filmcs.stream()).flatMap(filmc -> filmc.movies.stream()).filter(movie -> movie.actor != null && !movie.actor.isBlank()).map(movie -> {
            Star star = new Star();
            star.id = movie.actor.trim().toUpperCase();
            star.name = movie.actor;
            return star;
        }).collect(Collectors.toList());
    }

    private static List<StarsInMovies> transformStarsInMovies(List<Dirfilms> dirfilms) {
        return dirfilms.stream().flatMap(df -> df.filmcs.stream()).flatMap(filmc -> filmc.movies.stream()).map(movie -> {
            StarsInMovies sm = new StarsInMovies();
            sm.movieId = movie.movieId.toUpperCase();
            sm.starId = movie.actor.toUpperCase();
            return sm;
        }).distinct().collect(Collectors.toList());
    }

    private static List<Star> dedupeStars(List<Star> stars) {
        return new ArrayList<>(stars.stream().collect(Collectors.toMap(star -> star.id, star -> star, (star1, star2) -> {
            if (star1.name != null && !star1.name.isBlank() && star2.name != null && !star2.name.isBlank()) {
                if (star1.birthYear != null) {
                    return star1;
                } else {
                    return star2;
                }
            } else if (star1.name != null && !star1.name.isBlank()) {
                return star1;
            } else {
                return star2;
            }
        })).values());
    }

    private static List<String> getAllGenres(Map<String, Set<String>> movieGenres) {
        return movieGenres.values().stream()
                .flatMap(Set::stream).filter(element -> element!= null && !element.isBlank()).distinct().collect(Collectors.toList());
    }

    private static Future<?> transformAndImportDirectorFilms(Future<List<DirectorFilms>> directorFilmsFuture) throws InterruptedException, ExecutionException {
        List<DirectorFilms> directorFilms = directorFilmsFuture.get();
        Future<List<Movie>> moviesFuture = executor.submit(() -> transformMovies(directorFilms));
        Future<Map<String, Set<String>>> movieGenresFuture = executor.submit(() -> transformMovieGenres(directorFilms));
        Map<String, Set<String>> movieGenres = movieGenresFuture.get();
        List<String> allGenres = executor.submit(() -> getAllGenres(movieGenres)).get();
        Future<Map<String, Integer>> genreToIdFuture = executor.submit(() -> {
            System.out.println("Inserting to genres with size : " + allGenres.size());
            Map<String, Integer> genreToIdMap = new HashMap<>();
            try (Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPass)) {
                conn.setAutoCommit(false);
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO genres (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                for (String genre : allGenres) {
                    insertStmt.setString(1, genre);
                    insertStmt.addBatch();
                }
                int[] updateCounts = insertStmt.executeBatch();
                System.out.println("Batch execution complete for insert into genres");
                boolean allSuccessful = true;
                for (int count : updateCounts) {
                    if (count == Statement.EXECUTE_FAILED) {
                        allSuccessful = false;
                        break;
                    }
                }

                if (allSuccessful) {
                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    int index = 0;
                    while (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        genreToIdMap.put(allGenres.get(index).trim().toUpperCase(), id);
                        index ++;
                    }

                    conn.commit();
                    System.out.println("Transaction committed successfully.");
                } else {
                    System.err.println("One or more commands in the batch failed. Rolling back transaction.");
                    if (conn != null) {
                        conn.rollback();
                        System.err.println("Transaction rolled back.");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return genreToIdMap;
        });

        List<Movie> movies = moviesFuture.get();
        Future<?> moviesInsertFuture = executor.submit(() -> {
            System.out.println("Inserting to movies with size : " + movies.size());
            try (Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPass)) {
                conn.setAutoCommit(false);
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO movies (id, title, year, director) VALUES (?, ?, ?, ?)");
                for (Movie movie : movies) {
                    insertStmt.setString(1, movie.id);
                    insertStmt.setString(2, movie.title);
                    if (movie.year != null) {
                        insertStmt.setInt(3, movie.year);
                    }
                    insertStmt.setString(4, movie.director == null ? "" : movie.director);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
                System.out.println("Batch execution complete for insert into movies");
                conn.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        moviesInsertFuture.get();
        Map<String, Integer> genreToId = genreToIdFuture.get();
        return executor.submit(() -> {
            System.out.println("Inserting to genres_in_movies");
            try (Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPass)) {
                conn.setAutoCommit(false);
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO genres_in_movies (genreId, movieId) VALUES (?, ?)");
                for (Map.Entry<String, Set<String>> entry : movieGenres.entrySet()) {
                    for (String genre: entry.getValue()) {
                        if (genre != null && !genre.isBlank()) {
                            int genreId = genreToId.get(genre.trim().toUpperCase());
                            String movieId = entry.getKey();
                            insertStmt.setInt(1, genreId);
                            insertStmt.setString(2, movieId);
                            insertStmt.addBatch();
                        }
                    }
                }

                insertStmt.executeBatch();
                System.out.println("Batch execution complete for insert into genres_in_movies");
                conn.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static Future<?> transformAndImportDirfilms(Future<List<Dirfilms>> dirfilmsFuture, Future insertMoviesFuture, Future insertStarsFuture) throws InterruptedException, ExecutionException {
        List<Dirfilms> dirfilms = dirfilmsFuture.get();
        List<StarsInMovies> starsInMovies = executor.submit(() -> transformStarsInMovies(dirfilms)).get();
        insertMoviesFuture.get();
        insertStarsFuture.get();
        return executor.submit(() -> {
            System.out.println("Inserting to stars_in_movies with size : " + starsInMovies.size());
            try (Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPass)) {
                conn.setAutoCommit(false);
                PreparedStatement insertStmt = conn.prepareStatement("INSERT IGNORE INTO stars_in_movies (starId, movieId) VALUES (?, ?)");
                for (StarsInMovies sm : starsInMovies) {
                    insertStmt.setString(1, sm.starId);
                    insertStmt.setString(2, sm.movieId);
                    insertStmt.addBatch();
                }
                int[] updateCounts = insertStmt.executeBatch();
                System.out.println("Batch execution complete for insert into stars_in_movies");
                conn.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static Future<?> transformAndImportActors(Future<List<Actor>> actorsFuture, Future<List<Dirfilms>> dirfilmsFuture) throws InterruptedException, ExecutionException {
        List<Actor> actors = actorsFuture.get();
        Future<List<Star>> starsFromActorsFuture = executor.submit(() -> transformStars(actors));
        List<Dirfilms> dirfilms = dirfilmsFuture.get();
        Future<List<Star>> starsFromDirfilmsFuture = executor.submit(() -> transformStarsFromDirfilms(dirfilms));
        final List<Star> stars = starsFromActorsFuture.get();
        stars.addAll(starsFromDirfilmsFuture.get());
        List<Star> dedupedStars = executor.submit(() -> dedupeStars(stars)).get();

        return executor.submit(() -> {
            System.out.println("Inserting to stars with size : " + stars.size());
            try (Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPass)) {
                conn.setAutoCommit(false);
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?)");
                for (Star star : dedupedStars) {
                    insertStmt.setString(1, star.id);
                    insertStmt.setString(2, star.name);
                    if (star.birthYear != null) {
                        insertStmt.setInt(3, star.birthYear);
                    } else {
                        insertStmt.setNull(3, Types.INTEGER);
                    }
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
                System.out.println("Batch execution complete for insert into stars");
                conn.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Number of cores avialable: " + Runtime.getRuntime().availableProcessors());
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            Future<List<Actor>> actorsFuture = executor.submit(() -> {
                SAXParser saxParser = factory.newSAXParser();
                ActorHandler actorHandler = new ActorHandler();
                saxParser.parse(new File("xml/actors63.xml"), actorHandler);
                return actorHandler.getActors();
            });

            Future<List<Dirfilms>> dirfilmsFuture = executor.submit(() -> {
                SAXParser saxParser = factory.newSAXParser();
                CastHandler castHandler = new CastHandler();
                saxParser.parse(new File("xml/casts124.xml"), castHandler);
                return castHandler.getDirfilms();
            });

            Future<List<DirectorFilms>> directorFilmsFuture = executor.submit(() -> {
                SAXParser saxParser = factory.newSAXParser();
                DirectorFilmsHandler directorFilmsHandler = new DirectorFilmsHandler();
                saxParser.parse(new File("xml/mains243.xml"), directorFilmsHandler);
                return directorFilmsHandler.getDirectorFilms();
            });

            transformAndImportDirfilms(dirfilmsFuture, transformAndImportDirectorFilms(directorFilmsFuture), transformAndImportActors(actorsFuture, dirfilmsFuture)).get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
            while (!executor.isTerminated()) {
            }
            System.out.println("Total time: " + (System.currentTimeMillis() - startTime) + "ms");
        }
    }
}
