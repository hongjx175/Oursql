package sql.exceptions;

public class UnknownSequenceException extends Exception {

    public UnknownSequenceException() {
        super("Unknown sequence.");
    }
}
