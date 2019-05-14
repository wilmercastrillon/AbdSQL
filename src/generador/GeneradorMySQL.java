package generador;

import java.util.Vector;

/**
 *
 * @author wilmer
 */
public class GeneradorMySQL extends GeneradorSQL {

    public GeneradorMySQL() {
    }

    @Override
    public String GetDataBases() {
        String z = "SHOW DATABASES;";
        return z;
    }

    @Override
    public String SelectDataBase(String bd) {
        String z = "USE " + bd + ";";
        return z;
    }

    @Override
    public String CrearDataBase(String nombre) {
        String z = "CREATE DATABASE " + nombre + ";";
        return z;
    }

    @Override
    public String BorrarDataBase(String nombre) {
        String z = "DROP DATABASE IF EXISTS " + nombre + ";";
        return z;
    }

    @Override
    public String GetTables() {
        String z = "SHOW TABLES;";
        return z;
    }

    @Override
    public String GetTables(String bd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String GetColumnasTabla(String table) {
        String z = "DESCRIBE " + table + ";";
        return z;
    }

    @Override
    public String CrearTabla(String nombre) {
        String z = "Create Table " + nombre + " (id" + nombre.replace(" ", "") + " int NOT NULL);";
        return z;
    }

    @Override
    public String BorrarTabla(String nombre) {
        String z = "DROP TABLE IF EXISTS " + nombre + ";";
        return z;
    }

    @Override
    public String agregarRegistro(String table, String datos[]) {
        StringBuilder str = new StringBuilder("'");
        for (int i = 0; i < datos.length - 1; i++) {
            datos[i] = datos[i].trim();
            str.append(datos[i]).append("','");
        }
        datos[datos.length - 1] = datos[datos.length - 1].trim();
        str.append(datos[datos.length - 1]).append("'");

        String z = "INSERT INTO " + table + " values(" + str.toString() + ");";
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
            z += " ) values(" + dat.substring(0, dat.length() - 3) + ");";
        } catch (Exception e) {
            return null;
        }
        return z;
    }

    @Override
    public String GetDatosTabla(String table) {
        String z = "SELECT * FROM " + table + ";";
        return z;
    }

    @Override
    public String borrarFila(Vector<String> datos, Vector<String> columas, String table) {
        String z = "DELETE FROM " + table + " WHERE";
        for (int i = 0; i < columas.size(); i++) {
            if (datos.get(i) != null) {
                z += " " + columas.get(i) + " = '" + datos.get(i) + "' AND";
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
    public String actualizarFila(String tabla, Vector<String> columnas, Vector<String> datos,
            String columnaCambiar, String datoCambiar) {

        String z = "UPDATE " + tabla + " SET " + columnaCambiar + " = '" + datoCambiar + "' WHERE ";
        for (int i = 0; i < columnas.size(); i++) {
            if (!columnaCambiar.equals(columnas.get(i))) {
                z += columnas.get(i) + " = '" + datos.get(i) + "' AND  ";
            }
        }
        z = z.substring(0, z.length() - 6) + ";";
        return z;
    }

    public String BorrarAllDatosTabla(String tabla) {
        String z = "DELETE FROM " + tabla + ";";
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
        z += ");";
        return z;
    }

    @Override
    public String borrarColumnaTabla(String tabla, String columna) {
        String z = "ALTER TABLE " + tabla + " DROP " + columna + ";";
        return z;
    }

    @Override
    public String crearLlavePrimaria(String tabla, String columna) {
        String z = "ALTER TABLE " + tabla + " ADD PRIMARY KEY (" + columna + ");";
        return z;
    }

    @Override
    public String crearLlaveForanea(String tabla, String atri, String tabla_ref, String atri_ref) {
        String z = "ALTER TABLE " + tabla + " ADD FOREIGN KEY(" + atri
                + ") REFERENCES " + tabla_ref + "(" + atri_ref + ");";
        return z;
    }

    @Override
    public String crearLlaveForanea(String nombreConstraint, String tabla, String atri, String tabla_ref, String atri_ref) {
        String z = "ALTER TABLE " + tabla + " ADD CONSTRAINT " + nombreConstraint + " FOREIGN KEY(" + atri
                + ") REFERENCES " + tabla_ref + "(" + atri_ref + ");";
        return z;
    }

    @Override
    public String getTriggers() {
        String z = "SHOW TRIGGERS;";
        return z;
    }

    @Override
    public String getDatosTrigger(String BD, String nombreTrigger) {
        String z = "SHOW CREATE TRIGGER " + BD + "." + nombreTrigger + ";";
        return z;
    }

    @Override
    public String crearTrigger(String sql) {
        String z = sql;
        return z;
    }

    @Override
    public String borrarTrigger(String nombreTrigger) {
        String z = "DROP TRIGGER IF EXISTS " + nombreTrigger + ";";
        return z;
    }

    @Override
    public String crearLlaveUnique(String tabla, String columna) {
        String z = "ALTER TABLE " + tabla + " ADD UNIQUE (" + columna + ");";
        return z;
    }

    @Override
    public String actualizarAtributo(String tabla, String nombre, String tipo, String Nuevonombre,
            String longitud, String Default, boolean Nonulo) {

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
    public String renombrarTabla(String tabla, String nuevoNombre) {
        String z = "ALTER TABLE " + tabla + " RENAME " + nuevoNombre + ";";
        return z;
    }

    @Override
    public String CrearAuto_increment(String tabla, String columna, String tipo) {
        String z = "ALTER TABLE " + tabla + " CHANGE " + columna;
        z += " " + columna + " " + tipo + "  AUTO_INCREMENT;";
        return z;
    }

    @Override
    public String getProcedimientos(String BD) {
        String z = "SELECT ROUTINE_NAME FROM INFORMATION_SCHEMA.ROUTINES ";
        z += "WHERE ROUTINE_TYPE = 'PROCEDURE' AND ROUTINE_SCHEMA = '" + BD + "' ORDER BY ROUTINE_NAME;";
        return z;
    }

    public String getParametrosProcedimiento(String nombreP) {
        String z = "SELECT DATA_TYPE, PARAMETER_NAME FROM INFORMATION_SCHEMA.PARAMETERS ";
        z += "WHERE ROUTINE_TYPE='PROCEDURE' AND SPECIFIC_NAME = '" + nombreP + "' ";
        z += "ORDER BY ORDINAL_POSITION; ";
        return z;
    }

    @Override
    public String getDatosProcedimiento(String BD, String nombreP) {
        String z = " SELECT ROUTINE_NAME, ROUTINE_DEFINITION  FROM INFORMATION_SCHEMA.ROUTINES ";
        z += "WHERE ROUTINE_TYPE = 'PROCEDURE' AND ROUTINE_SCHEMA = '" + BD + "' ";
        z += "AND ROUTINE_NAME = '" + nombreP + "';";
        return z;
    }

    @Override
    public String crearProcedimiento(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String borrarProcedimiento(String nombreP) {
        String z = "DROP PROCEDURE IF EXISTS " + nombreP + ";";
        return z;
    }

    @Override
    public String LlamarProcedimiento(String procedimiento, String parametros) {
        String z = "CALL " + procedimiento + " (" + parametros + ");";
        return z;
    }
}
