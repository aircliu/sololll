package fablix.dataimport.xml.handlers;

import fablix.dataimport.xml.models.Towhom;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TowhomHandler extends ExtendedDefaultHandler {
    private Towhom towhom;

    public Towhom getTowhom() {
        return towhom;
    }

    @Override
    public void clear() {
        towhom = new Towhom();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        handler.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("relname")) {
//            if (towhom.relname != null) {
//                System.out.println("Duplicated element: relname, overriding value: '" + towhom.relname + "'");
//            }
            towhom.relname = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("relattr")) {
//            if (towhom.relattr != null) {
//                System.out.println("Duplicated element: relattr, overriding value: '" + towhom.relattr + "'");
//            }
            towhom.relattr = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("relnote")) {
//            if (towhom.relnote != null) {
//                System.out.println("Duplicated element: relnote, overriding value: '" + towhom.relnote + "'");
//            }
            towhom.relnote = ((PrimitiveHandler) handler).getString();
        } else {
            handler.endElement(uri, localName, qName);
        }
    }
}
