package TempleateRDB.GenericTempleate;

/**
 * @author wilmer-PC
 * 
 * Templeate for Relational DataBase
 */
public class Unique {
    public String name;
    public Attribute column;
    public Table table;
    
    public String toString(){
        return "ALTER TABLE " + table + " ADD CONSTRAINT " + name + " UNIQUE (" + 
            column + ");";
    }
}
