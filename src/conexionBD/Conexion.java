package conexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;

public class Conexion {

    protected Connection con;
    protected Statement sta;
    protected String driver;
    protected String host, puerto, usuario, pass;
    public static int MySQL = 1;
    public static int Oracle = 2;
    public static int PostgreSQL = 3;
    public static int Sqlite = 4;
    public int tipo;

    public Conexion(int tipo) {
        this.tipo = tipo;
        if (tipo == MySQL) {
            driver = "com.mysql.jdbc.Driver";
        } else if (tipo == Oracle) {
            driver = "oracle.jdbc.OracleDriver";
        } else {
            driver = "org.postgresql.Driver";
        }
    }

    public boolean conectar(String host, String puerto, String user, String password) {
        this.host = host;
        usuario = user;

        String url;
        if (tipo == MySQL) {
            url = "jdbc:mysql://" + host + ":" + puerto;
        } else if (tipo == Oracle) {
            url = "jdbc:oracle:thin:@" + host + ":" + puerto + ":xe";
        } else if(tipo == PostgreSQL){
            url = "jdbc:postgresql://" + host + ":" + puerto + "/";
        }else{
            url = "jdbc:sqlite:" + host;
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
            //con.setNetworkTimeout(Executors.newFixedThreadPool(1), 20000);
            pass = Encriptar(password);
        } catch (SQLException e) {
            System.err.println("no se ha conectado, error sql\n");
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
            return false;
        } catch (Exception e) {
            System.err.println("no se ha conectado, error clase\n");
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    protected static String Encriptar(String texto) {
        return Base64.getEncoder().encodeToString(texto.getBytes());
    }

    protected static String Desencriptar(String textoEncriptado) {
        byte[] decodedBytes = Base64.getDecoder().decode(textoEncriptado);
        return new String(decodedBytes);
    }

    public String getUsuario() {
        return usuario;
    }

    public String getHost() {
        return host;
    }

    public String getPuerto() {
        return puerto;
    }
    
    public String getPasswordText() {
        return Desencriptar(pass);
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

    public boolean conexionCerrada() {
        try {
            return sta.isClosed();
        } catch (SQLException ex) {
            return true;
        }
    }
}
