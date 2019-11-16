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
    public String GetDataBases() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String SelectDataBase(String bd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String CrearDataBase(String nombre) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String BorrarDataBase(String nombre) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String GetTables() {
        String z = "SELECT table_name FROM user_tables ORDER BY table_name";
        return z;
    }

    @Override
    public String GetTables(String bd) {
        String z = "SELECT " + bd + " FROM user_tables ORDER BY table_name";
        return z;
    }

    @Override
    public String GetColumnasTabla(String table) {
        String z = "SELECT COLUMN_NAME, DATA_TYPE, DATA_PRECISION, NULLABLE, DATA_DEFAULT, LOW_VALUE, HIGH_VALUE"
                + " FROM all_tab_columns WHERE table_name = '" + table + "'";
        return z;
    }

    @Override
    public String CrearTabla(String nombre) {
        String z = "CREATE TABLE " + nombre + "( ID NUMBER(10) )";
        return z;
    }

    @Override
    public String BorrarTabla(String nombre) {
        String z = "DROP TABLE " + nombre;
        return z;
    }

    @Override
    public String agregarRegistro(String table, String[] datos) {
        StringBuilder str = new StringBuilder("'");
        for (int i = 0; i < datos.length - 1; i++) {
            datos[i] = datos[i].trim();
            str.append(datos[i]).append("','");
        }
        datos[datos.length - 1] = datos[datos.length - 1].trim();
        str.append(datos[datos.length - 1]).append("'");

        String z = "INSERT INTO " + table + " values(" + str.toString() + ")";
        return z;
    }

    @Override
    public String agregarRegistro(String table, Vector<String> columnas, Vector<String> datos) {
        StringBuilder col = new StringBuilder("");
        StringBuilder dat = new StringBuilder("'");
        String z = "";

        try {
            for (int i = 0; i < columnas.size(); i++) {
                if (datos.get(i) != null) {
                    col.append(columnas.get(i));
                    col.append(", ");
                    dat.append(datos.get(i));
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
    public String agregarMultiplesRegistros(String table, Vector<String> columnas, Vector<Vector<String>> datos) {
        StringBuilder col = new StringBuilder("");
        StringBuilder dat;
        String z = "";

        try {
            for (int i = 0; i < columnas.size(); i++) {
                col.append(columnas.get(i));
                col.append(", ");
            }
            z += "INSERT INTO " + table + " (" + col.substring(0, col.length() - 2) + ") values ";

            Vector<String> aux;
            for (int j = 0; j < datos.size(); j++) {
                if (j > 0) {
                    z += ",";
                }
                aux = datos.get(j);
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
    public String GetDatosTabla(String table) {
        String z = "SELECT * FROM " + table;
        return z;
    }

    @Override
    public String borrarFila(Vector<String> datos, Vector<String> columas, String table) {
        String z = "DELETE FROM " + table + " WHERE";
        for (int i = 0; i < columas.size(); i++) {
            if (datos.get(i) != null) {
                z += " " + columas.get(i) + " = '" + datos.get(i) + "' AND";
            } else {
                z += " " + columas.get(i) + " IS NULL AND";
            }
        }
        z = z.substring(0, z.length() - 4);
        return z;
    }

    @Override
    public String actualizarFila(String tabla, Vector<String> columnas, Vector<String> datos, String columnaCambiar,
            String datoCambiar) {

        String z = "UPDATE " + tabla + " SET " + columnaCambiar + " = '" + datoCambiar + "' WHERE ";
        for (int i = 0; i < columnas.size(); i++) {
            if (!columnaCambiar.equals(columnas.get(i))) {
                z += columnas.get(i) + " = '" + datos.get(i) + "' AND ";
            }
        }
        z = z.substring(0, z.length() - 5);
        return z;
    }

    @Override
    public String agregarColumnaTabla(String tabla, String tipo, String nombre, String longitud,
            String Default, boolean Nonulo) {
        String z = "ALTER TABLE " + tabla + " ADD(" + nombre + " " + tipo;
        if (longitud != null) {
            z += "(" + longitud + ")";
        }
        if (Default != null) {
            z += " DEFAULT '" + Default + "'";
        }
        if (Nonulo) {
            z += " NOT NULL";
        }
        z += ")";
        return z;
    }

    @Override
    public String borrarColumnaTabla(String tabla, String columna) {
        String z = "ALTER TABLE " + tabla + " DROP COLUMN " + columna;
        return z;
    }

    @Override
    public String crearLlavePrimaria(String tabla, String columna) {
        String z = "ALTER TABLE " + tabla + " ADD PRIMARY KEY (" + columna + ")";
        return z;
    }
    
    @Override
    public String crearLlavePrimaria(String tabla, String columna, String nombre) {
        String z = "ALTER TABLE " + tabla + " ADD CONSTRAINT " + nombre + " PRIMARY KEY (" + columna + ");";
        return z;
    }

    @Override
    public String crearLlaveForanea(String tabla, String atri, String tabla_ref, String atri_ref) {
        throw new UnsupportedOperationException("Oracle requiere de un nombre para el constraint");
    }

    @Override
    public String crearLlaveForanea(String tabla, String atri, String tabla_ref, String atri_ref, String nombre) {
        String z = "ALTER TABLE " + tabla + " ADD CONSTRAINT " + nombre + " FOREIGN KEY(" + atri
                + ") REFERENCES " + tabla_ref + "(" + atri_ref + ");";
        return z;
    }

    @Override
    public String consultarLlavesPrimarias(String bd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String consultarLlavesForaneas(String bd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTriggers() {
        String z = "SELECT trigger_name, trigger_type, triggering_event, ";
        z += "table_name, status, trigger_body FROM ALL_TRIGGERS";
        return z;
    }

    @Override
    public String getDatosTrigger(String BD, String nombreTrigger) {
        String z = "SELECT trigger_name, trigger_type, trigger_body, ";
        z += "table_name, triggering_event, status FROM ALL_TRIGGERS ";
        z += "where trigger_name = '" + nombreTrigger + "'";
        return z;
    }

    @Override
    public String crearTrigger(String sql) {
        String z = sql;
        return z;
    }

    @Override
    public String borrarTrigger(String nombreTrigger) {
        String z = "DROP TRIGGER " + nombreTrigger;
        return z;
    }

    @Override
    public String crearLlaveUnique(String tabla, String columna) {
        String z = "ALTER TABLE " + tabla + " ADD UNIQUE (" + columna + ");";
        return z;
    }
    
    @Override
    public String crearLlaveUnique(String tabla, String columna, String nombre) {
        String z = "ALTER TABLE " + tabla + " ADD CONSTRAINT " + nombre + " UNIQUE (" + columna + ");";
        return z;
    }

    @Override
    public String actualizarAtributo(String tabla, String nombre, String tipo, String Nuevonombre,
            String longitud, String Default, boolean Nonulo) {
        String z0 = "";
        if (!Nuevonombre.equals(nombre)) {
            z0 += "ALTER TABLE " + tabla + " rename column " + nombre;
            z0 += " to " + Nuevonombre + "; ";
        }
        String z = z0 + "ALTER TABLE " + tabla + " MODIFY " + Nuevonombre;
        z += " " + tipo;
        if (longitud != null && longitud.length() > 0) {
            z += "(" + longitud + ")";
        }
        if (Default != null) {
            z += " DEFAULT '" + Default + "'";
        }
        if (Nonulo) {
            z += " NOT NULL";
        }
        return z;
    }

    @Override
    public String renombrarTabla(String tabla, String nuevoNombre) {
        String z = "RENAME " + tabla + " TO " + nuevoNombre;
        return z;
    }

    @Override
    public String CrearAuto_increment(String tabla, String columna, String tipo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getProcedimientos(String BD) {
        String z = "SELECT Distinct name FROM all_source WHERE UPPER(owner) = UPPER('" + BD + "') AND type = 'PROCEDURE'";
        return z;
    }

    @Override
    public String getDatosProcedimiento(String BD, String nombreP) {
        String z = "SELECT LISTAGG(TEXT, CHR(13)) WITHIN GROUP (ORDER BY LINE) \"texto\" ";
        z += "FROM  all_source WHERE NAME = '" + nombreP + "' AND type = 'PROCEDURE' ";
        z += "AND UPPER(owner) = UPPER('" + BD + "') ORDER BY LINE";
        return z;
    }

    @Override
    public String crearProcedimiento(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String borrarProcedimiento(String nombreP) {
        String z = "DROP PROCEDURE " + nombreP;
        return z;
    }

    @Override
    public String LlamarProcedimiento(String procedimiento, String parametros) {
        String z = "EXEC " + procedimiento + " (" + parametros + ");";
        return z;
    }

    @Override
    public String getForaneasTabla(String BD, String tabla) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getForanea(String BD, String tabla, String constraint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String borrarForanea(String tabla, String constraint) {
        String z = "ALTER TABLE " + tabla + " DROP FOREIGN KEY " + constraint + ";";
        return z;
    }

    @Override
    public String getIndicesTabla(String BD, String tabla) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String borrarIndex(String tabla, String constraint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
