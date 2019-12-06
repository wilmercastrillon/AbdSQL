package GeneradorSQL;

import java.util.Vector;

/**
 *
 * @author wilmer
 */
public abstract class GeneradorSQL {
    
    public GeneradorSQL(){
    }

    abstract public String getDataBases();

    abstract public String selectDataBase(String db);

    abstract public String createDataBase(String name);
    
    abstract public String dropDataBase(String db);

    abstract public String getTables();//[nombre]
    
    abstract public String getTables(String db);//[nombre]

    abstract public String getColumnsTable(String table);//[nombre, tipo, nulo, default, length,.........]

    abstract public String createTable(String name);

    abstract public String dropTable(String name);

    abstract public String addRow(String table, String data[]);

    abstract public String addRow(String table, Vector<String> columns, Vector<String> data);
    
    abstract public String addMultipleRows(String table, Vector<String> columns, Vector<Vector<String>> data);

    abstract public String selectRowsTable(String table);

    abstract public String deleteRow(String table, Vector<String> columns, Vector<String> data);

    abstract public String updateRow(String table, Vector<String> columns, Vector<String> data,
            String updateColumn, String newValue);

    abstract public String addColumnTable(String table, String type, String name, String length,
            String Default, boolean noNull);

    abstract public String dropColumnTable(String table, String column);

    abstract public String addPrimaryKey(String table, String column);
    
    abstract public String addPrimaryKey(String table, String column, String name);

    abstract public String addForeignKey(String table, String column, String table_ref, String col_ref);
    
    abstract public String getPrimaryKeys(String db);//[nombre, tabla, columnas]
    
    abstract public String getForeignKeys(String db);//[nombre, tabla, columnas, tabla_referencia, columnas_referencia]
    
    abstract public String addForeignKey(String table, String column, String table_ref, String col_ref, String name);

    abstract public String getTriggers();

    abstract public String getTriggerData(String db, String triggerName);

    abstract public String createTrigger(String sql);

    abstract public String dropTrigger(String triggerName);

    abstract public String addUniqueKey(String table, String column);
    
    abstract public String addUniqueKey(String table, String column, String name);

    abstract public String updateColumn(String table, String name, String type, String newName,
            String length, String defaultValue, boolean notNull);

    abstract public String renameTable(String table, String newName);

    abstract public String addAuto_increment(String table, String column, String type);

    abstract public String getProcedures(String db);

    abstract public String getProcedureData(String db, String name);

    abstract public String createProcedure(String sql);

    abstract public String dropProcedure(String name);
    
    abstract public String callProcedure(String procedure, String params);
    
    //[constraint][tabla][columna][tabla_referencia][columna_referencia]
    abstract public String getForeignKeys(String db, String table);
    
    //[constraint][tabla][columna][tabla_referencia][columna_referencia]
    abstract public String getForeignKey(String db, String table, String constraint);
    
    abstract public String dropForeignKey(String table, String constraint);
    
    //[tabla][indice]
    abstract public String getIndexs(String db, String table);
    
    abstract public String dropIndex(String table, String constraint);
    
    abstract public String SingleAddColumnTable(String type, String name, String length, String Default, boolean noNull);
}
