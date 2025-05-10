package fablix.dataimport.xml.handlers;

import fablix.dataimport.xml.models.Relationships;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RelationshipsHandler extends ExtendedDefaultHandler {
    private Relationships relationships = new Relationships();

    RelationshipsHandler() {
        handler = new RelshipHandler();
    }

    public Relationships getRelationships() {
        return relationships;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("relship")) {
            handler.clear();
        } else {
            handler.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("relship")) {
            relationships.relationships.add(((RelshipHandler) handler).getRelship());
        } else {
            handler.endElement(uri, localName, qName);
        }
    }

    @Override
    public void clear() {
        relationships = new Relationships();
    }
}
