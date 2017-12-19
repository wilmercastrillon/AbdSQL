package clases;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class operaciones {

    private Conexion con;
    private String puerto;
    private String usuario;

    public operaciones() {
    }

    public void setConexion(Conexion con, String puerto, String user) {
        this.puerto = puerto;
        this.con = con;
        this.con.BaseDeDatosSeleccionada = puerto;
        usuario = user;
    }

    public Conexion getConexion() {
        return con;
    }
    
    public String getUsuario(){
        return usuario;
    }

    public void mostrarConsola() {
        con.cons.setVisible(true);
    }

    public Vector<String> getTablesDataBase(String dataBase) throws SQLException {
        Vector<String> aux = new Vector<>();
        con.SelectDataBase(dataBase);
        ResultSet res2 = con.GetTables();
        while (res2.next()) {
            String h2 = res2.getString("Tables_in_" + dataBase);
            aux.add(h2);
        }
        return aux;
    }

    public Vector<String> getBasesDeDatos() throws SQLException {
        Vector<String> aux = new Vector<>();
        if (con instanceof ConexionMySql) {
            ResultSet res2 = con.GetDataBases();
            while (res2.next()) {
                String h2 = res2.getString(1);
                aux.add(h2);
            }
        } else {
            aux.add(puerto);
        }
        return aux;
    }

    public Vector<DefaultMutableTreeNode> getTriggers() throws SQLException {
        Vector<DefaultMutableTreeNode> triggers = new Vector<>();
        ResultSet res = con.getTriggers();
        
        while (res.next()) {
            triggers.add(new DefaultMutableTreeNode(res.getString(1)));
        }

        return triggers;
    }
    
    public String getSqlTrigger(String bd, String nombreTrigger) throws SQLException{
        ResultSet res = con.getDatosTrigger(bd, nombreTrigger);
        res.next();
        return res.getString(3);
    }

    public void llenarTableModel(ResultSet res, DefaultTableModel modeloJtable) throws SQLException {
        ResultSetMetaData rsmd = res.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            modeloJtable.addColumn(rsmd.getColumnName(i));
        }

        while (res.next()) {
            Vector<String> datos = new Vector<>();
            for (int i = 1; i <= modeloJtable.getColumnCount(); i++) {
                datos.add(res.getString(i));
            }
            modeloJtable.addRow(datos);
        }
    }
    
    public Vector<String> cargarDatosConexion(){
        Vector<String> vs = new Vector<>();
        try {
            System.out.println(System.getProperty("user.dir") + "\\config");
            BufferedReader tec = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\config"));
            vs.add(tec.readLine());
            vs.add(tec.readLine());
            vs.add(tec.readLine());
        } catch (Exception e) {
            return null;
        }
        return vs;
    }
    
    public void guardarDatosConexion(String puerto, String usuario, String sistemaGestor){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\config"));
            bw.write(puerto + "\r\n");
            bw.write(usuario + "\r\n");
            bw.write(sistemaGestor + "\r\n");
            bw.close();
        } catch (Exception e) {
        }
    }
}
