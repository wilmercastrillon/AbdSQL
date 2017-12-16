package clases;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class operaciones {

    private Conexion con;
    private String puerto;

    public operaciones() {
    }

    public void setConexion(Conexion con, String puerto) {
        this.puerto = puerto;
        this.con = con;
        this.con.BaseDeDatosSeleccionada = puerto;
    }

    public Conexion getConexion() {
        return con;
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
}
