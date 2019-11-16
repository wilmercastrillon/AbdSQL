package GeneradorSQL;

import java.util.Vector;

/**
 *
 * @author wilmer
 */
public abstract class GeneradorSQL {
    
    public GeneradorSQL(){
    }

    abstract public String GetDataBases();

    abstract public String SelectDataBase(String bd);

    abstract public String CrearDataBase(String nombre);
    
    abstract public String BorrarDataBase(String bd);

    abstract public String GetTables();//[nombre]
    
    abstract public String GetTables(String bd);

    abstract public String GetColumnasTabla(String table);//[nombre, tipo, nulo, defecto,.........]

    abstract public String CrearTabla(String nombre);

    abstract public String BorrarTabla(String nombre);

    abstract public String agregarRegistro(String table, String datos[]);

    abstract public String agregarRegistro(String table, Vector<String> columnas, Vector<String> datos);
    
    abstract public String agregarMultiplesRegistros(String table, Vector<String> columnas, Vector<Vector<String>> datos);

    abstract public String GetDatosTabla(String table);

    abstract public String borrarFila(Vector<String> datos, Vector<String> columas, String table);

    abstract public String actualizarFila(String tabla, Vector<String> columnas, Vector<String> datos,
            String columnaCambiar, String datoCambiar);

    abstract public String agregarColumnaTabla(String tabla, String tipo, String nombre, String longitud,
            String Default, boolean Nonulo);

    abstract public String borrarColumnaTabla(String tabla, String columna);

    abstract public String crearLlavePrimaria(String tabla, String columna);

    abstract public String crearLlaveForanea(String tabla, String atri, String tabla_ref, String atri_ref);
    
    abstract public String consultarLlavesPrimarias(String bd);//[nombre, tabla, columnas]
    
    abstract public String consultarLlavesForaneas(String bd);//[nombre, tabla, columnas, tabla_referencia, columnas_referencia]
    
    abstract public String crearLlaveForanea(String nombreConstraint, String tabla, String atri, 
            String tabla_ref, String atri_ref);

    abstract public String getTriggers();

    abstract public String getDatosTrigger(String BD, String nombreTrigger);

    abstract public String crearTrigger(String sql);

    abstract public String borrarTrigger(String nombreTrigger);

    abstract public String crearLlaveUnique(String tabla, String columna);

    abstract public String actualizarAtributo(String tabla, String nombre, String tipo, String Nuevonombre,
            String longitud, String Default, boolean Nonulo);

    abstract public String renombrarTabla(String tabla, String nuevoNombre);

    abstract public String CrearAuto_increment(String tabla, String columna, String tipo);

    abstract public String getProcedimientos(String BD);

    abstract public String getDatosProcedimiento(String BD, String nombreP);

    abstract public String crearProcedimiento(String sql);

    abstract public String borrarProcedimiento(String nombreP);
    
    abstract public String LlamarProcedimiento(String procedimiento, String parametros);
    
    //[constraint][tabla][columna][tabla_referencia][columna_referencia]
    abstract public String getForaneasTabla(String BD, String tabla);
    
    //[constraint][tabla][columna][tabla_referencia][columna_referencia]
    abstract public String getForanea(String BD, String tabla, String constraint);
    
    abstract public String borrarForanea(String tabla, String constraint);
    
    //[tabla][indice]
    abstract public String getIndicesTabla(String BD, String tabla);
    
    abstract public String borrarIndex(String tabla, String constraint);
}
