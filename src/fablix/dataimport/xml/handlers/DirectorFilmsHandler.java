package fablix.dataimport.xml.handlers;

import fablix.dataimport.xml.models.DirectorFilms;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public class DirectorFilmsHandler extends ExtendedDefaultHandler {
    private List<DirectorFilms> directorFilms = new ArrayList<>();
    private DirectorFilms df;
    private final DirectorHandler directorHandler = new DirectorHandler();
    private final FilmsHandler filmsHandler = new FilmsHandler();

    public List<DirectorFilms> getDirectorFilms() {
        return directorFilms;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("directorfilms")) {
            df = new DirectorFilms();
        }  else if (qName.equalsIgnoreCase("director")) {
            directorHandler.clear();
            handlerStack.push(handler);
            handler = directorHandler;
        } else if (qName.equalsIgnoreCase("films")) {
            filmsHandler.clear();
            handlerStack.push(handler);
            handler = filmsHandler;
        } else {
            handler.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("directorfilms")) {
            directorFilms.add(df);
        } else if (qName.equalsIgnoreCase("director")) {
            if (df.director != null) {
                System.out.println("Duplicated element: director, overriding value: '" + df.director + "'");
            }
            df.director = ((DirectorHandler) handler).getDirector();
            handler = handlerStack.pop();
            if (df.director.dirname  == null || df.director.dirname.isBlank()) {
                System.out.println("dirname is null or empty for director: " + df.director);
            }
        } else if (qName.equalsIgnoreCase("films")) {
            if (df.films != null) {
                System.out.println("Duplicated element: films, overriding value: '" + df.films + "'");
            }
            df.films = ((FilmsHandler) handler).getFilms();
            handler = handlerStack.pop();
        } else {
            handler.endElement(uri, localName, qName);
        }
    }
}
