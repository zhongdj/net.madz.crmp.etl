package net.madz.db.core.meta.immutable.impl.enums;


public enum PrimaryKeyDbMetaDataEnum {
    table_cat, 
    table_schem, 
    table_name, 
    column_name,
    /** short => sequence number within primary key( a value of 1 represents the first column of the primary key, a value of 2 would represent the second column within the primary key). */
    KEY_SEQ,
    /** String => primary key name (may be null) */
    PK_NAME 
 }