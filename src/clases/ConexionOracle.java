package clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import vistas.consola;

public class ConexionOracle extends Conexion {

    public ConexionOracle() {
        cons = new consola(this);
        BaseDeDatosSeleccionada = "";
//        url = "jdbc:oracle:thin:@localhost:1521:xe"; //xe;oracleBD;
        driver = "oracle.jdbc.OracleDriver";
        con = null;
        sta = null;
    }

    @Override
    public ResultSet GetDataBases() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void SelectDataBase(String bd) throws SQLException {
    }

    @Override
    public void CrearDataBase(String nombre) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResultSet GetTables() throws SQLException {
        String z = "SELECT table_name FROM user_tables ORDER BY table_name";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    @Override
    public ResultSet GetColumnasTabla(String table) throws SQLException {
//        String z = "DESC " + table;
        String z = "SELECT COLUMN_NAME, DATA_TYPE, DATA_PRECISION, NULLABLE, DATA_DEFAULT, LOW_VALUE, HIGH_VALUE"
                + " FROM all_tab_columns WHERE table_name = '" + table + "'";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    @Override
    public void CrearTabla(String nombre) throws SQLException {
        String z = "CREATE TABLE " + nombre + "( ID NUMBER(10) )";
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    @Override
    public void BorrarTabla(String nombre) throws SQLException {
        String z = "DROP TABLE " + nombre;
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    @Override
    public int agregarRegistro(String table, String[] datos) throws SQLException {
        StringBuilder str = new StringBuilder("'");
        for (int i = 0; i < datos.length - 1; i++) {
            datos[i] = datos[i].trim();
            str.append(datos[i]).append("','");
        }
        datos[datos.length - 1] = datos[datos.length - 1].trim();
        str.append(datos[datos.length - 1]).append("'");

        String z = "INSERT INTO " + table + " values(" + str.toString() + ")";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public boolean agregarRegistro(String table, Vector<String> columnas, Vector<String> datos) throws SQLException {
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
            System.out.println(z);
            return false;
        }
        cons.agregar(z);
        return sta.executeUpdate(z) != Statement.EXECUTE_FAILED;
    }

    @Override
    public ResultSet GetDatosTabla(String table) throws SQLException {
        String z = "SELECT * FROM " + table;
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    @Override
    public void borrarFila(Vector<String> datos, Vector<String> columas, String table) throws SQLException {
        String z = "DELETE FROM " + table + " WHERE";
        for (int i = 0; i < columas.size(); i++) {
            if (datos.get(i) != null) {
                z += " " + columas.get(i) + " = '" + datos.get(i) + "' AND";
            } else {
                z += " " + columas.get(i) + " IS NULL AND";
            }
        }
        z = z.substring(0, z.length() - 4);
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    @Override
    public int actualizarFila(String tabla, Vector<String> columnas, Vector<String> datos, String columnaCambiar,
            String datoCambiar) throws SQLException {

        String z = "UPDATE " + tabla + " SET " + columnaCambiar + " = '" + datoCambiar + "' WHERE ";
        for (int i = 0; i < columnas.size(); i++) {
            if (!columnaCambiar.equals(columnas.get(i))) {
                z += columnas.get(i) + " = '" + datos.get(i) + "' AND ";
            }
        }
        z = z.substring(0, z.length() - 5);
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int agregarColumnaTabla(String tabla, String tipo, String nombre, String longitud,
            String Default, boolean Nonulo) throws SQLException {
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
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int borrarColumnaTabla(String tabla, String columna) throws SQLException {
        String z = "ALTER TABLE " + tabla + " DROP COLUMN " + columna;
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int crearLlavePrimaria(String tabla, String columna) throws SQLException {
        String z = "ALTER TABLE " + tabla + " ADD PRIMARY KEY (" + columna + ")";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int crearLlaveForanea(String tabla, String atri, String tabla_ref, String atri_ref) throws SQLException {
        String constraint = JOptionPane.showInputDialog(null, "ingrese nombre constraint:");
        if (constraint == null) {
            return Statement.EXECUTE_FAILED;
        }

        String z = "ALTER TABLE " + tabla + " ADD CONSTRAINT " + constraint
                + " FOREIGN KEY(" + atri + ") REFERENCES " + tabla_ref + "(" + atri_ref + ")";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public ResultSet getTriggers() throws SQLException {
        String z = "SELECT trigger_name, trigger_type, triggering_event, ";
        z += "table_name, status, trigger_body FROM ALL_TRIGGERS";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    @Override
    public ResultSet getDatosTrigger(String BD, String nombreTrigger) throws SQLException {
        String z = "SELECT trigger_name, trigger_type, trigger_body, ";
        z += "table_name, triggering_event, status FROM ALL_TRIGGERS ";
        z += "where trigger_name = '" + nombreTrigger + "'";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    @Override
    public int crearTrigger(String sql) throws SQLException {
        String z = sql;
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int borrarTrigger(String nombreTrigger) throws SQLException {
        String z = "DROP TRIGGER " + nombreTrigger;
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int crearLlaveUnique(String tabla, String columna) throws SQLException {
        String z = "ALTER TABLE " + tabla + " ADD UNIQUE (" + columna + ")";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int actualizarAtributo(String tabla, String nombre, String tipo, String Nuevonombre,
            String longitud, String Default, boolean Nonulo) throws SQLException {
        if (!Nuevonombre.equals(nombre)) {
            String z0 = "alter table " + tabla + " rename column " + nombre;
            z0 += " to " + Nuevonombre + ";";
            cons.agregar(z0);
            if (Statement.EXECUTE_FAILED == sta.executeUpdate(z0)) {
                System.out.println("Fallo");
                return Statement.EXECUTE_FAILED;
            }
        }
        String z = "ALTER TABLE " + tabla + " MODIFY " + Nuevonombre;
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
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int renombrarTabla(String tabla, String nuevoNombre) throws SQLException {
        String z = "RENAME " + tabla + " TO " + nuevoNombre;
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int CrearAuto_increment(String tabla, String columna, String tipo) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet getProcedimientos(String BD) throws SQLException {
        String z = "SELECT Distinct name FROM all_source WHERE UPPER(owner) = UPPER('"+BD+"') AND type = 'PROCEDURE'";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    @Override
    public ResultSet getDatosProcedimiento(String BD, String nombreP) throws SQLException {
        String z = "SELECT LISTAGG(TEXT, CHR(13)) WITHIN GROUP (ORDER BY LINE) \"texto\" ";
        z += "FROM  all_source WHERE NAME = '" + nombreP + "' AND type = 'PROCEDURE' ";
        z += "AND UPPER(owner) = UPPER('" + BD + "') ORDER BY LINE";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    @Override
    public int crearProcedimiento(String sql) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int borrarProcedimiento(String nombreP) throws SQLException {
        String z = "DROP PROCEDURE " + nombreP;
        cons.agregar(z);
        return sta.executeUpdate(z);
    }
}
