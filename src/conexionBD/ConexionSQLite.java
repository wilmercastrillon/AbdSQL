package conexionBD;

import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionSQLite extends Conexion{
    
    public ConexionSQLite() {
        super(Conexion.Sqlite);
    }
    
    public boolean conectar(String archivo) {
        this.host = "localhost";
        char separador = archivo.lastIndexOf('/') >= 0? '/' : '\\';
        usuario = archivo.substring(archivo.lastIndexOf(separador));
        String url = "jdbc:sqlite:" + archivo;
        con = null;
        
        try {
            con = DriverManager.getConnection(url);
            sta = con.createStatement();
            if (con != null) {
                System.out.println("Conexion exitosa\n");
            } else {
                System.out.println("no se ha conectado\n");
                return false;
            }
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
}
