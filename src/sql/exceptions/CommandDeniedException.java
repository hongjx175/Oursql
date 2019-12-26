package sql.exceptions;

public class CommandDeniedException extends Exception {

    public CommandDeniedException() {
        super("Your command is denied. No permission.");
    }
}
