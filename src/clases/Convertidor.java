package clases;

import GeneradorSQL.GeneradorPostgreSQL;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSetMetaData;

public class Convertidor {

    private Vector<String> tablas;
    private Fachada op;
    private GeneradorPostgreSQL genPosgres;

    public Convertidor(Vector<String> tablas, Fachada c) {
        this.tablas = tablas;
        this.op = c;
        genPosgres = new GeneradorPostgreSQL();
    }

    public String CrearTabla(String nombre, Vector<String> columnas) {
        String z = "Create Table " + nombre + " (" + System.lineSeparator();
        z += "\t" + columnas.get(0);
        for (int i = 1; i < columnas.size(); i++) {
            z += "," + System.lineSeparator() + "\t" + columnas.get(i);
        }
        z += System.lineSeparator() + ");";
        return z;
    }

    public String consultarPrimarias(String bd) {
        String sql = "select constraint_name, table_name, group_concat(column_name) from ";
        sql += " information_schema.key_column_usage where constraint_schema = '" + bd + "' and ";
        sql += " constraint_name = 'primary' group by table_name ;";
        return sql;
    }

    public String consultarForaneas(String bd) {
        String sql = "select constraint_name, table_name, group_concat(column_name), referenced_table_name, ";
        sql += " group_concat(referenced_column_name) from information_schema.key_column_usage where ";
        sql += "constraint_schema = '" + bd + "' and constraint_name != 'primary' and referenced_table_name ";
        sql += "is not null group by constraint_name ;";
        return sql;
    }
    
    public String generarInsert(String bd) throws SQLException, UnsupportedEncodingException{
        String sql = "", tabla;
        
        for (int j = 0; j < tablas.size(); j++) {
            tabla = tablas.get(j);
            ResultSet res = op.ejecutarConsulta("SELECT * FROM " + tabla);
            ResultSetMetaData rsmd = res.getMetaData();
            Vector<String> col = new Vector<>();
            
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                col.add(rsmd.getColumnName(i));
                
            }

            Vector<Vector<String>> datos = new Vector<>();
            while (res.next()) {
                Vector<String> fila = new Vector<>();
                for (int i = 1; i <= col.size(); i++) {
                    fila.add(res.getString(i));
                }
                datos.add(fila);
            }
            
            sql += genPosgres.agregarMultiplesRegistros(tabla, col, datos) + System.lineSeparator();
        }
        
        return sql;
    }

    public void convertirPostgreSQL(String bd, String ruta, String script) throws IOException, SQLException {
        ruta += script + ".sql";
        BufferedWriter out = new BufferedWriter(new FileWriter(ruta));
        String nombre, n = System.lineSeparator();
        Vector<String> columnas = new Vector<>();

        out.write(genPosgres.BorrarDataBase(bd) + n);
        out.write(genPosgres.CrearDataBase(bd) + n);
        out.write("\\c " + bd + ";" + n + n);//usar base de datos

        for (int i = 0; i < tablas.size(); i++) {
            nombre = tablas.get(i);
            System.out.println("\t tabla: " + nombre);

            ResultSet rs = op.ejecutarConsulta(op.gen.GetColumnasTabla(nombre));
            String tipo, nombreCol, str, def;
            Vector<String> crearColumnas = new Vector<>();
            while (rs.next()) {
                nombreCol = rs.getString("nombre");
                tipo = rs.getString("tipo").toLowerCase();

                if (!tipo.startsWith("varchar")) {
                    int parentesis = tipo.indexOf('(');
                    if (parentesis != -1) {
                        tipo = tipo.substring(0, parentesis);
                    }
                    if (tipo.startsWith("enum")) {
                        tipo = "varchar(3)";
                    }else if(tipo.startsWith("double")){
                        tipo = "DOUBLE PRECISION";
                    }else if(tipo.startsWith("blob") || tipo.startsWith("mediumblob")){
                        tipo = "BYTEA";
                    }else if(tipo.startsWith("datetime")){
                        tipo = "timestamp";
                    }else if(tipo.startsWith("year")){
                        tipo = "smallint";
                    }
                }
                tipo = nombreCol + " " + tipo;

                str = tipo;
                if (rs.getString("nulo").equalsIgnoreCase("NO")) {
                    str += " NOT NULL";
                }
                //System.out.println("....null");
                def = rs.getString("default");
                if (def != null && !def.equalsIgnoreCase("null")) {
                    if (def.equalsIgnoreCase("CURRENT_TIMESTAMP")) {
                        def = "now()";
                    }else{
                        str += " DEFAULT '" + def + "'";
                    }
                }

                crearColumnas.add(str);
                columnas.add(nombreCol);
            }
            out.write(CrearTabla(nombre, crearColumnas) + n + n);
        }

        ResultSet res = op.getConexion().EjecutarConsulta(consultarPrimarias(bd));
        while (res.next()) {
            out.write(op.gen.crearLlavePrimaria(res.getString(2), res.getString(3)) + n);
        }
        out.write(n);
        
        out.write(generarInsert(bd));
        out.write(n);

        ResultSet res2 = op.getConexion().EjecutarConsulta(consultarForaneas(bd));
        while (res2.next()) {
            out.write(genPosgres.crearLlaveForanea(res2.getString(1), res2.getString(2), res2.getString(3),
                    res2.getString(4), res2.getString(5)) + n);
        }
        out.write(n);
        out.close();
    }
}
