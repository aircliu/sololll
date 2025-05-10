package fablix.dataimport.xml.handlers;

import fablix.dataimport.xml.models.Dirfilms;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public class CastHandler extends ExtendedDefaultHandler {
    private List<Dirfilms> dirfilms = new ArrayList<>();
    private Dirfilms dirfilm = null;
    private final FilmcHandler filmcHandler = new FilmcHandler();

    public List<Dirfilms> getDirfilms() {
        return dirfilms;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("dirfilms")) {
            dirfilm = new Dirfilms();
        } else if (qName.equalsIgnoreCase("filmc")) {
            handlerStack.push(handler);
            handler = filmcHandler;
            handler.clear();
        } else {
            handler.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("dirfilms")) {
            dirfilms.add(dirfilm);
        } else if (qName.equalsIgnoreCase("dirid")) {
//            if (dirfilm.dirid != null) {
//                System.out.println("Duplicated element: dirid, overriding value: '" + dirfilm.dirid + "'");
//            }
            dirfilm.dirid = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("is")) {
//            if (dirfilm.is != null) {
//                System.out.println("Duplicated element: is, overriding value: '" + dirfilm.is + "'");
//            }
            dirfilm.is = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("castnote")) {
//            if (dirfilm.castnote != null) {
//                System.out.println("Duplicated element: castnote, overriding value: '" + dirfilm.castnote + "'");
//            }
            dirfilm.castnote = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("filmc")) {
            dirfilm.filmcs.add(((FilmcHandler) handler).getFilmc());
            handler = handlerStack.pop();
        } else {
            handler.endElement(uri, localName, qName);
        }
    }
}