package sql;

public class NotLoginException extends Exception {
    NotLoginException(String type) {
        super(type + "cannot run, Please login first!");
    }
}
