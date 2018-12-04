package conexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {

    protected Connection con;
    protected Statement sta;
    protected String driver;
    public static int MySQL = 1;
    public static int Oracle = 2;
    public int tipo;

    public Conexion(int tipo) {
        this.tipo = tipo;
        if (tipo == MySQL) {
            driver = "com.mysql.jdbc.Driver";
        }else{
            driver = "oracle.jdbc.OracleDriver";
        }
    }

    public boolean conectar(String puerto, String user, String password) {
        String url = "";
        if (tipo == MySQL) {
            url = "jdbc:mysql://" + puerto;
        } else {
            url = "jdbc:oracle:thin:@" + puerto + ":1521:xe";
        }
        con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            sta = con.createStatement();
            if (con != null) {
                System.out.println("Conexion exitosa\n");
            } else {
                System.out.println("no se ha conectado\n");
                return false;
            }
            con.setNetworkTimeout(Executors.newFixedThreadPool(1), 10000);
        } catch (SQLException e) {
            System.out.println("no se ha conectado, error sql\n");
            System.out.println(e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("no se ha conectado, error clase\n");
            System.out.println(e.getMessage());
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
        System.out.println("desconectado");
    }

    public void EjecutarUpdate(String comando) throws SQLException {
        if (sta == null) {
            return;
        }
        sta.executeUpdate(comando);
    }

    public ResultSet EjecutarConsulta(String comando) throws SQLException {
        if (sta == null) {
            return null;
        }
        ResultSet r = sta.executeQuery(comando);
        return r;
    }
    
    public boolean conexionCerrada(){
        try {
            return sta.isClosed();
        } catch (SQLException ex) {
            return true;
        }
    }
}
