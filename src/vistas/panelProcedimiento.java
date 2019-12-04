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

public class panelProcedimiento extends javax.swing.JPanel {

    private final Fachada op;
    public final String nombre, BaseDeDatos;
    public boolean nuevo;
    private RSyntaxTextArea textProcedimiento;

    public panelProcedimiento(Fachada op, String BD, String nombre, boolean nuevo) {
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
        textProcedimiento = new RSyntaxTextArea();
        textProcedimiento.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        textProcedimiento.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(textProcedimiento);
        panelText.add(sp);
        panelText.setLayout((LayoutManager) new BoxLayout(panelText, BoxLayout.Y_AXIS));
        panelText.add(sp);

        CompletionProvider provider = createCompletionProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(textProcedimiento);
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
                String sql = "CREATE PROCEDURE " + nombre + " ( )\n";
                sql += "Begin\n\nEnd;\n";
                textProcedimiento.setText(sql);
            } else {
                textProcedimiento.setText(op.getSqlProcedimiento(BaseDeDatos, nombre));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar procedimiento", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        botonGuardar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        textParametros = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        panelText = new javax.swing.JPanel();

        botonGuardar.setText("Guardar");
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });

        jLabel4.setText("Procedimiento:");

        jLabel1.setText("Parametros:");

        jButton1.setText("Ejecutar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("En desarrollo...");

        javax.swing.GroupLayout panelTextLayout = new javax.swing.GroupLayout(panelText);
        panelText.setLayout(panelTextLayout);
        panelTextLayout.setHorizontalGroup(
            panelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelTextLayout.setVerticalGroup(
            panelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 280, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(textParametros, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                        .addComponent(botonGuardar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(panelText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonGuardar)
                    .addComponent(jLabel1)
                    .addComponent(textParametros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarActionPerformed
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            if (!BaseDeDatos.equals(op.getBDseleccionada())) {
                op.seleccionarBD(BaseDeDatos);
            }
            op.ejecutarUpdate(op.getGeneradorSQL().borrarProcedimiento(nombre));
            op.ejecutarUpdate(op.getGeneradorSQL().crearProcedimiento(textProcedimiento.getText()));
            nuevo = false;
            JOptionPane.showMessageDialog(null, "Operacion exitosa", "Exitoso", 1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar procedimiento", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", 0);
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_botonGuardarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            if (!BaseDeDatos.equals(op.getBDseleccionada())) {
                op.seleccionarBD(BaseDeDatos);
            }
            op.ejecutarUpdate(op.getGeneradorSQL().LlamarProcedimiento(nombre, textParametros.getText()));
            JOptionPane.showMessageDialog(null, "Operacion exitosa", "Exitoso", 1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al ejecutar procedimiento", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", 0);
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonGuardar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel panelText;
    private javax.swing.JTextField textParametros;
    // End of variables declaration//GEN-END:variables
}
