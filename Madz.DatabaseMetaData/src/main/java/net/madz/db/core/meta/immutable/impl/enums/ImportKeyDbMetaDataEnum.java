package net.madz.db.core.meta.immutable.impl.enums;

public enum ImportKeyDbMetaDataEnum {
   /** String => primary key table catalog being imported (may be null) */
   PKKTABLE_CAT,
   /**String => primary key table schema being imported (may be null) */
   PKTABLE_SCHEM ,
   /** String => primary key table name being imported */
   PKTABLE_NAME ,
   /** String => primary key column name being imported */
   PKCOLUMN_NAME ,
   /** String => foreign key table catalog (may be null) */
   FKTABLE_CAT ,
   /** String => foreign key table schema (may be null)*/
   FKTABLE_SCHEM ,
   
   /**String => foreign key table name */
   FKTABLE_NAME ,
   
   /** String => foreign key column name */
   FKCOLUMN_NAME ,
   
   /** short => sequence number within a foreign key( a value of 1 represents the first column of the foreign key, a value of 2 would represent the second column within the foreign key). */
   KEY_SEQ ,
   
   /** short => What happens to a foreign key when the primary key is updated: */
   UPDATE_RULE ,
   
   /** short => What happens to the foreign key when primary is deleted. */
   DELETE_RULE,
   
   /**  String => foreign key name (may be null) */
   FK_NAME,
   
   /** String => primary key name (may be null) */      
   PK_NAME ,
   
   /** short => can the evaluation of foreign key constraints be deferred until commit
    * This controls whether the constraint can be deferred to the end of the transaction. If SET CONSTRAINTS ALL DEFERRED is used or the constraint is set to INITIALLY DEFERRED, this will cause the foreign key to be checked only at the end of the transaction.
    * importedKeyInitiallyDeferred - see SQL92 for definition
    * importedKeyInitiallyImmediate - see SQL92 for definition
    * importedKeyNotDeferrable - see SQL92 for definition 
    */
   DEFERRABILITY 
}