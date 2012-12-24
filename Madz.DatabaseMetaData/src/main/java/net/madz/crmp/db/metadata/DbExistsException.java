package net.madz.crmp.db.metadata;

/**
 * The exception will be thrown when cloning database if the target database
 * exists.
 * 
 * @author tracy
 * 
 */
public class DbExistsException extends Exception {

	private static final long serialVersionUID = 1L;
	private final String message;

	public DbExistsException(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "DbExistsException [message=" + message + "]";
	}

}
