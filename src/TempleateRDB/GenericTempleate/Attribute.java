package TempleateRDB.GenericTempleate;

/**
 * @author wilmer-PC
 * 
 * Templeate for Relational DataBase
 */
public class Attribute {
    public String name, type, defaultValue, superType;
    public String length;
    public boolean nullable;
    public Primary primaryKey;
    public Unique uniqueKey;
    public Foreign foreignKey;
    
}
