package fablix.dataimport.xml.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class PrimitiveHandler extends ClearableDefaultHandler {
    private final StringBuilder stringBuilder = new StringBuilder();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        clear();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        System.out.println("Unknown element: " + qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        stringBuilder.append(ch, start, length);
    }

    public String getString() {
        return stringBuilder.toString();
    }

    public int getInt() throws NumberFormatException {
        return Integer.parseInt(stringBuilder.toString());
    }

    @Override
    public void clear() {
        stringBuilder.setLength(0);
    }
}
