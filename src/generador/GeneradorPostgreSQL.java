package generador;

import java.util.Vector;

public class GeneradorPostgreSQL extends GeneradorSQL {

    @Override
    public String GetDataBases() {
        return "SELECT datname FROM pg_database;"; //" WHERE datistemplate = false;";
    }

    @Override
    public String SelectDataBase(String bd) {
        return "\\c " + bd + ";";
    }

    @Override
    public String CrearDataBase(String nombre) {
        return "CREATE DATABASE " + nombre + ";";
    }

    @Override
    public String BorrarDataBase(String nombre) {
        String z = "DROP DATABASE IF EXISTS " + nombre + ";";
        return z;
    }

    @Override
    public String GetTables() {
        return "select tablename as nombre from pg_catalog.pg_tables where schemaname != "
                + "'information_schema' and schemaname != 'pg_catalog' order by nombre;";
    }

    public String GetTables(String bd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String GetColumnasTabla(String table) {
        String sql = " SELECT DISTINCT attname as nombre, format_type(atttypid, atttypmod) ";
        sql += "as tipo, case when not attnotnull then 'si' else 'no' end as nulo, adsrc as default, coalesce(i.indisprimary,false) ";
        sql += "as llave_primaria, a.attnum as numero FROM pg_attribute a JOIN pg_class pgc ON pgc.oid = ";
        sql += "a.attrelid LEFT JOIN pg_index i ON (pgc.oid = i.indrelid AND i.indkey[0] = a.attnum) ";
        sql += "LEFT JOIN pg_description com on (pgc.oid = com.objoid AND a.attnum = com.objsubid) ";
        sql += "LEFT JOIN pg_attrdef def ON (a.attrelid = def.adrelid AND a.attnum = def.adnum) ";
        sql += "WHERE a.attnum > 0 AND pgc.oid = a.attrelid AND pg_table_is_visible(pgc.oid)AND NOT ";
        sql += "a.attisdropped  AND pgc.relname = '" + table + "' ORDER BY a.attnum; ";
        return sql;
    }

    @Override
    public String CrearTabla(String nombre) {
        String z = "Create Table " + nombre + " (id" + nombre.replace(" ", "") + " int NOT NULL);";
        return z;
    }
    
    @Override
    public String BorrarTabla(String nombre) {
        String z = "DROP TABLE IF EXISTS " + nombre + ";";
        return z;
    }

    @Override
    public String agregarRegistro(String table, String datos[]) {
        StringBuilder str = new StringBuilder("'");
        for (int i = 0; i < datos.length - 1; i++) {
            datos[i] = datos[i].trim();
            str.append(datos[i]).append("','");
        }
        datos[datos.length - 1] = datos[datos.length - 1].trim();
        str.append(datos[datos.length - 1]).append("'");

        String z = "INSERT INTO " + table + " values(" + str.toString() + ");";
        return z;
    }

    @Override
    public String agregarRegistro(String table, Vector<String> columnas, Vector<String> datos) {
        StringBuilder col = new StringBuilder("");
        StringBuilder dat = new StringBuilder("'");
        String z = "";

        try {
            for (int i = 0; i < columnas.size(); i++) {
                if (datos.get(i) != null) {
                    col.append(columnas.get(i));
                    col.append(", ");
                    dat.append(datos.get(i));
                    dat.append("', '");
                }
            }

            z += "INSERT INTO " + table + " ( " + col.substring(0, col.length() - 2);
            z += " ) values(" + dat.substring(0, dat.length() - 3) + ");";
        } catch (Exception e) {
            return null;
        }
        return z;
    }
    
    @Override
    public String agregarMultiplesRegistros(String table, Vector<String> columnas, Vector<Vector<String>> datos) {
        StringBuilder col = new StringBuilder("");
        StringBuilder dat;
        String z = "";

        try {
            for (int i = 0; i < columnas.size(); i++) {
                col.append(columnas.get(i));
                col.append(", ");
            }
            z += "INSERT INTO " + table + " (" + col.substring(0, col.length() - 2) + ") values ";
            
            Vector<String> aux;
            for (int j = 0; j < datos.size(); j++) {
                if (j > 0) {
                    z += ",";
                }
                aux = datos.get(j);
                dat = new StringBuilder("(");
                
                for (int k = 0; k < aux.size(); k++) {
                    if (k > 0) {
                        dat.append(",");
                    }
                    if (aux.get(k) == null) {
                        dat.append("null");
                    }else{
                        if (aux.get(k).startsWith("_binary 0x")) {
                            String s = aux.get(k).replace("_binary 0x", "");
                            dat.append("decode('").append(s).append("', 'hex')");
                        }else{
                            dat.append("'");
                            dat.append(aux.get(k));
                            dat.append("'");
                        }
                    }
                }
                dat.append(")");
                z += dat.toString();
            }
            z += ";";
        } catch (Exception e) {
            return null;
        }
        return z;
    }

    @Override
    public String GetDatosTabla(String table) {
        String z = "SELECT * FROM " + table + ";";
        return z;
    }

    @Override
    public String borrarFila(Vector<String> datos, Vector<String> columas, String table) {
        String z = "DELETE FROM " + table + " WHERE";
        for (int i = 0; i < columas.size(); i++) {
            if (datos.get(i) != null) {
                z += " " + columas.get(i) + " = '" + datos.get(i) + "' AND";
            }
        }
        z = z.substring(0, z.length() - 4) + ";";
        return z;
    }

    public String borrarFila(String dato, String columa, String table) {
        String z = "DELETE FROM " + table + " WHERE " + columa + " = '" + dato + "' ;";
        return z;
    }

    @Override
    public String actualizarFila(String tabla, Vector<String> columnas, Vector<String> datos,
            String columnaCambiar, String datoCambiar) {

        String z = "UPDATE " + tabla + " SET " + columnaCambiar + " = '" + datoCambiar + "' WHERE ";
        for (int i = 0; i < columnas.size(); i++) {
            if (!columnaCambiar.equals(columnas.get(i))) {
                z += columnas.get(i) + " = '" + datos.get(i) + "' AND  ";
            }
        }
        z = z.substring(0, z.length() - 6) + ";";
        return z;
    }

    public String BorrarAllDatosTabla(String tabla) {
        String z = "DELETE FROM " + tabla + ";";
        return z;
    }

    @Override
    public String agregarColumnaTabla(String tabla, String tipo, String nombre, String longitud,
            String Default, boolean Nonulo) {

        String z = "ALTER TABLE " + tabla + " ADD(" + nombre + " " + tipo;
        if (longitud != null) {
            z += "(" + longitud + ")";
        }
        if (Default != null) {
            z += " DEFAULT '" + Default + "'";
        }
        if (Nonulo) {
            z += " NOT NULL";
        }
        z += ");";
        return z;
    }

    @Override
    public String borrarColumnaTabla(String tabla, String columna) {
        String z = "ALTER TABLE " + tabla + " DROP " + columna + ";";
        return z;
    }

    @Override
    public String crearLlavePrimaria(String tabla, String columna) {
        String z = "ALTER TABLE " + tabla + " ADD PRIMARY KEY (" + columna + ");";
        return z;
    }
    
    @Override
    public String consultarLlavesPrimarias(String bd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String crearLlaveForanea(String tabla, String atri, String tabla_ref, String atri_ref) {
        String z = "ALTER TABLE " + tabla + " ADD FOREIGN KEY(" + atri
                + ") REFERENCES " + tabla_ref + "(" + atri_ref + ");";
        return z;
    }
    
    @Override
    public String crearLlaveForanea(String nombreConstraint, String tabla, String atri, String tabla_ref, String atri_ref) {
        String z = "ALTER TABLE " + tabla + " ADD CONSTRAINT " + nombreConstraint + " FOREIGN KEY(" + atri
                + ") REFERENCES " + tabla_ref + "(" + atri_ref + ");";
        return z;
    }
    
    @Override
    public String consultarLlavesForaneas(String bd){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTriggers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDatosTrigger(String BD, String nombreTrigger) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String crearTrigger(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String borrarTrigger(String nombreTrigger) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String crearLlaveUnique(String tabla, String columna) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String actualizarAtributo(String tabla, String nombre, String tipo, String Nuevonombre, String longitud, String Default, boolean Nonulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String renombrarTabla(String tabla, String nuevoNombre) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String CrearAuto_increment(String tabla, String columna, String tipo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getProcedimientos(String BD) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDatosProcedimiento(String BD, String nombreP) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String crearProcedimiento(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String borrarProcedimiento(String nombreP) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String LlamarProcedimiento(String procedimiento, String parametros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
