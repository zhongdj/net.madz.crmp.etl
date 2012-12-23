package net.madz.crmp.etl.engine.impl;

import net.madz.crmp.etl.engine.SyncContext;


public class Notifier {

    public void notifyStarted() {
        // TODO Auto-generated method stub
        
    }

    public long notifyImportStarted(SyncContext context, String plantId) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void notifyImportEnded(SyncContext context, String plantId, long start) {
        // TODO Auto-generated method stub
        
    }

    public void notifyImportFailed(SyncContext context, long start, RuntimeException ex) {
        // TODO Auto-generated method stub
        
    }

    public long notifySyncStarted(SyncContext context, String plantId) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void notifySyncEnded(SyncContext context, String plantId, long start) {
        // TODO Auto-generated method stub
        
    }

    public void notifySyncFailed(SyncContext context, long start, RuntimeException ex) {
        // TODO Auto-generated method stub
        
    }

}
