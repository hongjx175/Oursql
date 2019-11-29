package sql.exceptions;

public class NotFoundException extends Exception{
    public NotFoundException(String type, String name) {
        super("The " + type + " named " + name + " is not found.");
    }
}
