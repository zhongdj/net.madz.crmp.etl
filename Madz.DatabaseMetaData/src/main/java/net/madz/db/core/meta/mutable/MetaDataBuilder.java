package net.madz.db.core.meta.mutable;

import net.madz.db.core.meta.immutable.MetaData;

public interface MetaDataBuilder<MD extends MetaData> {

    MD getMetaData();
}
