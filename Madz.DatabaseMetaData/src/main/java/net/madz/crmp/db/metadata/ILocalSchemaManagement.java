package net.madz.crmp.db.metadata;
/**
 * The interface provides methods for checking, validating, and coping source
 * database schema by mixing plant id. For now, we support Access, MySQL
 * database.
 * 
 * @author tracy
 * @since 2012-12-22
 */
public interface ILocalSchemaManagement {
	/***
	 * This method checks whether a copy schema of specified mixing plant
	 * exists.
	 * 
	 * @param mixingPlantId
	 * @return
	 */
	boolean checkCopyWhetherExists(String mixingPlantId);

	/***
	 * This method validate whether a copy schema is the same with source schema
	 * of a mixing plant.
	 * 
	 * @param mixingPlantId
	 * @return
	 */
	boolean validateCopy(String mixingPlantId);

	/**
	 * This method creates a copy schema by mixingPlantId. When a copy already
	 * exists, exception will be thrown.
	 * 
	 * @param mixingPlantId
	 * @throws AlreadyExistsException
	 */
	void createCopy(String mixingPlantId) throws AlreadyExistsException;

	/**
	 * This method can drop a copy schema of specified mixing plant. Some jdbc
	 * exception may be thrown. Check when implementation.
	 * 
	 * @param mixingPlantId
	 * @return
	 */
	boolean dropCopy(String mixingPlantId);
}
