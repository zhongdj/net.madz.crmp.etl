package net.madz.crmp.etl.engine;

public final class SyncContext {

    public String plantId;
    public String rawTableName;
    public String indicatorText;
    public int completedQuantity;
    public int batchNumber;
    public long timeCost;

    public SyncContext clone() {
        final SyncContext copy = new SyncContext();
        copy.plantId = plantId;
        copy.rawTableName = rawTableName;
        copy.indicatorText = indicatorText;
        copy.completedQuantity = completedQuantity;
        copy.batchNumber = batchNumber;
        copy.timeCost = timeCost;
        return copy;
    }
    
}
