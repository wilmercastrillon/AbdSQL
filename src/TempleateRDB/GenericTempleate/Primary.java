package TempleateRDB.GenericTempleate;

/**
 * @author wilmer-PC
 * 
 * Templeate for Relational DataBase
 */
public class Primary {
    public String name, table, columns;

//    public Primary(String name, String table, String columns) {
//        this.name = name;
//        this.table = table;
//        this.columns = columns;
//    }
    
    @Override
    public String toString(){
        return "ALTER TABLE " + table + " ADD CONSTRAINT " + name + " PRIMARY KEY(" +
            columns + ");";
    }
}
