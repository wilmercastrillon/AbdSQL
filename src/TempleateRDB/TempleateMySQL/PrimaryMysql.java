/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TempleateRDB.TempleateMySQL;

import GeneradorSQL.GeneradorMySQL;
import TempleateRDB.GenericTempleate.Primary;

/**
 *
 * @author wilmer-PC
 */
public class PrimaryMysql extends Primary{
    
//    public PrimaryMysql(String name, String table, String columns) {
//        super.table = table;
//        super.columns = columns;
//    }
    
    public PrimaryMysql(String table, String columns) {
        super.table = table;
        super.columns = columns;
    }
    
    public String toSql(GeneradorMySQL gen){
        return gen.addPrimaryKey(table, columns);
    }
}
