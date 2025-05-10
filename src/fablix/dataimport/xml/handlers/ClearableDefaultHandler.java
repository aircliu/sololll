package fablix.dataimport.xml.handlers;

import org.xml.sax.helpers.DefaultHandler;

public abstract class ClearableDefaultHandler extends DefaultHandler {
    public abstract void clear();
}
