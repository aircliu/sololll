package fablix.dataimport.xml.handlers;

import fablix.dataimport.xml.models.Award;
import fablix.dataimport.xml.models.Awards;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AwardsHandler extends ExtendedDefaultHandler {
    private Awards awards = new Awards();
    private Award tempAward;

    public Awards getAwards() {
        return awards;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("award")) {
            tempAward = new Award();
        } else {
            handler.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("award")) {
            if (tempAward == null) {
//                System.out.println("tempAward is null");
            } else {
                awards.awards.add(tempAward);
            }
        } else if (qName.equalsIgnoreCase("awardtype")) {
            if (tempAward == null) {
//                System.out.println("tempAward is null");
            } else {
//                if (tempAward.awardtype != null) {
//                    System.out.println("Duplicated element: awardtype, overriding value: '" + tempAward.awardtype + "'");
//                }
                tempAward.awardtype = ((PrimitiveHandler) handler).getString();
            }
        } else if (qName.equalsIgnoreCase("awardattr")) {
            if (tempAward == null) {
//                System.out.println("tempAward is null");
            } else {
//                if (tempAward.awardattr != null) {
//                    System.out.println("Duplicated element: awardattr, overriding value: '" + tempAward.awardattr + "'");
//                }
                tempAward.awardattr = ((PrimitiveHandler) handler).getString();
            }
        } else {
            handler.endElement(uri, localName, qName);
        }
    }

    @Override
    public void clear() {
        awards = new Awards();
    }
}
