package ro.ctrln.exceptions;

public class SqlInjectionException extends  Exception {

    public SqlInjectionException(String message) {
        super(message);
    }
}
