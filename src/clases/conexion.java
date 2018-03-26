package clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import vistas.consola;

public abstract class Conexion {
    
    protected Connection con;
    protected Statement sta;
    protected String driver;
    public consola cons;
    public String BaseDeDatosSeleccionada;

    public Conexion() {
        driver = "com.mysql.jdbc.Driver";
        cons = new consola(this);
        BaseDeDatosSeleccionada = "";
    }

    public boolean conectar(String user, String password, String url) {
        con = null;
        BaseDeDatosSeleccionada = "";
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
        } catch (Exception e) {
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

    abstract public ResultSet GetDataBases() throws SQLException;

    abstract public void SelectDataBase(String bd) throws SQLException;

    abstract public void CrearDataBase(String nombre) throws SQLException;

    abstract public ResultSet GetTables() throws SQLException;

    abstract public ResultSet GetColumnasTabla(String table) throws SQLException;

    abstract public void CrearTabla(String nombre) throws SQLException;

    abstract public void BorrarTabla(String nombre) throws SQLException;

    abstract public int agregarRegistro(String table, String datos[]) throws SQLException;

    abstract public boolean agregarRegistro(String table, Vector<String> columnas, Vector<String> datos)
            throws SQLException;

    abstract public ResultSet GetDatosTabla(String table) throws SQLException;

    abstract public void borrarFila(Vector<String> datos, Vector<String> columas, String table) throws SQLException;

    abstract public int actualizarFila(String tabla, Vector<String> columnas, Vector<String> datos,
            String columnaCambiar, String datoCambiar) throws SQLException;

    abstract public int agregarColumnaTabla(String tabla, String tipo, String nombre, String longitud,
            String Default, boolean Nonulo) throws SQLException;

    abstract public int borrarColumnaTabla(String tabla, String columna) throws SQLException;

    abstract public int crearLlavePrimaria(String tabla, String columna) throws SQLException;

    abstract public int crearLlaveForanea(String tabla, String atri, String tabla_ref, String atri_ref) 
            throws SQLException;
    
    abstract public ResultSet getTriggers() throws SQLException;
    
    abstract public ResultSet getDatosTrigger(String BD, String nombreTrigger) throws SQLException;
    
    abstract public int crearTrigger(String sql) throws SQLException;
    
    abstract public int borrarTrigger(String nombreTrigger) throws SQLException;
    
    abstract public int crearLlaveUnique(String tabla, String columna) throws SQLException;
    
    abstract public int actualizarAtributo(String tabla, String columna) throws SQLException;
    
    abstract public int renombrarTabla(String tabla, String nuevoNombre) throws SQLException;
}
