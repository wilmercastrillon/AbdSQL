package TempleateRDB.GenericTempleate;

import java.util.ArrayList;

/**
 * @author wilmer-PC
 * 
 * Templeate for Relational DataBase
 */
public class Table {
    public String name;
    public ArrayList<Attribute> attributes;
    public Primary primaryKey;
    public ArrayList<Foreign> foreingKeys;
    public ArrayList<Unique> uniqueKeys;
    
    public void addAtribute(Attribute at){
        attributes.add(at);
    }
}
