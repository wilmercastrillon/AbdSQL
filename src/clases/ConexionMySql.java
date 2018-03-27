package clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import vistas.consola;

public class ConexionMySql extends Conexion {

    public ConexionMySql() {
        driver = "com.mysql.jdbc.Driver";
        cons = new consola(this);
        BaseDeDatosSeleccionada = "";
        con = null;
        sta = null;
    }

    @Override
    public void EjecutarUpdate(String comando) throws SQLException {
        if (sta == null) {
            return;
        }
        sta.executeUpdate(comando);
        cons.agregar(comando);
    }

    @Override
    public ResultSet EjecutarConsulta(String comando) throws SQLException {
        if (sta == null) {
            return null;
        }
        ResultSet r = sta.executeQuery(comando);
        cons.agregar(comando);
        return r;
    }

    @Override
    public ResultSet GetDataBases() throws SQLException {
        String z = "SHOW DATABASES;";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    @Override
    public void SelectDataBase(String bd) throws SQLException {
        String z = "USE " + bd + ";";
        cons.agregar(z);
        sta.executeUpdate(z);
        BaseDeDatosSeleccionada = bd;
    }

    @Override
    public void CrearDataBase(String nombre) throws SQLException {
        String z = "CREATE DATABASE " + nombre + ";";
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    public void BorrarDataBase(String nombre) throws SQLException {
        String z = "DROP DATABASE " + nombre + ";";
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    @Override
    public ResultSet GetTables() throws SQLException {
        String z = "SHOW TABLES;";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    @Override
    public ResultSet GetColumnasTabla(String table) throws SQLException {
        String z = "DESCRIBE " + table + ";";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    @Override
    public void CrearTabla(String nombre) throws SQLException {
        String z = "Create Table " + nombre + " (id" + nombre.replace(" ", "") + " int NOT NULL);";
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    @Override
    public void BorrarTabla(String nombre) throws SQLException {
        String z = "DROP TABLE IF EXISTS " + nombre + ";";
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    @Override
    public int agregarRegistro(String table, String datos[]) throws SQLException {
        StringBuilder str = new StringBuilder("'");
        for (int i = 0; i < datos.length - 1; i++) {
            datos[i] = datos[i].trim();
            str.append(datos[i]).append("','");
        }
        datos[datos.length - 1] = datos[datos.length - 1].trim();
        str.append(datos[datos.length - 1]).append("'");

        String z = "INSERT INTO " + table + " values(" + str.toString() + ");";
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
            z += " ) values(" + dat.substring(0, dat.length() - 3) + ");";
        } catch (Exception e) {
            System.out.println(z);
            return false;
        }
        cons.agregar(z);
        return sta.executeUpdate(z) != Statement.EXECUTE_FAILED;
    }

    @Override
    public ResultSet GetDatosTabla(String table) throws SQLException {
        String z = "SELECT * FROM " + table + ";";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    @Override
    public void borrarFila(Vector<String> datos, Vector<String> columas, String table) throws SQLException {
        String z = "DELETE FROM " + table + " WHERE";
        for (int i = 0; i < columas.size(); i++) {
            z += " " + columas.get(i) + " = '" + datos.get(i) + "' AND";
        }
        z = z.substring(0, z.length() - 4) + ";";
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    public void borrarFila(String dato, String columa, String table) throws SQLException {
        String z = "DELETE FROM " + table + " WHERE " + columa + " = '" + dato + "' ;";
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    @Override
    public int actualizarFila(String tabla, Vector<String> columnas, Vector<String> datos,
            String columnaCambiar, String datoCambiar) throws SQLException {

        String z = "UPDATE " + tabla + " SET " + columnaCambiar + " = '" + datoCambiar + "' WHERE ";
        for (int i = 0; i < columnas.size(); i++) {
            if (!columnaCambiar.equals(columnas.get(i))) {
                z += columnas.get(i) + " = '" + datos.get(i) + "' AND ";
            }
        }
        z = z.substring(0, z.length() - 5) + ";";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    public int BorrarAllDatosTabla(String tabla) throws SQLException {
        String z = "DELETE FROM " + tabla + ";";
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
        z += ");";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int borrarColumnaTabla(String tabla, String columna) throws SQLException {
        String z = "ALTER TABLE " + tabla + " DROP " + columna + ";";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int crearLlavePrimaria(String tabla, String columna) throws SQLException {
        String z = "ALTER TABLE " + tabla + " ADD PRIMARY KEY (" + columna + ");";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int crearLlaveForanea(String tabla, String atri, String tabla_ref, String atri_ref) throws SQLException {
        String z = "ALTER TABLE " + tabla + " ADD FOREIGN KEY(" + atri
                + ") REFERENCES " + tabla_ref + "(" + atri_ref + ");";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }
    
    @Override
    public ResultSet getTriggers() throws SQLException{
        String z = "SHOW TRIGGERS;";
        cons.agregar(z);
        return sta.executeQuery(z);
    }
    
    @Override
    public ResultSet getDatosTrigger(String BD, String nombreTrigger) throws SQLException{
        String z = "SHOW CREATE TRIGGER " + BD + "." + nombreTrigger + ";";
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
        String z = "DROP TRIGGER IF EXISTS " + nombreTrigger + ";";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int crearLlaveUnique(String tabla, String columna) throws SQLException {
        String z = "ALTER TABLE " + tabla + " ADD UNIQUE (" + columna + ");";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    @Override
    public int actualizarAtributo(String tabla, String columna) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int renombrarTabla(String tabla, String nuevoNombre) throws SQLException {
        String z = "ALTER TABLE " + tabla + " RENAME " + nuevoNombre + ";";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }
}
