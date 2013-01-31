package net.madz.db.core.meta.mutable.impl;

import net.madz.db.core.meta.immutable.MetaData;
import net.madz.db.core.meta.mutable.MetaDataBuilder;

public abstract class BaseMetaDataBuilder<MD extends MetaData> implements MetaDataBuilder<MD> {

    // un-thread-safe
    protected MD constructedMetaData;

    @Override
    public MD getMetaData() {
        if ( null == constructedMetaData ) {
            constructedMetaData = createMetaData();
        }
        return constructedMetaData;
    }

    protected abstract MD createMetaData();
}
