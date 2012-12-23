package net.madz.crmp.etl.engine;

public interface ISyncEngine {

    public static enum SyncStateEnum {
        LocalImportStarted,
        LocalImporting,
        LocalImportEnded,
        RemoteSyncStarted,
        RemoteSyncing,
        RemoteSyncEnded
    }

    void doSync(String plantId) throws SyncException;
}
