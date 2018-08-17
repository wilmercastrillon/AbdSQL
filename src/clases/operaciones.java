package clases;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JTable;
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

    public String getUsuario() {
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

    public Vector<DefaultMutableTreeNode> getProcedimientos(String bd) throws SQLException {
        Vector<DefaultMutableTreeNode> proc = new Vector<>();
        ResultSet res = con.getProcedimientos(bd);
        while (res.next()) {
            proc.add(new DefaultMutableTreeNode(res.getString(1)));
        }

        return proc;
    }

    public String getSqlTrigger(String bd, String nombreTrigger) throws SQLException {
        ResultSet res = con.getDatosTrigger(bd, nombreTrigger);
        res.next();
        return res.getString(3);
    }

    public String getSqlProcedimiento(String bd, String nombreP) throws SQLException {
        ResultSet res = con.getDatosProcedimiento(bd, nombreP);
        String sql = "";
        if (con instanceof ConexionMySql) {
            res.next();
            sql += "CREATE PROCEDURE " + nombreP + "(" + res.getString(2) + ")\n";
            sql += res.getString(3);
        } else {
            res.next();
            sql += "CREATE OR REPLACE ";
            sql += res.getString(1);
        }
        return sql;
    }

    public void GuardarProcedimiento(String nombre, String sql, boolean nuevo) throws SQLException {
        if (con instanceof ConexionMySql && !nuevo) {
            con.borrarProcedimiento(nombre);
        }
        con.EjecutarUpdate(sql);
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

    public Vector<String> getDatosColumna(JTable modelo, String Columna) {
        Vector<String> datos = new Vector<>();//nombre, tipo, default y nulo.
        int index = -1;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 0).toString().equals(Columna)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return null;
        }
        datos.add(modelo.getValueAt(index, 0).toString());

        if (con instanceof ConexionMySql) {
            datos.add(modelo.getValueAt(index, 1).toString().toUpperCase());
            Object obj = modelo.getValueAt(index, 4);
            if (obj == null) {
                datos.add(null);
            } else {
                datos.add(obj.toString());
            }
            datos.add(modelo.getValueAt(index, 2).toString());
        } else {//nombre, tipo, default y nulo.
            if (modelo.getValueAt(index, 1).toString().equalsIgnoreCase("NUMBER")) {
                datos.add("INT");
            } else {
                datos.add(modelo.getValueAt(index, 1).toString().toUpperCase());
            }

            Object obj = modelo.getValueAt(index, 4);
            if (obj == null) {
                datos.add(null);
            } else {
                datos.add(obj.toString().replace("'", ""));
            }
            datos.add(modelo.getValueAt(index, 3).toString());
        }
//        System.out.println("datos: " + java.util.Arrays.toString(datos.toArray()));
        return datos;
    }

    public Vector<String> cargarDatosConexion() {
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

    public void guardarDatosConexion(String puerto, String usuario, String sistemaGestor) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\config"));
            bw.write(puerto + "\r\n");
            bw.write(usuario + "\r\n");
            bw.write(sistemaGestor + "\r\n");
            bw.close();
        } catch (Exception e) {
        }
    }

    public boolean generaraMVC(Vector<String> tablas, String ruta) {
        GeneradorMVC gen = new GeneradorMVC(tablas, getConexion());
        try {
            gen.GenerarModelos(ruta);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
