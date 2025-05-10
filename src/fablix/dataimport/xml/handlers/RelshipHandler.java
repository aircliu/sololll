package fablix.dataimport.xml.handlers;

import fablix.dataimport.xml.models.Relship;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

public class RelshipHandler extends ExtendedDefaultHandler {
    private Relship relship;
    private TowhomHandler towhomHandler = new TowhomHandler();

    public Relship getRelship() {
        return relship;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("towhom")) {
            handlerStack.push(handler);
            handler = towhomHandler;
            handler.clear();
        } else {
            handler.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("reltype")) {
            if (relship.reltype != null) {

            }
            relship.reltype = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("towhom")) {
            if (relship.towhoms != null) {

            }
            if (relship.towhoms == null) {
                relship.towhoms = new ArrayList<>();
            }
            relship.towhoms.add(((TowhomHandler) handler).getTowhom());
            handler = handlerStack.pop();
        } else {
            handler.endElement(uri, localName, qName);
        }
    }

    @Override
    public void clear() {
        relship = new Relship();
    }
}
