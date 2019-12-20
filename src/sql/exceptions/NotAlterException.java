package sql.exceptions;

public class NotAlterException extends Exception {

    public NotAlterException() {
        super("No database is selected. Please select one first.");
    }
}
