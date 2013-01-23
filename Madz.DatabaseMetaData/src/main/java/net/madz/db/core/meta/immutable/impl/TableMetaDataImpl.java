package net.madz.db.core.meta.immutable.impl;

import java.util.Collection;
import java.util.List;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.type.TableType;

public class TableMetaDataImpl
<
SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>,
TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>,
CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>,
FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>,
IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>
> 
implements
		TableMetaData<SMD, TMD, CMD, FMD, IMD>{

	@Override
	public DottedPath getTablePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TableType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemarks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIdCol() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIdGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMD getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CMD getColumn(String columnName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CMD> getColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<FMD> getForeignKeySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IMD> getIndexSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMD getIndex(String indexName) {
		// TODO Auto-generated method stub
		return null;
	}

}
