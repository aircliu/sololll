package fablix.dataimport.xml.handlers;

import fablix.dataimport.xml.models.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

public class FilmcHandler extends ExtendedDefaultHandler {
    private Filmc filmc;
    private Movie movie;
    public Filmc getFilmc() {
        return filmc;
    }

    @Override
    public void clear() {
        filmc = new Filmc();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("m")) {
            movie = new Movie();
        } else if (qName.equalsIgnoreCase("rolespecs")) {
          movie.roleSpecs = new RoleSpecs();
        } else if (qName.equalsIgnoreCase("xref")) {
            movie.xref = new Xref();
        } else if (qName.equalsIgnoreCase("awards")) {
            movie.awards = new ArrayList<>();
        } else if (qName.equalsIgnoreCase("life")) {
            movie.life = new Life();
        } else {
            handler.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("awards")) {
            // skip
        } else if (qName.equalsIgnoreCase("life")) {
            // skip
        } else if (qName.equalsIgnoreCase("m")) {
            if (isValidMovie(movie)) {
                filmc.movies.add(movie);
            }
        } else if (qName.equalsIgnoreCase("f")) {
            if (movie.movieId != null) {
                System.out.println("Duplicated element: f, overriding value: '" + movie.movieId + "'");
            }
            movie.movieId = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("t")) {
            if (movie.title != null) {
                System.out.println("Duplicated element: t, overriding value: '" + movie.title + "'");
            }
            movie.title = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("a")) {
            if (movie.actor != null) {
                System.out.println("Duplicated element: a, overriding value: '" + movie.actor + "'");
            }
            movie.actor = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("p")) {
//            if (movie.roleType != null) {
//                System.out.println("Duplicated element: roleType, overriding value: '" + movie.roleType + "'");
//            }
            movie.roleType = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("r")) {
//            if (movie.roleDesc != null) {
//                System.out.println("Duplicated element: roleDesc, overriding value: '" + movie.roleDesc + "'");
//            }
            movie.roleDesc = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("rname")) {
//            if (movie.roleName != null) {
//                System.out.println("Duplicated element: roleName, overriding value: '" + movie.roleName + "'");
//            }
            movie.roleName = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("notes")) {
//            if (movie.notes != null) {
//                System.out.println("Duplicated element: notes, overriding value: '" + movie.notes + "'");
//            }
            movie.notes = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("fact")) {
            movie.notes = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("error")) {
//            if (movie.error != null) {
//                System.out.println("Duplicated element: error, overriding value: '" + movie.error + "'");
//            }
            movie.error = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("award")) {
            if (movie.awards == null) {
//                System.out.println("movie.awards is null");
            } else {
                MovieAward award = new MovieAward();
                award.award = ((PrimitiveHandler) handler).getString();
                movie.awards.add(award);
            }
        } else if (qName.equalsIgnoreCase("kref")) {
//            if (movie.xref.kref != null) {
//                System.out.println("Duplicated element: kref, overriding value: '" + movie.xref.kref + "'");
//            }
            movie.xref.kref = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("refname")) {
//            if (movie.xref.refname != null) {
//                System.out.println("Duplicated element: refname, overriding value: '" + movie.xref.refname + "'");
//            }
            movie.xref.refname = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("rbase")) {
//            if (movie.roleSpecs.rbase != null) {
//                System.out.println("Duplicated element: rbase, overriding value: '" + movie.roleSpecs.rbase + "'");
//            }
            movie.roleSpecs.rbase = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("rnumber")) {
//            if (movie.rnumber != null) {
//                System.out.println("Duplicated element: rnumber, overriding value: '" + movie.rnumber + "'");
//            }
            try {
                movie.rnumber = ((PrimitiveHandler) handler).getInt();
            } catch (NumberFormatException e) {
//                System.out.println("Invalid number format for rnumber: " + ((PrimitiveHandler) handler).getString());
            }
        } else if (qName.equalsIgnoreCase("episode")) {
//            if (movie.episode != null) {
//                System.out.println("Duplicated element: episode, overriding value: '" + movie.episode + "'");
//            }
            movie.episode = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("sings")) {
//            if (movie.sings != null) {
//                System.out.println("Duplicated element: sings, overriding value: '" + movie.sings + "'");
//            }
            movie.sings = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("n")) {
//            if (movie.n != null) {
//                System.out.println("Duplicated element: n, overriding value: '" + movie.n + "'");
//            }
            movie.n = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("debut")) {
//            if (movie.life.debut != null) {
//                System.out.println("Duplicated element: debut, overriding value: '" + movie.life.debut + "'");
//            }
            movie.life.debut = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("lifenote")) {
//            if (movie.life.lifenote != null) {
//                System.out.println("Duplicated element: lifenote, overriding value: '" + movie.life.lifenote + "'");
//            }
            movie.life.lifenote = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("fno")) {
//            if (movie.life.fno != null) {
//                System.out.println("Duplicated element: fno, overriding value: '" + movie.life.fno + "'");
//            }
            movie.life.fno = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("last")) {
//            if (movie.life.last != null) {
//                System.out.println("Duplicated element: last, overriding value: '" + movie.life.last + "'");
//            }
            movie.life.last = ((PrimitiveHandler) handler).getString();
        }
        else {
            handler.endElement(uri, localName, qName);
        }
    }

    private boolean isValidMovie(Movie movie) {
        if (movie.movieId == null) {
            System.out.println("f is null on movie: " + movie);
        }
        if (movie.title == null) {
            System.out.println("t is null on movie: " + movie);
        }
        if (movie.actor == null) {
            System.out.println("a is null on movie: " + movie);
        }
        return true;
    }
}
