package TempleateRDB.TempleateMySQL;

import GeneradorSQL.GeneradorMySQL;
import TempleateRDB.GenericTempleate.DataBase;
import TempleateRDB.GenericTempleate.Foreign;
import TempleateRDB.GenericTempleate.Primary;
import java.util.ArrayList;

/**
 *
 * @author wilmer-PC
 */
public class DataBaseMySQL extends DataBase{
    
    public DataBaseMySQL(String name){
        super.name = name;
        super.tables = new ArrayList<>();
        super.primarys = new ArrayList<>();
        super.foreigns = new ArrayList<>();
        super.uniques = new ArrayList<>();
    }
    
    public void addTable(TableMySQL tm){
        tables.add(tm);
    }
    
    public void addPrimaryKey(Primary key){
        primarys.add(key);
    }
    
    public void addForeignKey(Foreign key){
        foreigns.add(key);
    }
    
    public String toSql(){
        GeneradorMySQL gen = new GeneradorMySQL();
        String sql = gen.createDataBase(name) + System.lineSeparator();
        sql += gen.selectDataBase(name) + System.lineSeparator();
        sql += System.lineSeparator();
        
        sql += "/*--- Tables ---*/" + System.lineSeparator();
        for (int i = 0; i < tables.size(); i++) {
            TableMySQL table = (TableMySQL) tables.get(i);
            sql += table.toSql(gen);
            sql += System.lineSeparator() + System.lineSeparator();
        }
        
        sql += "/*--- Primarys ---*/" + System.lineSeparator();
        sql += System.lineSeparator();
        for (Primary key : primarys) {
            PrimaryMysql pm = (PrimaryMysql) key;
            sql += pm.toSql(gen);
            sql += System.lineSeparator();
        }
        sql += System.lineSeparator();
        
        sql += "/*--- Foreings ---*/" + System.lineSeparator();
        sql += System.lineSeparator();
        for (Foreign key : foreigns) {
            sql += key.toSql();
            sql += System.lineSeparator();
        }
        sql += System.lineSeparator();
        
        return sql;
    }
}
