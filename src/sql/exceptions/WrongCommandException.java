package sql.exceptions;

public class WrongCommandException extends Exception {

    public WrongCommandException(String cause) {
        super("请输入合法的指令.\n" + cause);
    }

    public WrongCommandException() {
        super("wrong command exception");

    }
}
