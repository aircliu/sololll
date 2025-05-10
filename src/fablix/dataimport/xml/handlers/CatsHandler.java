package fablix.dataimport.xml.handlers;

import fablix.dataimport.xml.models.Cats;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CatsHandler extends ExtendedDefaultHandler {
    private Cats cats;

    public Cats getCats() {
        return cats;
    }

    @Override
    public void clear() {
        cats = new Cats();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        handler.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("cat")) {
            cats.cat.add(((PrimitiveHandler) handler).getString());
        } else if (qName.equalsIgnoreCase("cattext")) {
            if (cats.cattext != null) {
                System.out.println("Duplicated element: cattext, overriding value: '" + cats.cattext + "'");
            }
            cats.cattext = ((PrimitiveHandler) handler).getString();
        } else {
            handler.endElement(uri, localName, qName);
        }
    }
}
