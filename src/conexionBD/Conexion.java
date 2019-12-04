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
    private String puerto, usuario, pass;
    public static int MySQL = 1;
    public static int Oracle = 2;
    public static int PostgreSQL = 3;
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

    public boolean conectar(String puerto, String user, String password) {
        this.puerto = puerto;
        usuario = user;

        String url;
        if (tipo == MySQL) {
            url = "jdbc:mysql://" + puerto;
        } else if (tipo == Oracle) {
            url = "jdbc:oracle:thin:@" + puerto + ":1521:xe";
        } else {
            url = "jdbc:postgresql://" + puerto + ":5432/";
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

    public boolean conectar(String puerto, String user, String password, String bd) {
        this.puerto = puerto;
        usuario = user;

        String url;
        if (tipo == MySQL) {
            url = "jdbc:mysql://" + puerto + "/" + bd;
        } else if (tipo == Oracle) {
            url = "jdbc:oracle:thin:@" + puerto + ":1521:xe/" + bd;
        } else {
            url = "jdbc:postgresql://" + puerto + ":5432/" + bd;
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
            con.setNetworkTimeout(Executors.newFixedThreadPool(1), 20000);
            pass = Encriptar(password);
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
    
    private static String Encriptar(String texto) {
        return Base64.getEncoder().encodeToString(texto.getBytes());
    }

    private static String Desencriptar(String textoEncriptado) {
        byte[] decodedBytes = Base64.getDecoder().decode(textoEncriptado);
        return new String(decodedBytes);
    }

    public String getUsuario() {
        return usuario;
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
