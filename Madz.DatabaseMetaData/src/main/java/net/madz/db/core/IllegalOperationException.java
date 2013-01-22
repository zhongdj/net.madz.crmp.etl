package net.madz.db.core;

/**
 * The exception will be thrown when cloning database if the target database
 * exists.
 * 
 * @author tracy
 * 
 */
public class IllegalOperationException extends Exception {

    private static final long serialVersionUID = 1L;
    public static final String DB_NAME_ALREAY_EXISTS = "Database name already exists.";

    public IllegalOperationException(String message) {
        super(message);
    }
}
