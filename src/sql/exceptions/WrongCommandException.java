package sql.exceptions;

public class WrongCommandException extends Exception {

    public WrongCommandException() {
        super("请输入合法的指令.");
    }
}
