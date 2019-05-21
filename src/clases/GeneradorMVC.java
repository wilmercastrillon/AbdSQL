package clases;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javafx.util.Pair;

public class GeneradorMVC {

    private Vector<String> tablas;
    private Fachada op;

    public GeneradorMVC(Vector<String> tablas, Fachada c) {
        this.tablas = tablas;
        this.op = c;
    }

    public void GenerarModelos(String ruta) throws IOException, SQLException {
        BufferedWriter out, outCon;
        String nombre, n = System.lineSeparator();
        Vector<Pair<String, String>> columnas = new Vector<>();

        for (int i = 0; i < tablas.size(); i++) {
            StringBuilder select = new StringBuilder("");
            StringBuilder insert = new StringBuilder("");
            StringBuilder update = new StringBuilder("");
            nombre = tablas.get(i);

            select.append("\t\tpublic DataTable get").append(nombre).append("(){").append(n);
            select.append("\t\t\tstring sql = \"SELECT * FROM ").append(nombre).append("; \";").append(n);
            select.append("\t\t\treturn Connection.EjecutarConsulta(sql, System.Data.CommandType.Text);").append(n);
            select.append("\t\t}").append(n).append(n);
            insert.append("\t\tpublic bool insert").append(nombre).append("(){").append(n);
            insert.append("\t\t\tstring[] sql = new string[1];").append(n);
            insert.append("\t\t\tsql[0] = \"INSERT INTO ").append(nombre).append("() VALUES() ; \";").append(n);
            insert.append("\t\t\treturn Connection.RealizarTransaccion(sql);").append(n);
            insert.append("\t\t}").append(n).append(n);
            update.append("\t\tpublic bool update").append(nombre).append("(){").append(n);
            update.append("\t\t\tstring[] sql = new string[1];").append(n);
            update.append("\t\t\tsql[0] = \"UPDATE ").append(nombre).append(" SET var=var WHERE id=var ; \";").append(n);
            update.append("\t\t\treturn Connection.RealizarTransaccion(sql);").append(n);
            update.append("\t\t}").append(n).append(n);

            columnas.clear();
            out = new BufferedWriter(new FileWriter(ruta + "\\Models\\" + nombre + "_model.cs"));
            out.write("using System;" + n
                    + "using System.Collections.Generic;" + n
                    + "using System.Data;" + n
                    + "using proyecto.BD; //Ubicacion de la clase conexion a a base de datos" + n + n
                    + "namespace Models {" + n + n
                    + "\tpublic class " + nombre + "_model" + " {" + n + n);

            outCon = new BufferedWriter(new FileWriter(ruta + "\\Controllers\\" + nombre + "_controller.cs"));
            outCon.write("using System;" + n
                    + "using System.Collections.Generic;" + n
                    + "using System.Data;" + n
                    + "using Models;" + n + n
                    + "namespace Controllers {" + n + n
                    + "\tpublic class " + nombre + "_controller" + " {" + n + n);

            ResultSet rs = op.ejecutarConsulta(op.gen.GetColumnasTabla(tablas.get(i)));
            String value;
            while (rs.next()) {
                value = rs.getString(2).toLowerCase();
                if (value.startsWith("int")) {
                    value = "int ";
                } else if (value.startsWith("double") || value.startsWith("float")) {
                    value = "double ";
                } else if (value.startsWith("char")) {
                    value = "char ";
                } else if (value.startsWith("binary")) {
                    value = "bool ";
                } else {
                    value = "string ";
                }
                columnas.add(new Pair<>(rs.getString(1), value));
            }

            StringBuilder variables = new StringBuilder("");
            StringBuilder constructorVar = new StringBuilder("");
            StringBuilder constructorCon = new StringBuilder("");
            StringBuilder constructorVarCon = new StringBuilder("");
            StringBuilder constructorConCon = new StringBuilder("");

            constructorVar.append("\t\tpublic ").append(nombre).append("_model (");
            constructorVarCon.append("\t\tpublic ").append(nombre).append("_controller (");
            constructorConCon.append("\t\t\tthis.").append(nombre).append(" = new ");
            constructorConCon.append(nombre).append("_model(");
            for (Pair<String, String> par : columnas) {
                variables.append("\t\tpublic ").append(par.getValue()).append(" ");
                variables.append(par.getKey()).append(" { get; set; }").append(n);
                constructorVar.append(par.getValue()).append(" ").append(par.getKey());
                constructorVarCon.append(par.getValue()).append(" ").append(par.getKey());
                constructorConCon.append(par.getKey());
                if(!par.getKey().equals(columnas.get(columnas.size()-1).getKey())){
                    constructorVarCon.append(", ");
                    constructorVar.append(", ");
                    constructorConCon.append(", ");
                }
                constructorCon.append("\t\t\tthis.").append(par.getKey()).append(" = ").append(par.getKey());
                constructorCon.append(";").append(n);
            }
            variables.append("\t\tConexion Connection = new Conexion();").append(n).append(n);
            variables.append("\t\tpublic ").append(nombre).append("_model (){}").append(n).append(n);
            constructorVar.append("){").append(n);
            constructorVarCon.append("){").append(n);
            constructorConCon.append(");").append(n).append("\t\t}").append(n).append(n);
            constructorCon.append("\t\t}").append(n);

            out.write(variables.toString());
            out.write(constructorVar.toString());
            out.write(constructorCon.toString());
            out.write(n);
            out.write(n);
            out.write(select.toString());
            out.write(insert.toString());
            out.write(update.toString());

            outCon.write("\t\tprivate " + nombre + "_model " + nombre + " = new " + nombre + "_model();" + n + n);
            outCon.write("\t\tpublic " + nombre + "_controller (){}" + n + n);
            outCon.write(constructorVarCon.toString());
            outCon.write(constructorConCon.toString());
            outCon.write("\t\tpublic DataTable get" + nombre + "(){" + n
                    + "\t\t\treturn " + nombre + ".get" + nombre + "();" + n + "\t\t}" + n + n);
            outCon.write("\t\tpublic bool insert" + nombre + "(){" + n
                    + "\t\t\treturn " + nombre + ".insert" + nombre + "();" + n + "\t\t}" + n + n);
            outCon.write("\t\tpublic bool update" + nombre + "(){" + n
                    + "\t\t\treturn " + nombre + ".update" + nombre + "();" + n + "\t\t}" + n);

            out.write("\t}" + n + "}" + n);
            out.close();
            outCon.write("\t}" + n + "}" + n);
            outCon.close();
        }
    }
}
