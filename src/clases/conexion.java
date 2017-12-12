package clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import vistas.consola;

public class conexion {

    private Connection con;
    private Statement sta;
    private final String driver;
    public consola cons;

    public conexion() {
        driver = "com.mysql.jdbc.Driver";
        cons = new consola(this);
    }

    public boolean conectar(String user, String password, String url) {
        con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            sta = con.createStatement();
            if (con != null) {
                System.out.println("Conexion exitosa\n");
                cons.agregar("Conexion exitosa!!!");
            } else {
                System.out.println("no se ha conectado\n");
                cons.agregar("Error al conectar!!!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("no se ha conectado, error sql\n");
            System.out.println(e.getMessage());
            cons.agregar("Error al conectar!!!");
            return false;
        }catch (ClassNotFoundException e){
            System.out.println("no se ha conectado, error clase\n");
            System.out.println(e.getMessage());
            cons.agregar("Error al conectar!!!");
            return false;
        }
        return true;
    }

    public Connection Getconeccion() {
        return con;
    }

    public void desconectar() {
        con = null;
        sta = null;
        cons.agregar("desconectado");
        System.out.println("desconectado");
    }

    public void EjecutarUpdate(String comando) throws SQLException {
        if (sta == null) {
            return;
        }
        sta.executeUpdate(comando);
        cons.agregar(comando);
    }

    public ResultSet EjecutarConsulta(String comando) throws SQLException {
        if (sta == null) {
            return null;
        }
        ResultSet r = sta.executeQuery(comando);
        cons.agregar(comando);
        return r;
    }

    //metodos ventana princiapl
    public ResultSet GetDataBases() throws SQLException {
        String z = "SHOW DATABASES;";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    public void SelectDataBase(String bd) throws SQLException {
        String z = "USE " + bd + ";";
        cons.agregar(z);
        sta.executeUpdate(z);
    }

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

    public ResultSet TamanioDataBases() throws SQLException {
        String z = "SELECT table_schema \"database_name\", sum( data_length + index_length ) / 1024 /1024 \"Data Base Size in MB\", \n"
                + "COUNT(*) \"numero_de_tablas\" FROM information_schema.TABLES GROUP BY table_schema;";
//        cons.agregar(z);
        return sta.executeQuery(z);
    }

    public ResultSet GetTables() throws SQLException {
        String z = "SHOW TABLES;";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    public ResultSet GetColumnas(String table) throws SQLException {
        String z = "DESCRIBE " + table + ";";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    public void CrearTabla(String nombre) throws SQLException {
        String z = "Create Table " + nombre + " (ID int NOT NULL);";
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    public void BorrarTabla(String nombre) throws SQLException {
        String z = "DROP TABLE IF EXISTS " + nombre + ";";
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    //metodos ventaba tablas
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

    public ResultSet GetDatos(String table) throws SQLException {
        String z = "SELECT * FROM " + table + ";";
        cons.agregar(z);
        return sta.executeQuery(z);
    }

    public void BorrarFila(Vector<String> datos, Vector<String> columas, String table) throws SQLException {
        String z = "DELETE FROM " + table + " WHERE";
        for (int i = 0; i < columas.size(); i++) {
            z += " " + columas.get(i) + " = '" + datos.get(i) + "' AND";
        }
        z = z.substring(0, z.length() - 4) + ";";
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    public void BorrarFila(String dato, String columa, String table) throws SQLException {
        String z = "DELETE FROM " + table + " WHERE " + columa + " = '" + dato + "' ;";
        cons.agregar(z);
        sta.executeUpdate(z);
    }

    public int ActualizarFila(String tabla, Vector<String> columnas, Vector<String> datos,
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

    public int AgregarColumna(String tabla, String tipo, String nombre, String longitud,
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

    public int BorrarColumna(String tabla, String columna) throws SQLException {
        String z = "ALTER TABLE " + tabla + " DROP " + columna + ";";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    public int CrearLlavePrimaria(String tabla, String columna) throws SQLException {
        String z = "ALTER TABLE " + tabla + " ADD PRIMARY KEY (" + columna + ");";
        cons.agregar(z);
        return sta.executeUpdate(z);
    }

    public int CrearLlaveForanea(String tabla, String atri, String tabla_ref, String atri_ref) throws SQLException {
        String z = "ALTER TABLE " + tabla + " ADD FOREIGN KEY(" + atri
                + ") REFERENCES " + tabla_ref + "(" + atri_ref + ");";
//        cons.agregar(z);
        return sta.executeUpdate(z);
    }
}
