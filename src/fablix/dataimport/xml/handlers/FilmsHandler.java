package fablix.dataimport.xml.handlers;

import fablix.dataimport.xml.models.Film;
import fablix.dataimport.xml.models.Films;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class FilmsHandler extends ExtendedDefaultHandler {
    private Films films;
    private Film film;
    private final CatsHandler catsHandler = new CatsHandler();

    public Films getFilms() {
        return films;
    }

    @Override
    public void clear() {
        films = new Films();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("film")) {
            film = new Film();
        } else if (qName.equalsIgnoreCase("cats")) {
            handlerStack.push(handler);
            handler = catsHandler;
            handler.clear();
        } else {
            handler.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("film")) {
            if (isValidFilm(film)) {
                films.filmList.add(film);
            }
        } else if (qName.equalsIgnoreCase("fid")) {
            if (film.fid != null) {
                System.out.println("Duplicated element: fid, overriding value: '" + film.fid + "'");
            }
            film.fid = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("t")) {
            if (film.t != null) {
                System.out.println("Duplicated element: t, overriding value: '" + film.t + "'");
            }
            film.t = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("year")) {
            if (film.year != null) {
                System.out.println("Duplicated element: year, overriding value: '" + film.year + "'");
            }
            try {
                film.year = ((PrimitiveHandler) handler).getInt();
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for year: " + ((PrimitiveHandler) handler).getString());
            }
        } else if (qName.equalsIgnoreCase("cats")) {
            if (film.cats != null) {
                System.out.println("Duplicated element: cats, overriding value: '" + film.cats + "'");
            }
            film.cats = ((CatsHandler) handler).getCats();
            handler = handlerStack.pop();
        } else {
            handler.endElement(uri, localName, qName);
        }
    }

    private boolean isValidFilm(Film film) {
        if (film.fid == null || film.fid.isBlank()) {
            System.out.println("fid is null or empty on film: " + film);
        }
        if (film.t == null || film.t.isBlank()) {
            System.out.println("t is null or empty on film: " + film);
        }
        if (film.year == null) {
            System.out.println("year is null on film: " + film);
        }
//        if (film.cats == null || film.cats.cat.isEmpty()) {
//            System.out.println("cats is null or empty on film: " + film);
//        }
        if (film.cats == null) {
            System.out.println("cats is null on film: " + film);
        }
        return true;
    }
}
