package sql.exceptions;

public class DataInvalidException extends Exception {

    public DataInvalidException(String type, String get) {
        super("Data is invalid, need " + type + " found " + get);
    }
}
