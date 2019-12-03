package sql.exceptions;

public class CannotDeleteException extends Exception {

    public CannotDeleteException(String type, String reason) {
        super(type + " cannot be deleted, " + reason);
    }
}
