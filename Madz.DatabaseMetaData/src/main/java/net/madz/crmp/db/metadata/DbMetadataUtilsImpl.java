package net.madz.crmp.db.metadata;

public class DbMetadataUtilsImpl implements DbMetadataUtils {

	@Override
	public boolean isDbExists(String dbName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean compareDbsMetadata(String firstDbName, String secondDBName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String cloneDbMetadata(String sourceDbName, String targetDbName)
			throws DbExistsException {
				return targetDbName;
	}

	@Override
	public boolean dropDb(String dbName) {
		// TODO Auto-generated method stub
		return false;
	}

}
