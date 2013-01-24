package net.madz.db.core.meta.immutable.impl.enums;

public enum TableDbMetaDataEnum {
    /** TABLE_CAT String => table catalog (may be null) */
    table_cat,
    /** TABLE_SCHEM String => table schema (may be null) */
    table_schem,
    /** TABLE_NAME String => table name */
    table_name,
    /**
     * TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW",
     * "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM"
     */
    table_type,
    /** REMARKS String => explanatory comment on the table */
    remarks,
    /** TYPE_CAT String => the types catalog (may be null) */
    type_cat,
    /** TYPE_SCHEM String => the types schema (may be null) */
    type_schem,
    /** TYPE_NAME String => type name (may be null) */
    type_name,
    /**
     * SELF_REFERENCING_COL_NAME String => name of the designated "identifier"
     * column of a typed table (may be null)
     */
    self_referencing_col_name,
    /**
     * REF_GENERATION String => specifies how values in
     * SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER",
     * "DERIVED". (may be null)
     */
    ref_generation;
}