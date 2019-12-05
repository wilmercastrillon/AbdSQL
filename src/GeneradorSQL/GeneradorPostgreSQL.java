package GeneradorSQL;

import java.util.Vector;

/**
 *
 * @author wilmer
 */
public class GeneradorPostgreSQL extends GeneradorSQL {

    public GeneradorPostgreSQL(){
    }
    
    @Override
    public String getDataBases() {
        return "SELECT datname FROM pg_database;"; //" WHERE datistemplate = false;";
    }

    @Override
    public String selectDataBase(String db) {
        return "\\c " + db + ";";
    }

    @Override
    public String createDataBase(String name) {
        return "CREATE DATABASE " + name + ";";
    }

    @Override
    public String dropDataBase(String db) {
        String z = "DROP DATABASE IF EXISTS " + db + ";";
        return z;
    }

    @Override
    public String getTables() {
        return "select tablename as nombre from pg_catalog.pg_tables where schemaname != "
                + "'information_schema' and schemaname != 'pg_catalog' order by nombre;";
    }

    public String getTables(String db) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getColumnsTable(String table) {
        String sql = " SELECT DISTINCT attname as nombre, format_type(atttypid, atttypmod) ";
        sql += "as tipo, case when not attnotnull then 'si' else 'no' end as nulo, adsrc as default, coalesce(i.indisprimary,false) ";
        sql += "as llave_primaria, a.attnum as numero FROM pg_attribute a JOIN pg_class pgc ON pgc.oid = ";
        sql += "a.attrelid LEFT JOIN pg_index i ON (pgc.oid = i.indrelid AND i.indkey[0] = a.attnum) ";
        sql += "LEFT JOIN pg_description com on (pgc.oid = com.objoid AND a.attnum = com.objsubid) ";
        sql += "LEFT JOIN pg_attrdef def ON (a.attrelid = def.adrelid AND a.attnum = def.adnum) ";
        sql += "WHERE a.attnum > 0 AND pgc.oid = a.attrelid AND pg_table_is_visible(pgc.oid)AND NOT ";
        sql += "a.attisdropped  AND pgc.relname = '" + table + "' ORDER BY a.attnum; ";
        return sql;
    }

    @Override
    public String createTable(String name) {
        String z = "Create Table " + name + " (id" + name.replace(" ", "") + " int NOT NULL);";
        return z;
    }
    
    @Override
    public String dropTable(String name) {
        String z = "DROP TABLE IF EXISTS " + name + ";";
        return z;
    }

    @Override
    public String addRow(String table, String data[]) {
        StringBuilder str = new StringBuilder("'");
        for (int i = 0; i < data.length - 1; i++) {
            data[i] = data[i].trim();
            str.append(data[i]).append("','");
        }
        data[data.length - 1] = data[data.length - 1].trim();
        str.append(data[data.length - 1]).append("'");

        String z = "INSERT INTO " + table + " values(" + str.toString() + ");";
        return z;
    }

    @Override
    public String addRow(String table, Vector<String> columns, Vector<String> data) {
        StringBuilder col = new StringBuilder("");
        StringBuilder dat = new StringBuilder("'");
        String z = "";

        try {
            for (int i = 0; i < columns.size(); i++) {
                if (data.get(i) != null) {
                    col.append(columns.get(i));
                    col.append(", ");
                    dat.append(data.get(i));
                    dat.append("', '");
                }
            }

            z += "INSERT INTO " + table + " ( " + col.substring(0, col.length() - 2);
            z += " ) values(" + dat.substring(0, dat.length() - 3) + ");";
        } catch (Exception e) {
            return null;
        }
        return z;
    }
    
    @Override
    public String addMultipleRows(String table, Vector<String> columns, Vector<Vector<String>> data) {
        StringBuilder col = new StringBuilder("");
        StringBuilder dat;
        String z = "";

        try {
            for (int i = 0; i < columns.size(); i++) {
                col.append(columns.get(i));
                col.append(", ");
            }
            z += "INSERT INTO " + table + " (" + col.substring(0, col.length() - 2) + ") values ";
            
            Vector<String> aux;
            for (int j = 0; j < data.size(); j++) {
                if (j > 0) {
                    z += ",";
                }
                aux = data.get(j);
                dat = new StringBuilder("(");
                
                for (int k = 0; k < aux.size(); k++) {
                    if (k > 0) {
                        dat.append(",");
                    }
                    if (aux.get(k) == null) {
                        dat.append("null");
                    }else{
                        if (aux.get(k).startsWith("_binary 0x")) {
                            String s = aux.get(k).replace("_binary 0x", "");
                            dat.append("decode('").append(s).append("', 'hex')");
                        }else{
                            dat.append("'");
                            dat.append(aux.get(k));
                            dat.append("'");
                        }
                    }
                }
                dat.append(")");
                z += dat.toString();
            }
            z += ";";
        } catch (Exception e) {
            return null;
        }
        return z;
    }

    @Override
    public String selectRowsTable(String table) {
        String z = "SELECT * FROM " + table + ";";
        return z;
    }

    @Override
    public String deleteRow(String table, Vector<String> columns, Vector<String> data) {
        String z = "DELETE FROM " + table + " WHERE";
        for (int i = 0; i < columns.size(); i++) {
            if (data.get(i) != null) {
                z += " " + columns.get(i) + " = '" + data.get(i) + "' AND";
            }else{
                z += " " + columns.get(i) + " IS NULL AND";
            }
        }
        z = z.substring(0, z.length() - 4) + ";";
        return z;
    }

    public String borrarFila(String dato, String columa, String table) {
        String z = "DELETE FROM " + table + " WHERE " + columa + " = '" + dato + "' ;";
        return z;
    }

    @Override
    public String updateRow(String table, Vector<String> columns, Vector<String> data,
            String updateColumn, String newValue) {

        String z = "UPDATE " + table + " SET " + updateColumn + " = '" + newValue + "' WHERE ";
        for (int i = 0; i < columns.size(); i++) {
            if (!updateColumn.equals(columns.get(i))) {
                z += columns.get(i) + " = '" + data.get(i) + "' AND  ";
            }
        }
        z = z.substring(0, z.length() - 6) + ";";
        return z;
    }

//    public String BorrarAllDatosTabla(String tabla) {
//        String z = "DELETE FROM " + tabla + ";";
//        return z;
//    }

    @Override
    public String addColumnTable(String table, String type, String name, String length,
            String Default, boolean noNull) {
        String z = "ALTER TABLE " + table + " ADD(" + name + " " + type;
        if (length != null) {
            z += "(" + length + ")";
        }
        if (Default != null) {
            z += " DEFAULT '" + Default + "'";
        }
        if (noNull) {
            z += " NOT NULL";
        }
        z += ");";
        return z;
    }

    @Override
    public String dropColumnTable(String table, String column) {
        String z = "ALTER TABLE " + table + " DROP " + column + ";";
        return z;
    }

    @Override
    public String addPrimaryKey(String table, String column) {
        String z = "ALTER TABLE " + table + " ADD PRIMARY KEY (" + column + ");";
        return z;
    }
    
    @Override
    public String addPrimaryKey(String table, String column, String name) {
        String z = "ALTER TABLE " + table + " ADD CONSTRAINT " + name + " PRIMARY KEY (" + column + ");";
        return z;
    }

    @Override
    public String addForeignKey(String table, String column, String table_ref, String col_ref)  {
        String z = "ALTER TABLE " + table + " ADD FOREIGN KEY(" + column
                + ") REFERENCES " + table_ref + "(" + col_ref + ");";
        return z;
    }
    
    @Override
    public String getPrimaryKeys(String db) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getForeignKeys(String db) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String addForeignKey(String table, String column, String table_ref, String col_ref, String name) {
        String z = "ALTER TABLE " + table + " ADD CONSTRAINT " + name + " FOREIGN KEY(" + column
                + ") REFERENCES " + table_ref + "(" + col_ref + ");";
        return z;
    }

    @Override
    public String getTriggers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTriggerData(String db, String triggerName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String createTrigger(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String dropTrigger(String triggerName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String addUniqueKey(String table, String column) {
        String z = "ALTER TABLE " + table + " ADD UNIQUE (" + column + ");";
        return z;
    }
    
    @Override
    public String addUniqueKey(String table, String column, String name) {
        String z = "ALTER TABLE " + table + " ADD CONSTRAINT " + name + " UNIQUE (" + column + ");";
        return z;
    }

    @Override
    public String updateColumn(String table, String name, String type, String newName,
            String length, String defaultValue, boolean notNull) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String renameTable(String table, String newName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String addAuto_increment(String table, String column, String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getProcedures(String db) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getProcedureData(String db, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String createProcedure(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String dropProcedure(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String callProcedure(String procedure, String params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getForeignKeys(String db, String table) {
        String z = "SELECT tc.constraint_name as constraint, tc.table_name as tabla, ";
        z += "kcu.column_name as columna, ccu.table_name AS tabla_referencia, ccu.column_name ";
        z += "AS columna_referencia FROM information_schema.table_constraints AS tc ";
        z += "JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name ";
        z += "= kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN ";
        z += "information_schema.constraint_column_usage AS ccu ON ccu.constraint_name ";
        z += "= tc.constraint_name AND ccu.table_schema = tc.table_schema WHERE tc.constraint_type ";
        z += "= 'FOREIGN KEY' AND tc.table_name='" + table + "';";
        return z;
    }
    
    @Override
    public String getForeignKey(String db, String table, String constraint) {
        String z = "SELECT tc.constraint_name as constraint, tc.table_name as tabla, ";
        z += "kcu.column_name as columna, ccu.table_name AS tabla_referencia, ccu.column_name ";
        z += "AS columna_referencia FROM information_schema.table_constraints AS tc ";
        z += "JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name ";
        z += "= kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN ";
        z += "information_schema.constraint_column_usage AS ccu ON ccu.constraint_name ";
        z += "= tc.constraint_name AND ccu.table_schema = tc.table_schema WHERE tc.constraint_type ";
        z += "= 'FOREIGN KEY' AND tc.table_name='" + table + "' AND tc.constraint_name ";
        z += "= '" + constraint + "'; ";
        return z;
    }
    
    @Override
    public String dropForeignKey(String table, String constraint) {
        String z = "ALTER TABLE " + table + " DROP FOREIGN KEY " + constraint + ";";
        return z;
    }

    @Override
    public String getIndexs(String db, String table) {
        String z = "select tablename as tabla, indexname as indice from pg_indexes ";
        z += "where tablename = '" + table + "';";
        return z;
    }
    
    @Override
    public String dropIndex(String table, String constraint) {
        String z = "DROP INDEX  " + constraint + ";";
        return z;
    }
}
