package TempleateRDB.TempleateMySQL;

import GeneradorSQL.GeneradorMySQL;
import TempleateRDB.GenericTempleate.Foreign;
import TempleateRDB.GenericTempleate.Generic;
import TempleateRDB.GenericTempleate.Table;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class GenericMySQL extends Generic{
    
    private DataBaseMySQL DB;
    private GeneradorMySQL gen;
    private Statement sta;
    
    public GenericMySQL(String name){
        DB = new DataBaseMySQL(name);
        gen = new GeneradorMySQL();
    }
    
    @Override
    public void loadDatabase(Connection con) throws SQLException{
        sta = con.createStatement();
        sta.executeUpdate(gen.selectDataBase(DB.name));
        
        ResultSet rsTables = sta.executeQuery(gen.getTables(DB.name));
        while (rsTables.next()) {
            String tableName = rsTables.getString("nombre");
            TableMySQL table = new TableMySQL(tableName);
            DB.addTable(table);
        }
        for (Table t: DB.tables) {
            TableMySQL table = (TableMySQL) t;
            ResultSet rsColumns = sta.executeQuery(gen.getColumnsTable(table.name));
            
            while (rsColumns.next()) {
                String columnName = rsColumns.getString("nombre");
                String columntype = rsColumns.getString("tipo");
                String defaultValue = rsColumns.getString("default");
                boolean nullable = rsColumns.getString("nulo").equals("si");
                String length = rsColumns.getString("length");
                int aux = columntype.indexOf("(");
                if (aux == -1) {
                    length = null;
                }else{
                    if (columntype.startsWith("enum")) {
                        length = columntype.substring(aux+1,columntype.length()-1);
                    }
                    columntype = columntype.substring(0, aux);
                }
                
                AttributeMySQL att = new AttributeMySQL(columnName, columntype, defaultValue, length, nullable);
                table.addAtribute(att);
            }
        }
        System.out.println("terminadas las tablas");
        
        ResultSet rsPrimarias = sta.executeQuery(gen.getPrimaryKeys(DB.name));//nombre, tabla, columnas
        while (rsPrimarias.next()) {            
            PrimaryMysql key = new PrimaryMysql(rsPrimarias.getString("tabla"), rsPrimarias.getString("columnas"));
            DB.addPrimaryKey(key);
        }
        System.out.println("terminadas las primarias");
        
        //[nombre, tabla, columnas, tabla_referencia, columnas_referencia]
        ResultSet rsForaneas = sta.executeQuery(gen.getForeignKeys(DB.name));
        while (rsForaneas.next()) {            
            Foreign key = new Foreign(rsForaneas.getString("nombre"), rsForaneas.getString("tabla"), 
                    rsForaneas.getString("columnas"),rsForaneas.getString("tabla_referencia"),
                    rsForaneas.getString("columnas_referencia"));
            DB.addForeignKey(key);
        }
        System.out.println("terminadas las foraneas");
    }
    
    public DataBaseMySQL getDatabase(){
        return DB;
    }
    
    @Override
    public String DatabaseToSql(){
        return DB.toSql();
    }
    
    @Override
    public String DatabaseToSqlWithData(Connection con) throws SQLException{
        String sql = DB.toSql();
        sql += System.lineSeparator() + "/*--- Data ---*/";
        sql += System.lineSeparator() + System.lineSeparator();
        sql += "SET FOREIGN_KEY_CHECKS=0;";
        sql += System.lineSeparator() + System.lineSeparator();
        sta = con.createStatement();
        
        for(Table tabla : DB.tables){
            ResultSet rs = sta.executeQuery(gen.selectRowsTable(tabla.name));
            ResultSetMetaData meta = rs.getMetaData();
            
            Vector<String> Columns = new Vector<>();
            Vector<Vector<String>> data = new Vector<>();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                Columns.add(meta.getColumnName(i));
            }
            
            while (rs.next()) {
                Vector<String> aux = new Vector<>();
                for (int i = 1; i <= Columns.size(); i++) {
                    aux.add(rs.getString(i));
                }
                data.add(aux);
            }
            
            if (data.isEmpty()) {
                continue;
            }
            sql += gen.addMultipleRows(tabla.name, Columns, data);
            sql += System.lineSeparator() + System.lineSeparator();
        }
        
        sql += "SET FOREIGN_KEY_CHECKS=1;";
        sql += System.lineSeparator() + System.lineSeparator();
        
        return sql;
    }
    
    private String getSuperType(String tipo){
        tipo = tipo.toLowerCase();
        if (tipo.startsWith("int") || tipo.startsWith("mediumint") || tipo.startsWith("year")) {
            return "int";
        }else if(tipo.startsWith("tinyint") || tipo.startsWith("smallint") || tipo.startsWith("bit")){
            return "short";
        }else if(tipo.startsWith("bigint")){
            return "long";
        }else if(tipo.startsWith("float")){
            return "float";
        }else if(tipo.startsWith("double") || tipo.startsWith("real") || tipo.startsWith("dec")){
            return "double";
        }else if(tipo.startsWith("date") || tipo.startsWith("time") || tipo.startsWith("datetime")){
            return "date";
        }else if(tipo.startsWith("timestamp")){
            return "timestamp";
        }else if(tipo.endsWith("blob")){
            return "blob";
        }else if(tipo.startsWith("bool")){
            return "boolean";
        }
        return "string";
    }
}
