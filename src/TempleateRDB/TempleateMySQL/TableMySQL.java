package TempleateRDB.TempleateMySQL;

import GeneradorSQL.GeneradorMySQL;
import TempleateRDB.GenericTempleate.Table;
import java.util.ArrayList;

/**
 *
 * @author wilmer-PC
 */
public class TableMySQL extends Table{

    public TableMySQL(String name) {
        super.name = name;
        super.attributes = new ArrayList<>();
        super.foreingKeys = new ArrayList<>();
        super.uniqueKeys = new ArrayList<>();
        super.primaryKey = null;
    }
    
    public String toSql(GeneradorMySQL gen){
        String z = "Create table " + name + "(" + System.lineSeparator();
        for (int i = 0; i < attributes.size(); i++) {
            z += "\t";
            z += ((AttributeMySQL) attributes.get(i)).toSql(gen);
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
