package sql.exceptions;

public class NotNullException extends Exception {

    public NotNullException(String columnName) {
        super("Column " + columnName + " can't be null.");
    }
}
