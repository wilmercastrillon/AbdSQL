package TempleateRDB.GenericTempleate;

/**
 * @author wilmer-PC
 * 
 * Templeate for Relational DataBase
 */
public class Foreign {
    public String name, column, columnReference, table, tableReference;

    public Foreign(String name, String table, String column, String tableReference, String columnReference) {
        this.name = name;
        this.table = table;
        this.column = column;
        this.tableReference = tableReference;
        this.columnReference = columnReference;
    }
    
    public String toSql(){
        return this.toString();
    }
    
    @Override
    public String toString(){
        return "ALTER TABLE " + table + " ADD CONSTRAINT " + name + " FOREIGN KEY(" +
            column + ") REFERENCES " + tableReference + "(" + columnReference + "); ";
    }
}
