package sql;

public class CannotDeleteException extends Exception {
    CannotDeleteException(String type, String reason) {
        super(type + " cannot be deleted, " + reason);
    }
}
