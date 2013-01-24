package net.madz.db.core.meta.immutable.impl.enums;


public enum IndexDbMetaDataEnum {
    TABLE_CAT,
    TABLE_SCHEM,
    TABLE_NAME,
    
    /**boolean => Can index values be non-unique. false when TYPE is tableIndexStatistic*/
    NON_UNIQUE ,
    
    /** String => index catalog (may be null); null when TYPE is tableIndexStatistic */
    INDEX_QUALIFIER ,
    
    /** String => index name; null when TYPE is tableIndexStatistic */
    INDEX_NAME,
    
    /** short => index type: */
    TYPE ,
    
    /** short => column sequence number within index; zero when TYPE is tableIndexStatistic */
    ORDINAL_POSITION, 
    
    /**String => column name; null when TYPE is tableIndexStatistic */
    COLUMN_NAME,
    
    /**String => column sort sequence, "A" => ascending, "D" => descending, may be null if sort sequence is not supported; null when TYPE is tableIndexStatistic */
    ASC_OR_DESC ,
    
    /** int => When TYPE is tableIndexStatistic, then this is the number of rows in the table; otherwise, it is the number of unique values in the index. */
    CARDINALITY ,
    
    /**int => When TYPE is tableIndexStatisic then this is the number of pages used for the table, otherwise it is the number of pages used for the current index. */
    PAGES,
    
    /** String => Filter condition, if any. (may be null) */
    FILTER_CONDITION   
  }
