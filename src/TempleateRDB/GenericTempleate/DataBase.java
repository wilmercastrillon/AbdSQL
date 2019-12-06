package TempleateRDB.GenericTempleate;

import java.util.ArrayList;

/**
 * @author wilmer-PC
 * 
 * Templeate for Relational DataBase
 */
public class DataBase {
    public String name;
    public ArrayList<Table> tables;
    public ArrayList<Primary> primarys;
    public ArrayList<Foreign> foreigns;
    public ArrayList<Unique> uniques;
    
}
