package TempleateRDB.TempleatePostgreSQL;

import GeneradorSQL.GeneradorPostgreSQL;
import TempleateRDB.GenericTempleate.Primary;

/**
 *
 * @author wilmer-PC
 */
public class PrimaryPostgreSQL extends Primary{
    
    public PrimaryPostgreSQL(String table, String columns) {
        super.table = table;
        super.columns = columns;
        super.name = null;
    }
    
    public PrimaryPostgreSQL(String name, String table, String columns) {
        super.name = name;
        super.table = table;
        super.columns = columns;
    }
    
    public String toSql(GeneradorPostgreSQL gen){
        if (super.name == null) {
            return gen.addPrimaryKey(table, columns);
        }
        return gen.addPrimaryKey(table, columns, name);
    }
}
