package net.madz.crmp.etl.engine;

public interface ISyncListener {

    void onTaskStarted();

    void onTaskCompleted();

    void onTaskFailed(SyncException ex);

    void onImportStarted(SyncContext context);

    void onImportBatchStarted(SyncContext context);

    void onImportBatchEnded(SyncContext context);

    void onImportBatchFailed(SyncContext context, SyncException ex);

    void onImportFailed(SyncContext context, SyncException ex);

    void onImportEnded(SyncContext context);

    void onSyncStarted(SyncContext context);

    void onSyncBatchStarted(SyncContext context);

    void onSyncBatchEnded(SyncContext context);

    void onSyncBatchFailed(SyncContext context, SyncException ex);

    void onSyncFailed(SyncContext context, SyncException ex);

    void onSyncEnded(SyncContext context);
}
