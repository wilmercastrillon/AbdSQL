package clases;

import GeneradorSQL.GeneradorSQL;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class ExportadorSQL {

    private final Fachada op;
    private final GeneradorSQL gen;
    private final int tipoFinal;
    private final String bd;
    private final String ls = System.lineSeparator();
    private Vector<Vector<String>> tipos = new Vector<>();

    public ExportadorSQL(Fachada conexionInicial, int tipoSGBD) {
        op = conexionInicial;
        bd = op.getBDseleccionada();
        gen = op.nuevoGeneradorSQL(tipoSGBD);
        tipos = op.cargarTipos();
        tipoFinal = tipoSGBD;
    }

    public void exportar(String bd, String ruta, String nombreScript) throws IOException, SQLException {
        ruta += nombreScript + ".sql";
        BufferedWriter out = new BufferedWriter(new FileWriter(ruta));
        
        //se recomienda tener codificacion utf8
        out.write(crearBD() + ls);
        if (tipoFinal == op.getConexion().PostgreSQL) {
             out.write("SET client_encoding = 'utf8';" + ls + ls);
        }
        out.write(crearTablas() + ls);
        out.write(crearLlavesPrimarias() + ls);
        System.out.println("creo primarias");
        out.write(generarInsert(bd) + ls);
        System.out.println("creo insert");
        out.write(crearLlavesForaneas());
        System.out.println("creo foraneas");
        out.close();
    }

    public String crearBD() {
        String s = gen.BorrarDataBase(bd) + ls + gen.CrearDataBase(bd) + ls;
        s += gen.SelectDataBase(bd) + ls;
        return s;
    }

    private String convertirTipo(String tipo) {//<n> longitud, <a> nombre de atributo, <t> nombre de la tabla
        tipo = tipo.toUpperCase();
        String aux;
        for (int i = 0; i < tipos.size(); i++) {
            aux = tipos.get(i).get(0).toUpperCase();
            //System.out.println("compara con " + aux);
            if (aux.startsWith(tipo)) {
                return tipos.get(i).get(tipoFinal);
            }
        }
        return tipo;
    }

    public String crearSqlTabla(String nombre, Vector<String> columnas) {
        String z = "Create Table " + nombre + " (" + System.lineSeparator();
        z += "\t" + columnas.get(0);
        for (int i = 1; i < columnas.size(); i++) {
            z += "," + System.lineSeparator() + "\t" + columnas.get(i);
        }
        z += System.lineSeparator() + ");";
        return z;
    }

    public String crearTablas() throws SQLException {
        Vector<String> tablas = op.getTablesDataBase(bd);
        String nombreTabla;
        StringBuilder sql = new StringBuilder("");

        for (int i = 0; i < tablas.size(); i++) {
            nombreTabla = tablas.get(i);
            ResultSet rs = op.ejecutarConsulta(op.gen.GetColumnasTabla(nombreTabla));//columnas
            String tipo, longitud, nombreCol, str, def;
            Vector<String> crearColumnas = new Vector<>();

            while (rs.next()) {
                nombreCol = rs.getString("nombre");
                tipo = rs.getString("tipo");

                int p = tipo.indexOf("(");
                if (p != -1) {
                    longitud = tipo.substring(p, tipo.length());
                    tipo = tipo.substring(0, p);
                } else {
                    longitud = "";
                }
                tipo = convertirTipo(tipo);
                tipo = tipo.replace("(<n>)", longitud);
                tipo = tipo.replace("(<t>)", nombreTabla);
                tipo = tipo.replace("(<a>)", nombreCol);
                tipo = nombreCol + " " + tipo;

                str = tipo;
                if (rs.getString("nulo").equalsIgnoreCase("NO")) {
                    str += " NOT NULL";
                }
                //System.out.println("....null");
                def = rs.getString("default");
                if (def != null && !def.equalsIgnoreCase("null")) {
                    if (def.equalsIgnoreCase("CURRENT_TIMESTAMP")) {
                        str += " DEFAULT CURRENT_TIMESTAMP";
                    } else {
                        str += " DEFAULT '" + def + "'";
                    }
                }

                crearColumnas.add(str);
            }
            sql.append(crearSqlTabla(nombreTabla, crearColumnas)).append(ls).append(ls);
        }
        return sql.toString();
    }

    public String crearLlavesPrimarias() throws SQLException {
        ResultSet res = op.ejecutarConsulta(op.gen.consultarLlavesPrimarias(bd));
        StringBuilder sql = new StringBuilder();
        while (res.next()) {
            sql.append(gen.crearLlavePrimaria(res.getString("tabla"), res.getString("columnas"))).append(ls);
        }
        return sql.toString();
    }

    public String crearLlavesForaneas() throws SQLException {
        ResultSet res = op.ejecutarConsulta(op.gen.consultarLlavesForaneas(bd));
        StringBuilder sql = new StringBuilder();
        while (res.next()) {
            sql.append(gen.crearLlaveForanea(res.getString("nombre"), res.getString("tabla"),
                    res.getString("columnas"), res.getString("tabla_referencia"), res.getString("columnas_referencia")
            )).append(ls);
        }
        return sql.toString();
    }

    public static String ByteArrayToString(byte[] bytes) {
        return javax.xml.bind.DatatypeConverter.printHexBinary(bytes);
    }

    public String generarInsert(String bd) throws SQLException, UnsupportedEncodingException {
        Vector<String> tablas = op.getTablesDataBase(bd);
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
                    if (rsmd.getColumnClassName(i).equalsIgnoreCase("[B")) {
                        fila.add("_binary 0x" + ByteArrayToString(res.getBytes(i)));
                    } else {
                        fila.add(res.getString(i));
                    }
                }
                datos.add(fila);
            }
            if (datos.isEmpty()) {
                continue;
            }
            
            sql += gen.agregarMultiplesRegistros(tabla, col, datos) + System.lineSeparator();
        }

        return sql;
    }
}
