package TempleateRDB.TempleatePostgreSQL;

import GeneradorSQL.GeneradorPostgreSQL;
import TempleateRDB.GenericTempleate.Table;
import java.util.ArrayList;

/**
 *
 * @author wilmer-PC
 */
public class TablePostgreSQL extends Table{
 
    public TablePostgreSQL(String name){
        super.name = name;
        super.attributes = new ArrayList<>();
        super.foreingKeys = new ArrayList<>();
        super.uniqueKeys = new ArrayList<>();
        super.primaryKey = null;
    }
    
    public String toSql(GeneradorPostgreSQL gen){
        String z = "Create table " + name + "(" + System.lineSeparator();
        for (int i = 0; i < attributes.size(); i++) {
            z += "\t";
            z += ((AttributePostgreSQL) attributes.get(i)).toSql(gen);
            if (i != attributes.size()-1) {
                z += ",";
            }
            z += System.lineSeparator();
        }
        z += ");";
        return z;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
