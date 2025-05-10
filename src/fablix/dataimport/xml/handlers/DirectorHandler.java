package fablix.dataimport.xml.handlers;

import fablix.dataimport.xml.models.Director;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class DirectorHandler extends ExtendedDefaultHandler {
    private Director director;

    public Director getDirector() {
        return director;
    }

    @Override
    public void clear() {
        director = new Director();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        handler.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("dirid")) {
//            if (director.dirid != null) {
//                System.out.println("Duplicated element: dirid, overriding value: '" + director.dirid + "'");
//            }
            director.dirid = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("dirstart")) {
//            if (director.dirstart != null) {
//                System.out.println("Duplicated element: dirstart, overriding value: '" + director.dirstart + "'");
//            }
            director.dirstart = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("dirname")) {
            if (director.dirname != null) {
                System.out.println("Duplicated element: dirname, overriding value: '" + director.dirname + "'");
            }
            director.dirname = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("coverage")) {
//            if (director.coverage != null) {
//                System.out.println("Duplicated element: coverage, overriding value: '" + director.coverage + "'");
//            }
            director.coverage = ((PrimitiveHandler) handler).getString();
        } else {
            handler.endElement(uri, localName, qName);
        }
    }
}
