package GeneradorSQL;

import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author wilmer
 */
public class GeneradorOracle extends GeneradorSQL {

    public GeneradorOracle() {
    }

    @Override
    public String getDataBases() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String selectDataBase(String db) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String createDataBase(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String dropDataBase(String db) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTables() {
        String z = "SELECT table_name as name FROM user_tables ORDER BY table_name";
        return z;
    }

    @Override
    public String getTables(String db) {
        String z = "SELECT " + db + " FROM user_tables ORDER BY table_name";
        return z;
    }

    @Override
    public String getColumnsTable(String table) {
        String z = "SELECT COLUMN_NAME as nombre, DATA_TYPE as tipo, DATA_PRECISION, "
                + "NULLABLE as nulo, DATA_DEFAULT as defecto, LOW_VALUE, HIGH_VALUE "
                + "FROM all_tab_columns WHERE table_name = '" + table + "'";
        return z;
    }

    @Override
    public String createTable(String name) {
        String z = "CREATE TABLE " + name + "( ID NUMBER(10) )";
        return z;
    }

    @Override
    public String dropTable(String name) {
        String z = "DROP TABLE " + name;
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

        String z = "INSERT INTO " + table + " values(" + str.toString() + ")";
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
            z += " ) values(" + dat.substring(0, dat.length() - 3) + ")";
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
        String z = "SELECT * FROM " + table;
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
        z = z.substring(0, z.length() - 4);
        return z;
    }

    @Override
    public String updateRow(String table, Vector<String> columns, Vector<String> data,
            String updateColumn, String newValue) {

        String z = "UPDATE " + table + " SET " + updateColumn + " = '" + newValue + "' WHERE ";
        for (int i = 0; i < columns.size(); i++) {
            if (!updateColumn.equals(columns.get(i))) {
                z += columns.get(i) + " = '" + data.get(i) + "' AND ";
            }
        }
        z = z.substring(0, z.length() - 5);
        return z;
    }

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
        z += ")";
        return z;
    }

    @Override
    public String dropColumnTable(String table, String column) {
        String z = "ALTER TABLE " + table + " DROP COLUMN " + column;
        return z;
    }

    @Override
    public String addPrimaryKey(String table, String column) {
        String z = "ALTER TABLE " + table + " ADD PRIMARY KEY (" + column + ")";
        return z;
    }
    
    @Override
    public String addPrimaryKey(String table, String column, String name) {
        String z = "ALTER TABLE " + table + " ADD CONSTRAINT " + name + " PRIMARY KEY (" + column + ");";
        return z;
    }

    @Override
    public String addForeignKey(String table, String column, String table_ref, String col_ref) {
        throw new UnsupportedOperationException("Oracle requiere de un nombre para el constraint");
    }

    @Override
    public String getPrimaryKeys(String db) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        String z = "SELECT trigger_name, trigger_type, triggering_event, ";
        z += "table_name, status, trigger_body FROM ALL_TRIGGERS";
        return z;
    }

    @Override
    public String getTriggerData(String db, String triggerName) {
        String z = "SELECT trigger_name, trigger_type, trigger_body, ";
        z += "table_name, triggering_event, status FROM ALL_TRIGGERS ";
        z += "where trigger_name = '" + triggerName + "'";
        return z;
    }

    @Override
    public String createTrigger(String sql) {
        String z = sql;
        return z;
    }

    @Override
    public String dropTrigger(String triggerName) {
        String z = "DROP TRIGGER " + triggerName;
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
    public String updateColumn(String table, String name, String type, String newName,
            String length, String defaultValue, boolean notNull) {
        String z0 = "";
        if (!newName.equals(name)) {
            z0 += "ALTER TABLE " + table + " rename column " + name;
            z0 += " to " + newName + "; ";
        }
        String z = z0 + "ALTER TABLE " + table + " MODIFY " + newName;
        z += " " + type;
        if (length != null && length.length() > 0) {
            z += "(" + length + ")";
        }
        if (defaultValue != null) {
            z += " DEFAULT '" + length + "'";
        }
        if (notNull) {
            z += " NOT NULL";
        }
        return z;
    }

    @Override
    public String renameTable(String table, String newName) {
        String z = "RENAME " + table + " TO " + newName;
        return z;
    }

    @Override
    public String addAuto_increment(String table, String column, String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getProcedures(String db) {
        String z = "SELECT Distinct name FROM all_source WHERE UPPER(owner) = UPPER('" + db + "') AND type = 'PROCEDURE'";
        return z;
    }

    @Override
    public String getProcedureData(String db, String name) {
        String z = "SELECT LISTAGG(TEXT, CHR(13)) WITHIN GROUP (ORDER BY LINE) \"texto\" ";
        z += "FROM  all_source WHERE NAME = '" + name + "' AND type = 'PROCEDURE' ";
        z += "AND UPPER(owner) = UPPER('" + db + "') ORDER BY LINE";
        return z;
    }

    @Override
    public String createProcedure(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String dropProcedure(String name) {
        String z = "DROP PROCEDURE " + name;
        return z;
    }

    @Override
    public String callProcedure(String procedure, String params) {
        String z = "EXEC " + procedure + " (" + params + ");";
        return z;
    }

    @Override
    public String getForeignKeys(String db, String table) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getForeignKey(String db, String table, String constraint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String dropForeignKey(String table, String constraint) {
        String z = "ALTER TABLE " + table + " DROP FOREIGN KEY " + constraint + ";";
        return z;
    }

    @Override
    public String getIndexs(String db, String table) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String dropIndex(String table, String constraint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
