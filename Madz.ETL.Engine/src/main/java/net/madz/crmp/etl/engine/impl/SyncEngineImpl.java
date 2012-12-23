package net.madz.crmp.etl.engine.impl;

import net.madz.crmp.etl.engine.ISyncEngine;
import net.madz.crmp.etl.engine.SyncContext;
import net.madz.crmp.etl.engine.SyncException;
import net.madz.crmp.etl.engine.meta.DatabaseMetaData;

public class SyncEngineImpl implements ISyncEngine {

    private final Notifier notifier = new Notifier();

    @Override
    public void doSync(String plantId) throws SyncException {
        notifier.notifyStarted();
        final SyncContext context = new SyncContext();
        final DatabaseMetaData sourceDatabaseMetaData = loadDatabaseMetaData(plantId);
        final long localCopyStart = notifier.notifyImportStarted(context, plantId);
        try {
            // Start to Import
            doLocalCopy(context, sourceDatabaseMetaData);
            notifier.notifyImportEnded(context, plantId, localCopyStart);
        } catch (RuntimeException ex) {
            notifier.notifyImportFailed(context, localCopyStart, ex);
            throw ex;
        }
        
        final long remoteSyncStart = notifier.notifySyncStarted(context, plantId);
        try { // Start to Sync
            doRemoteSync(context, sourceDatabaseMetaData);
            notifier.notifySyncEnded(context, plantId, remoteSyncStart);
        } catch (RuntimeException ex) {
            notifier.notifySyncFailed(context, remoteSyncStart, ex);
            throw ex;
        }
    }

    protected DatabaseMetaData loadDatabaseMetaData(String plantId) {
        // TODO Auto-generated method stub
        return null;
    }

    protected void doRemoteSync(SyncContext context, DatabaseMetaData sourceDatabaseMetaData) {
        // TODO Auto-generated method stub
    }

    protected void doLocalCopy(SyncContext context, DatabaseMetaData sourceDatabaseMetaData) {
        // TODO Auto-generated method stub
    }
}
