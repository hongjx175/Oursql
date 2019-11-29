package sql.exceptions;

public class IsExistedException extends Exception {
    public IsExistedException(String type, String name) {
        super("The " + type + " named " + name+ " is existed.");
    }
}
