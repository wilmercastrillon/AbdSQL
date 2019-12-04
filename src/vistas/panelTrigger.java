package vistas;

import clases.Fachada;
import java.awt.Cursor;
import java.awt.LayoutManager;
import java.sql.SQLException;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class panelTrigger extends javax.swing.JPanel {

    private final Fachada op;
    public final String nombre, BaseDeDatos;
    public boolean nuevo;
    private RSyntaxTextArea textTrigger;

    public panelTrigger(Fachada op, String BD, String nombre, boolean nuevo) {
        this.op = op;
        this.nombre = nombre;
        this.nuevo = nuevo;
        BaseDeDatos = BD;
        initComponents();
        cargarTextArea();
        cargar();
        setName(nombre);
    }

    private void cargarTextArea() {
        textTrigger = new RSyntaxTextArea();
        textTrigger.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        textTrigger.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(textTrigger);
        panelTrigger.add(sp);
        panelTrigger.setLayout((LayoutManager) new BoxLayout(panelTrigger, BoxLayout.Y_AXIS));
        panelTrigger.add(sp);

        CompletionProvider provider = createCompletionProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(textTrigger);
    }

    private CompletionProvider createCompletionProvider() {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        provider.addCompletion(new BasicCompletion(provider, "select"));
        provider.addCompletion(new BasicCompletion(provider, "assert"));
        provider.addCompletion(new BasicCompletion(provider, "break"));
        provider.addCompletion(new BasicCompletion(provider, "case"));
        // ... etc ...
        provider.addCompletion(new BasicCompletion(provider, "transient"));
        provider.addCompletion(new BasicCompletion(provider, "try"));
        provider.addCompletion(new BasicCompletion(provider, "void"));
        provider.addCompletion(new BasicCompletion(provider, "volatile"));
        provider.addCompletion(new BasicCompletion(provider, "while"));

        // Add a couple of "shorthand" completions. These completions don't
        // require the input text to be the same thing as the replacement text.
        provider.addCompletion(new ShorthandCompletion(provider, "sysout",
                "System.out.println(", "System.out.println("));
        provider.addCompletion(new ShorthandCompletion(provider, "syserr",
                "System.err.println(", "System.err.println("));
        return provider;
    }

    private void cargar() {
        try {
            if (nuevo) {
                String sql = "CREATE ";
                if (op.esConexionMysql()) {
                    sql += "DEFINER=`" + op.getUsuario() + "`@`%`";
                } else {
                    sql += "OR REPLACE";
                }
                sql += " TRIGGER " + nombre + "\n"
                        + "AFTER INSERT ON nombre_tabla\n"
                        + "FOR EACH ROW \n"
                        + "BEGIN\n"
                        + "END";
                textTrigger.setText(sql);
            } else {
                textTrigger.setText(op.getSqlTrigger(BaseDeDatos, nombre));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar trigger", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        botonGuardar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        panelTrigger = new javax.swing.JPanel();

        botonGuardar.setText("Guardar");
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });

        jLabel4.setText("Sql:");

        jLabel1.setText("En desarrollo...");

        javax.swing.GroupLayout panelTriggerLayout = new javax.swing.GroupLayout(panelTrigger);
        panelTrigger.setLayout(panelTriggerLayout);
        panelTriggerLayout.setHorizontalGroup(
            panelTriggerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelTriggerLayout.setVerticalGroup(
            panelTriggerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 280, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTrigger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botonGuardar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 338, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(panelTrigger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(botonGuardar)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarActionPerformed
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            if (!BaseDeDatos.equals(op.getBDseleccionada())) {
                op.seleccionarBD(BaseDeDatos);
            }
            if (nuevo) {
                op.ejecutarUpdate(op.getGeneradorSQL().crearTrigger(textTrigger.getText()));
                nuevo = false;
            } else {
                op.ejecutarUpdate(op.getGeneradorSQL().borrarTrigger(nombre));
                op.ejecutarUpdate(op.getGeneradorSQL().crearTrigger(textTrigger.getText()));
            }
            JOptionPane.showMessageDialog(null, "Operacion exitosa", "Exitoso", 1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al crear trigger", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_botonGuardarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel panelTrigger;
    // End of variables declaration//GEN-END:variables
}
