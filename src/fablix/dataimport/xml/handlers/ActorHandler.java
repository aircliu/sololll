package fablix.dataimport.xml.handlers;

import fablix.dataimport.xml.models.Actor;
import fablix.dataimport.xml.models.Gender;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public class ActorHandler extends ExtendedDefaultHandler {
    private List<Actor> actors = new ArrayList<>();
    private Actor tempActor;
    private final RelationshipsHandler relationshipsHandler = new RelationshipsHandler();
    private final AwardsHandler awardsHandler = new AwardsHandler();

    public List<Actor> getActors() {
        return actors;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("actor")) {
            tempActor = new Actor();
        } else if (qName.equalsIgnoreCase("relationships")) {
            handlerStack.push(handler);
            handler = relationshipsHandler;
            handler.clear();
        } else if (qName.equalsIgnoreCase("awards")) {
            handlerStack.push(handler);
            handler = awardsHandler;
            handler.clear();
        } else {
            handler.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("actor")) {
            if (isValid(tempActor)) {
                actors.add(tempActor);
            }
        } else if (qName.equalsIgnoreCase("stagename")) {
            if (tempActor.stagename != null) {
                System.out.println("Duplicated element: stagename, overriding value: '" + tempActor.stagename + "'");
            }
            tempActor.stagename = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("alias")) {
//            if (tempActor.alias != null) {
//                System.out.println("Duplicated element: alias, overriding value: '" + tempActor.alias + "'");
//            }
            tempActor.alias = ((PrimitiveHandler) handler).getString();
            ;
        } else if (qName.equalsIgnoreCase("dowstart")) {
//            if (tempActor.dowstart != null) {
//                System.out.println("Duplicated element: dowstart, overriding value: '" + tempActor.dowstart + "'");
//            }
            try {
                tempActor.dowstart = ((PrimitiveHandler) handler).getInt();
            } catch (NumberFormatException ignored) {
            }
        } else if (qName.equalsIgnoreCase("dowend")) {
//            if (tempActor.dowend != null) {
//                System.out.println("Duplicated element: dowend, overriding value: '" + tempActor.dowend + "'");
//            }
            try {
                tempActor.dowend = ((PrimitiveHandler) handler).getInt();
            } catch (NumberFormatException ignored) {

            }
        } else if (qName.equalsIgnoreCase("familyname")) {
            if (tempActor.familyname != null) {
                System.out.println("Duplicated element: familyname, overriding value: '" + tempActor.familyname + "'");
            }
            tempActor.familyname = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("firstname")) {
            if (tempActor.firstname != null) {
                System.out.println("Duplicated element: firstname, overriding value: '" + tempActor.firstname + "'");
            }
            tempActor.firstname = ((PrimitiveHandler) handler).getString();
            ;
        } else if (qName.equalsIgnoreCase("gender")) {
//            if (tempActor.gender != null) {
//                System.out.println("Duplicated element: gender, overriding value: '" + tempActor.gender + "'");
//            }
            String gender = ((PrimitiveHandler) handler).getString();
            if (gender != null && !gender.isBlank()) {
                try {
                    tempActor.gender = Gender.valueOf(gender);
                } catch (IllegalArgumentException e) {
//                    System.out.println("Invalid gender: " + gender);
                }
            }
        } else if (qName.equalsIgnoreCase("dob")) {
            if (tempActor.dob != null) {
                System.out.println("Duplicated element: dob, overriding value: '" + tempActor.dob + "'");
            }
            try {
                tempActor.dob = ((PrimitiveHandler) handler).getInt();
            } catch (NumberFormatException ignored) {
                String dobString = ((PrimitiveHandler) handler).getString();
                if (dobString != null && !dobString.isBlank()) {
                    System.out.println("Invalid number format for dob: " + ((PrimitiveHandler) handler).getString());
                }
            }
        } else if (qName.equalsIgnoreCase("dod")) {
//            if (tempActor.dod != null) {
//                System.out.println("Duplicated element: dod, overriding value: '" + tempActor.dod + "'");
//            }
            tempActor.dod = ((PrimitiveHandler) handler).getString();
        } else if (qName.equalsIgnoreCase("origin")) {
//            if (tempActor.origin == null) {
//                tempActor.origin = ((PrimitiveHandler) handler).getString();;
//            }
        } else if (qName.equalsIgnoreCase("picref")) {
//            if (tempActor.picref != null) {
//                System.out.println("Duplicated element: picref, overriding value: '" + tempActor.picref + "'");
//            }
            tempActor.picref = ((PrimitiveHandler) handler).getString();
            ;
        } else if (qName.equalsIgnoreCase("relationships")) {
//            if (tempActor.relationships != null) {
//                System.out.println("Duplicated element: relationships, overriding value: '" + tempActor.relationships + "'");
//            }
            tempActor.relationships = ((RelationshipsHandler) handler).getRelationships();
            handler = handlerStack.pop();
        } else if (qName.equalsIgnoreCase("awards")) {
//            if (tempActor.awards != null) {
//                System.out.println("Duplicated element: awards, overriding value: '" + tempActor.awards + "'");
//            }
            tempActor.awards = ((AwardsHandler) handler).getAwards();
            handler = handlerStack.pop();
        } else if (qName.equalsIgnoreCase("error")) {
//            if (tempActor.error != null) {
//                System.out.println("Duplicated element: error, overriding value: '" + tempActor.error + "'");
//            }
            tempActor.error = ((PrimitiveHandler) handler).getString();
            ;
        } else if (qName.equalsIgnoreCase("roletype")) {
//            if (tempActor.roletype != null) {
//                System.out.println("Duplicated element: roletype, overriding value: '" + tempActor.roletype + "'");
//            }
            tempActor.roletype = ((PrimitiveHandler) handler).getString();
        } else {
            handler.endElement(uri, localName, qName);
        }
    }

    private boolean isValid(Actor actor) {
        if (actor == null) {
            System.out.println("actor is null");
            return false;
        }
        if (actor.stagename == null) {
            System.out.println("stagename is null on actor: " + actor);
            return false;
        }
        return true;
    }

    @Override
    public void clear() {
        actors = new ArrayList<>();
    }
}
