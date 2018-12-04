package clases;

import generador.GeneradorSQL;
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
import vistas.consola;
import conexionBD.Conexion;
import generador.GeneradorMySQL;
import generador.GeneradorOracle;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class operaciones {

    private Conexion con;
    private consola cons;
    private String puerto, usuario;
    private String BDseleccionada;
    public GeneradorSQL gen;

    public operaciones() {
    }

    public operaciones(int tipo) {
        con = new Conexion(tipo);
    }

    public void setTipoConexion(int tipo) {
        con = new Conexion(tipo);
    }

    public void setConexion(String puerto, String user) {
        BDseleccionada = puerto;
        cons = new consola(this);
        this.puerto = puerto;
        if (con.tipo == Conexion.MySQL) {
            gen = new GeneradorMySQL();
        } else {
            gen = new GeneradorOracle();
        }
        usuario = user;
    }

    public void seleccionarBD(String bd) throws SQLException {
        if (con.tipo == Conexion.MySQL) {
            ejecutarUpdate(gen.SelectDataBase(bd));
            BDseleccionada = bd;
        }
    }

    public String getBDseleccionada() {
        return BDseleccionada;
    }

    public Conexion getConexion() {
        return con;
    }

    public GeneradorSQL getGeneradorSQL() {
        return gen;
    }

    public String getUsuario() {
        return usuario;
    }

    public void mostrarConsola() {
        cons.setVisible(true);
    }

    public String inputContraseña(String mensaje, String titulo) {
        String password = "";
        JPasswordField passwordField = new JPasswordField();
        Object[] obj = {mensaje + "\n\n", passwordField};
        Object stringArray[] = {"OK", "Cancel"};
        if (JOptionPane.showOptionDialog(null, obj, titulo,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, stringArray, obj) == JOptionPane.YES_OPTION) {
            password = passwordField.getText();
        }
        return password;
    }

    public boolean recuperarConexion() {
        JOptionPane.showMessageDialog(null, "Conexion perdida!!!", "Error", 0);
        String pass = inputContraseña(puerto + "\nUsuario: " + usuario + "\nIngrese contraseña: ", "Recuperar Conexión");

        try {
            if (con.conectar(puerto, usuario, pass)) {
                seleccionarBD(BDseleccionada);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Error al reconectar", "Error", 0);
            }
        } catch (SQLException ex) {
            return false;
        }
        return false;
    }

    public ResultSet ejecutarConsulta(String sql) throws SQLException {
        if (con.conexionCerrada()) {
            recuperarConexion();
        }
        ResultSet res = null;
        try {
            cons.agregar(sql);
            res = con.EjecutarConsulta(sql);
        } catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException ex) {
            if (recuperarConexion()) {
                ejecutarConsulta(sql);
            }
        }
        return res;
    }

    public void ejecutarUpdate(String sql) throws SQLException {
        if (con.conexionCerrada()) {
            recuperarConexion();
        }
        try {
            cons.agregar(sql);
            con.EjecutarUpdate(sql);
        } catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException ex) {
            recuperarConexion();
        }
    }

    public Vector<String> getTablesDataBase(String dataBase) throws SQLException {
        Vector<String> aux = new Vector<>();
        ejecutarUpdate(gen.SelectDataBase(dataBase));
        ResultSet res2 = ejecutarConsulta(gen.GetTables());
        while (res2.next()) {
            String h2 = res2.getString("Tables_in_" + dataBase);
            aux.add(h2);
        }
        return aux;
    }

    public boolean esConexionMysql() {
        return con.tipo == con.MySQL;
    }

    public void actualizarAtributoMysql(String tabla, String nombre, String tipo, String Nuevonombre,
            String longitud, String Default, boolean Nonulo, boolean primera, String despuesDe) throws SQLException {
        GeneradorMySQL cm = (GeneradorMySQL) gen;
        ejecutarUpdate(cm.actualizarAtributo(tabla, nombre, tipo, Nuevonombre, longitud, Default, Nonulo, primera, despuesDe));
    }

    public Vector<String> getBasesDeDatos() throws SQLException {
        Vector<String> aux = new Vector<>();
        if (esConexionMysql()) {
            ResultSet res2 = ejecutarConsulta(gen.GetDataBases());
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
        ResultSet res = ejecutarConsulta(gen.getTriggers());

        while (res.next()) {
            triggers.add(new DefaultMutableTreeNode(res.getString(1)));
        }

        return triggers;
    }

    public Vector<DefaultMutableTreeNode> getProcedimientos(String bd) throws SQLException {
        Vector<DefaultMutableTreeNode> proc = new Vector<>();
        ResultSet res = ejecutarConsulta(gen.getProcedimientos(bd));
        while (res.next()) {
            proc.add(new DefaultMutableTreeNode(res.getString(1)));
        }

        return proc;
    }

    public String getSqlTrigger(String bd, String nombreTrigger) throws SQLException {
        ResultSet res = ejecutarConsulta(gen.getDatosTrigger(bd, nombreTrigger));
        res.next();
        return res.getString(3);
    }

    public String getSqlProcedimiento(String bd, String nombreP) throws SQLException {
        String sql = "";
        if (esConexionMysql()) {
            GeneradorMySQL q = (GeneradorMySQL) gen;
            ResultSet p = ejecutarConsulta(q.getParametrosProcedimiento(nombreP));
            String parametros = "";
            while (p.next()) {            
                parametros += ", " + p.getString(1);
                parametros += " " + p.getString(2);
            }
            
            ResultSet res = ejecutarConsulta(gen.getDatosProcedimiento(bd, nombreP));
            res.next();
            sql += "CREATE PROCEDURE " + nombreP + "(" + parametros.substring(Math.min(1, parametros.length())) + ")\n";
            sql += res.getString("ROUTINE_DEFINITION");
        } else {
            ResultSet res = ejecutarConsulta(gen.getDatosProcedimiento(bd, nombreP));
            res.next();
            sql += "CREATE OR REPLACE ";
            sql += res.getString(1);
        }
        return sql;
    }

    public void GuardarProcedimiento(String nombre, String sql, boolean nuevo) throws SQLException {
        if (esConexionMysql() && !nuevo) {
            ejecutarUpdate(gen.borrarProcedimiento(nombre));
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
        Vector<String> datos = new Vector<>();//nombre, tipo, default, nulo y longitud.
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

        if (esConexionMysql()) {
            String tipo = modelo.getValueAt(index, 1).toString().toUpperCase();
            int parentesis = tipo.indexOf("(");
            if (parentesis == -1) {
                datos.add(tipo);
            } else {
                datos.add(tipo.substring(0, parentesis));
            }
            Object obj = modelo.getValueAt(index, 4);
            if (obj == null) {
                datos.add(null);
            } else {
                datos.add(obj.toString());
            }
            datos.add(modelo.getValueAt(index, 2).toString());
            if (parentesis == -1) {
                datos.add("");
            } else {
                datos.add(tipo.substring(parentesis + 1, tipo.length() - 1));
            }
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
            datos.add("");
        }
        return datos;
    }

    public Vector<String> cargarDatosConexion() {
        Vector<String> vs = new Vector<>();
        try {
            System.out.println(System.getProperty("user.dir") + "\\config");
            BufferedReader tec = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\config"));
            while (tec.ready()) {
                String s = tec.readLine();
                s += " " + tec.readLine();
                s += " " + tec.readLine();
                vs.add(s);
            }
        } catch (Exception e) {
            if (vs.isEmpty()) {
                return null;
            }
        }
        return vs;
    }

    public void guardarDatosConexion(Vector<String> vs) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\config"));
            String s[];
            for (int i = 0; i < vs.size(); i++) {
                s = vs.get(i).split(" ");
                bw.write(s[0] + "\r\n");
                bw.write(s[1] + "\r\n");
                bw.write(s[2] + "\r\n");
            }
            bw.close();
        } catch (Exception e) {
        }
    }

    public boolean generaraMVC(Vector<String> tablas, String ruta) {
        GeneradorMVC gen = new GeneradorMVC(tablas, this);
        try {
            gen.GenerarModelos(ruta);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
