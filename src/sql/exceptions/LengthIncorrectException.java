package sql.exceptions;

public class LengthIncorrectException extends Exception {

    public LengthIncorrectException(String type, int expected, int found) {
        super("Your length of " + type + " is incorrect.\n"
            + "Expected " + expected + ", but found " + found + " .");
    }
}
