package net.madz.db.core.meta.immutable.impl;

import java.util.Collection;

import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.type.IndexType;
import net.madz.db.core.meta.immutable.type.KeyType;
import net.madz.db.core.meta.immutable.type.SortDirection;

public class IndexMetaDataImpl
<
SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>,
TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>,
CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>,
FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>,
IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>
> 
implements
		IndexMetaData<SMD, TMD, CMD, FMD, IMD>{

	@Override
	public String getIndexName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUnique() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public KeyType getKeyType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexType getIndexType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortDirection getSortDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getCardinality() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getPageCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsColumn(CMD column) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<net.madz.db.core.meta.immutable.IndexMetaData.Entry<IMD, CMD>> getEntrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TMD getTable() {
		// TODO Auto-generated method stub
		return null;
	}

}
