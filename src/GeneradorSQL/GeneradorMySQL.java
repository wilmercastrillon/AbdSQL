package GeneradorSQL;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author wilmer
 */
public class GeneradorMySQL extends GeneradorSQL {

    //SHOW CREATE TABLE "nobre de la tabla";
    public GeneradorMySQL() {
    }

    @Override
    public String getDataBases() {
        String z = "SHOW DATABASES;";
        return z;
    }

    @Override
    public String selectDataBase(String db) {
        String z = "USE " + db + ";";
        return z;
    }

    @Override
    public String createDataBase(String name) {
        String z = "CREATE DATABASE " + name + ";";
        return z;
    }

    @Override
    public String dropDataBase(String db) {
        String z = "DROP DATABASE IF EXISTS " + db + ";";
        return z;
    }

    @Override
    public String getTables() {
        String z = "SELECT table_name as 'nombre' FROM information_schema.tables where ";
        z += "table_schema = database() order by 'nombre';";
        return z;
    }

    @Override
    public String getTables(String db) {
        String z = "SELECT table_name as 'nombre' FROM information_schema.tables WHERE table_schema ='";
        z += db + "';";
        return z;
    }

    @Override
    public String getColumnsTable(String table) {
        //String z = "DESCRIBE " + table + ";";
        String z = "SELECT column_name as 'nombre', column_type as 'tipo', if(is_nullable='yes', 'si', 'no') ";
        z += "as 'nulo', column_default as 'default', character_maximum_length as 'length', column_key as ";
        z += "'key', extra FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = '";
        z += table + "' order by ordinal_position;";
        return z;
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
            z += System.lineSeparator();

            Vector<String> aux;
            for (int j = 0; j < data.size(); j++) {
                if (j > 0) {
                    z += ",";
                    z += System.lineSeparator();
                }
                aux = data.get(j);
                dat = new StringBuilder("(");

                for (int k = 0; k < aux.size(); k++) {
                    if (k > 0) {
                        dat.append(",");
                    }
                    if (aux.get(k) == null) {
                        dat.append("null");
                    } else {
                        dat.append("'");
                        dat.append(aux.get(k));
                        dat.append("'");
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
            } else {
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
    public String addPrimaryKey(String table, ArrayList<String> columns, String name){
        String z = "ALTER TABLE " + table + " ADD CONSTRAINT " + name + " PRIMARY KEY (";
        z = columns.stream().map((s) -> s + ",").reduce(z, String::concat);
        z = z.substring(0, z.length() - 1) + ");";
        return z;
    }

    @Override
    public String addForeignKey(String table, String column, String table_ref, String col_ref) {
        String z = "ALTER TABLE " + table + " ADD FOREIGN KEY(" + column
                + ") REFERENCES " + table_ref + "(" + col_ref + ");";
        return z;
    }

    @Override
    public String getPrimaryKeys(String db) {
        String sql = "select constraint_name as 'nombre', table_name as 'tabla', group_concat(column_name) ";
        sql += "as 'columnas' from information_schema.key_column_usage where constraint_schema = '";
        sql += db + "' and constraint_name = 'primary' group by table_name ;";
        return sql;
    }

    @Override
    public String getForeignKeys(String db) {
        String sql = "select constraint_name as 'nombre', table_name as 'tabla', group_concat(column_name) as ";
        sql += "'columnas', referenced_table_name as 'tabla_referencia', group_concat(referenced_column_name) as";
        sql += "'columnas_referencia' from information_schema.key_column_usage where ";
        sql += "constraint_schema = '" + db + "' and constraint_name != 'primary' and referenced_table_name ";
        sql += "is not null group by constraint_name ;";
        return sql;
    }

    @Override
    public String addForeignKey(String table, String column, String table_ref, String col_ref, String name) {
        String z = "ALTER TABLE " + table + " ADD CONSTRAINT " + name + " FOREIGN KEY(" + column
                + ") REFERENCES " + table_ref + "(" + col_ref + ");";
        return z;
    }

    @Override
    public String getTriggers() {
        String z = "SHOW TRIGGERS;";
        return z;
    }

    @Override
    public String getTriggerData(String db, String triggerName) {
        String z = "SHOW CREATE TRIGGER " + db + "." + triggerName + ";";
        return z;
    }

    @Override
    public String createTrigger(String sql) {
        String z = sql;
        return z;
    }

    @Override
    public String dropTrigger(String triggerName) {
        String z = "DROP TRIGGER IF EXISTS " + triggerName + ";";
        return z;
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
    public String addUniqueKey(String table, ArrayList<String> columns, String name){
        String z = "ALTER TABLE " + table + " ADD CONSTRAINT " + name + " UNIQUE (";
        z = columns.stream().map((s) -> s + ",").reduce(z, String::concat);
        z = z.substring(0, z.length() - 1) + ");";
        return z;
    }

    @Override
    public String updateColumn(String table, String name, String type, String newName,
            String length, String defaultValue, boolean notNull) {

        String z = "ALTER TABLE " + table + " CHANGE " + name;
        z += " " + newName + " " + type;
        if (length != null && length.length() > 0) {
            z += "(" + length + ")";
        }
        if (defaultValue != null) {
            z += " DEFAULT '" + defaultValue + "'";
        }
        if (notNull) {
            z += " NOT NULL";
        }
        z += ";";
        return z;
    }

    public String actualizarAtributo(String tabla, String nombre, String tipo, String Nuevonombre,
            String longitud, String Default, boolean Nonulo, boolean primera, String despuesDe) {

        String z = "ALTER TABLE " + tabla + " CHANGE " + nombre;
        z += " " + Nuevonombre + " " + tipo;
        if (longitud != null && longitud.length() > 0) {
            z += "(" + longitud + ")";
        }
        if (Default != null) {
            z += " DEFAULT '" + Default + "'";
        }
        if (Nonulo) {
            z += " NOT NULL";
        }
        if (primera) {
            z += " FIRST";
        } else if (despuesDe != null) {
            z += " AFTER " + despuesDe;
        }
        z += ";";
        return z;
    }

    @Override
    public String renameTable(String table, String newName) {
        String z = "ALTER TABLE " + table + " RENAME " + newName + ";";
        return z;
    }

    @Override
    public String addAuto_increment(String table, String column, String type) {
        String z = "ALTER TABLE " + table + " CHANGE " + column;
        z += " " + column + " " + type + "  AUTO_INCREMENT;";
        return z;
    }

    @Override
    public String getProcedures(String db) {
        String z = "SELECT ROUTINE_NAME FROM INFORMATION_SCHEMA.ROUTINES ";
        z += "WHERE ROUTINE_TYPE = 'PROCEDURE' AND ROUTINE_SCHEMA = '" + db + "' ORDER BY ROUTINE_NAME;";
        return z;
    }

    public String getParametrosProcedimiento(String nombreP) {
        String z = "SELECT DATA_TYPE, PARAMETER_NAME FROM INFORMATION_SCHEMA.PARAMETERS ";
        z += "WHERE ROUTINE_TYPE='PROCEDURE' AND SPECIFIC_NAME = '" + nombreP + "' ";
        z += "ORDER BY ORDINAL_POSITION; ";
        return z;
    }

    @Override
    public String getProcedureData(String db, String name) {
        String z = " SELECT ROUTINE_NAME, ROUTINE_DEFINITION  FROM INFORMATION_SCHEMA.ROUTINES ";
        z += "WHERE ROUTINE_TYPE = 'PROCEDURE' AND ROUTINE_SCHEMA = '" + db + "' ";
        z += "AND ROUTINE_NAME = '" + name + "';";
        return z;
    }

    @Override
    public String createProcedure(String sql) {
        String z = sql;
        return z;
    }

    @Override
    public String dropProcedure(String name) {
        String z = "DROP PROCEDURE IF EXISTS " + name + ";";
        return z;
    }

    @Override
    public String callProcedure(String procedure, String params) {
        String z = "CALL " + procedure + " (" + params + ");";
        return z;
    }

    @Override
    public String getForeignKeys(String db, String table) {
        String z = "SELECT CONSTRAINT_NAME as 'constraint', TABLE_NAME as 'tabla', ";
        z += "COLUMN_NAME as 'columna', REFERENCED_TABLE_NAME as 'tabla_referencia', ";
        z += "REFERENCED_COLUMN_NAME as 'columna_referencia' FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE ";
        z += "WHERE REFERENCED_TABLE_SCHEMA = '" + db + "' AND TABLE_NAME='" + table + "';";
        return z;
    }

    @Override
    public String getForeignKey(String db, String table, String constraint) {
        String z = "SELECT CONSTRAINT_NAME as 'constraint', TABLE_NAME as 'tabla', ";
        z += "COLUMN_NAME as 'columna', REFERENCED_TABLE_NAME as 'tabla_referencia', ";
        z += "REFERENCED_COLUMN_NAME as 'columna_referencia' FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE ";
        z += "WHERE REFERENCED_TABLE_SCHEMA = '" + db + "' AND TABLE_NAME='" + table + "' ";
        z += "AND CONSTRAINT_NAME = '" + constraint + "';";
        return z;
    }

    @Override
    public String dropForeignKey(String table, String constraint) {
        String z = "ALTER TABLE " + table + " DROP FOREIGN KEY " + constraint + "; ";
        return z;
    }

    @Override
    public String getIndexs(String db, String table) {
        String z = "SELECT TABLE_NAME AS 'tabla', INDEX_NAME AS 'indice' FROM INFORMATION_SCHEMA.STATISTICS ";
        z += "WHERE TABLE_SCHEMA = '" + db + "' AND TABLE_NAME = '" + table + "';";
        return z;
    }

    @Override
    public String dropIndex(String table, String constraint) {
        String z = "ALTER TABLE " + table + " DROP INDEX " + constraint + ";";
        return z;
    }

    @Override
    public String SingleAddColumnTable(String type, String name, String length, String Default, boolean noNull) {
        String z = name + " " + type;
        if (length != null) {
            z += "(" + length + ")";
        }
        if (Default != null) {
            z += " DEFAULT '" + Default + "'";
        }
        if (noNull) {
            z += " NOT NULL";
        }
        return z;
    }
    
    @Override
    public String getColumsConstraint(String db, String table, String constraint){
        String z = "SELECT key_column_usage.column_name FROM information_schema.key_column_usage ";
        z += "WHERE table_schema = '" + db + "' AND constraint_name = '" + constraint + "' ";
        z += "AND table_name = '" + table + "';";
        return z;
    }
}
