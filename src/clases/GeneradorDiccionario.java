package clases;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public class GeneradorDiccionario {

    private final Fachada op;
    private final String bd;
    private final String ls = System.lineSeparator();

    public GeneradorDiccionario(Fachada op, String bd) {
        this.op = op;
        this.bd = bd;
    }

    public void crearWord(String ruta, String nombre) throws FileNotFoundException, SQLException, IOException {
        XWPFDocument documento = new XWPFDocument();

        Vector<String> tablasBD = op.getTablesDataBase(bd);
        String nombreTabla;
        for (int i = 0; i < tablasBD.size(); i++) {
            nombreTabla = tablasBD.get(i);
            System.out.println("generador diccionario: tabla " + nombreTabla);

            XWPFParagraph parrafo = documento.createParagraph();
            XWPFRun run = parrafo.createRun();
            run.addBreak();
            run.setText("Tabla: " + nombreTabla);

            XWPFTable tabla = documento.createTable();

            CTTbl table = tabla.getCTTbl();
            CTTblPr pr = table.getTblPr();
            CTTblWidth tblW = pr.getTblW();
            tblW.setW(BigInteger.valueOf(5000));
            tblW.setType(STTblWidth.PCT);
            pr.setTblW(tblW);
            table.setTblPr(pr);

            XWPFTableRow fila0 = tabla.getRow(0);
            fila0.getCell(0).setText("NOMBRE");
            fila0.createCell().setText("TIPO");
            fila0.createCell().setText("NULO");
            fila0.createCell().setText("DEFAULT");
            fila0.createCell().setText("LLAVE");
            fila0.createCell().setText("COMENTARIOS");

            fila0.getCell(0).setColor("C0C0C0");
            fila0.getCell(1).setColor("C0C0C0");
            fila0.getCell(2).setColor("C0C0C0");
            fila0.getCell(3).setColor("C0C0C0");
            fila0.getCell(4).setColor("C0C0C0");
            fila0.getCell(5).setColor("C0C0C0");

            System.out.println("generador diccionario: creo el encabezado ");

            ResultSet res = op.ejecutarConsulta(op.gen.getColumnsTable(nombreTabla));
            String nombreCol, tipoCol, nuloCol, defaultCol;
            while (res.next()) {//[nombre, tipo, nulo, defecto]
                nombreCol = res.getString(1);
                tipoCol = res.getString(2).toUpperCase();
                nuloCol = res.getString(3).toUpperCase();
                defaultCol = res.getString(4);
                System.out.println("generador diccionario: atributo " + nombreCol);

                XWPFTableRow fila = tabla.createRow();
                fila.getCell(0).setText(nombreCol);
                fila.getCell(1).setText(tipoCol);
                fila.getCell(2).setText(nuloCol);
                fila.getCell(3).setText(defaultCol);
                fila.getCell(4).setText(res.getString(5).toUpperCase());
            }
            //run.addBreak();
        }

        FileOutputStream out = new FileOutputStream(ruta + "\\" + nombre);
        documento.write(out);
        out.close();
        System.out.println("generador diccionario: termino en direccion: " + ruta + "\\" + nombre);
    }
}
