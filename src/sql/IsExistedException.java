package sql;

public class IsExistedException extends Exception {
    IsExistedException(String type, String name) {
        super("The " + type + " named " + name+ " is existed.");
    }
}
