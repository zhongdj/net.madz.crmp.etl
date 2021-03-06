package net.madz.db.metadata.jdbc.type;


public enum JdbcTableType {
    table, 
    view, 
    system_table, 
    global_temporary, 
    local_temporary, 
    alias, 
    synonym;
    
    private final String jdbcValue;
    
    private JdbcTableType() {
       this.jdbcValue= this.name().replaceAll("_"," ").toUpperCase();
    }
    
    public String getJdbcValue() {
       return this.jdbcValue;
    }
    
    public final static JdbcTableType getTableType( String jdbcValue ) {
       if ( null == jdbcValue ) {
          return null;
       }
       return valueOf( jdbcValue.replaceAll(" ","_").toLowerCase() );
    }

 }
