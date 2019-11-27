package sql;

public class NotFoundException extends Exception{
    NotFoundException(String type, String name) {
        super("The " + type + " named " + name + " is not found.");
    }
}
