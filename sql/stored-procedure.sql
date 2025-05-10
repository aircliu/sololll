USE moviedb;

DELIMITER $$

CREATE PROCEDURE add_movie(
    IN movie_title VARCHAR(100),
    IN movie_year INT,
    IN movie_director VARCHAR(100),
    IN star_name VARCHAR(100),
    IN genre_name VARCHAR(100)
)
BEGIN
    DECLARE movie_id VARCHAR(10);
    DECLARE star_id VARCHAR(10);
    DECLARE genre_id INT;
    DECLARE movie_exists INT;
    
    -- Check if movie already exists
    SELECT COUNT(*) INTO movie_exists 
    FROM movies 
    WHERE title = movie_title AND year = movie_year AND director = movie_director;
    
    IF movie_exists > 0 THEN
        SELECT 'Movie already exists with the same title, year, and director.' AS message;
    ELSE
        -- Start transaction
        START TRANSACTION;
        
        -- Generate new movie ID
        SELECT MAX(id) INTO movie_id FROM movies;
        SET movie_id = CONCAT('tt', LPAD(SUBSTRING(movie_id, 3) + 1, 7, '0'));
        
        -- Insert new movie
        INSERT INTO movies (id, title, year, director)
        VALUES (movie_id, movie_title, movie_year, movie_director);
        
        -- Check if genre exists, otherwise create it
        SELECT id INTO genre_id FROM genres WHERE name = genre_name LIMIT 1;
        IF genre_id IS NULL THEN
            INSERT INTO genres (name) VALUES (genre_name);
            SET genre_id = LAST_INSERT_ID();
            SELECT CONCAT('Created new genre: ', genre_name, ' with ID: ', genre_id) AS message;
        ELSE
            SELECT CONCAT('Using existing genre: ', genre_name, ' with ID: ', genre_id) AS message;
        END IF;
        
        -- Link movie and genre
        INSERT INTO genres_in_movies (genreId, movieId)
        VALUES (genre_id, movie_id);
        
        -- Check if star exists, otherwise create it
        SELECT id INTO star_id FROM stars WHERE name = star_name LIMIT 1;
        IF star_id IS NULL THEN
            -- Generate new star ID
            SELECT MAX(id) INTO star_id FROM stars;
            SET star_id = CONCAT('nm', LPAD(SUBSTRING(star_id, 3) + 1, 7, '0'));
            
            -- Insert new star
            INSERT INTO stars (id, name, birthYear)
            VALUES (star_id, star_name, NULL);
            
            SELECT CONCAT('Created new star: ', star_name, ' with ID: ', star_id) AS message;
        ELSE
            SELECT CONCAT('Using existing star: ', star_name, ' with ID: ', star_id) AS message;
        END IF;
        
        -- Link movie and star
        INSERT INTO stars_in_movies (starId, movieId)
        VALUES (star_id, movie_id);
        
        -- Commit transaction
        COMMIT;
        
        SELECT CONCAT('Movie added successfully with ID: ', movie_id) AS message;
    END IF;
END$$

DELIMITER ;