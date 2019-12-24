package sql.exceptions;

public class TooLongException extends Exception {

    public TooLongException(String type, int maxLength) {
        super("Your data of type: " + type + " is too long, maxLength is " + maxLength + " .");
    }
}
