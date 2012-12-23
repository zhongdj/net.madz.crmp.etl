package net.madz.crmp.etl.engine;

public class SyncException extends Exception {

    private static final long serialVersionUID = 5148338803603379736L;

    public SyncException() {
        super();
    }

    public SyncException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public SyncException(String arg0) {
        super(arg0);
    }

    public SyncException(Throwable arg0) {
        super(arg0);
    }
    
    
}
