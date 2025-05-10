package fablix.dataimport.xml.handlers;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.Stack;

public abstract class ExtendedDefaultHandler extends ClearableDefaultHandler {
    protected PrimitiveHandler primitiveHandler = new PrimitiveHandler();
    protected ClearableDefaultHandler handler = primitiveHandler;
    protected Stack<ClearableDefaultHandler> handlerStack = new Stack<>();

    public void clear() {
        handler.clear();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        handler.characters(ch, start, length);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        System.out.println("Skipped entity: " + name);
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        System.out.println("Warning: " + e.getMessage());
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        System.out.println("Error: " + e.getMessage());
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        System.out.println("Fatal Error: " + e.getMessage());
    }
}
