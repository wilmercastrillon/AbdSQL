package conexionBD;

import static conexionBD.Conexion.MySQL;
import static conexionBD.Conexion.Oracle;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;

public class ConexionPostgreSQL extends Conexion{
    
    public ConexionPostgreSQL() {
        super(Conexion.PostgreSQL);
    }
    
    public boolean conectar(String host, String puerto, String user, String password, String bd) {
        this.host = host;
        usuario = user;
        String url = "jdbc:postgresql://" + host + ":" + puerto + "/" + bd;
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
}
