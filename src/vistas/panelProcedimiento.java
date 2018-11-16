package vistas;

import clases.operaciones;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class panelProcedimiento extends javax.swing.JPanel {

    private final operaciones op;
    public final String nombre, BaseDeDatos;
    public boolean nuevo;

    public panelProcedimiento(operaciones op, String BD, String nombre, boolean nuevo) {
        this.op = op;
        this.nombre = nombre;
        this.nuevo = nuevo;
        BaseDeDatos = BD;
        initComponents();
        cargar();
        setName(nombre);
    }

    private void cargar() {
        try {
            if (nuevo) {
                String sql = "CREATE OR REPLACE PROCEDURE " + nombre + " ( )\n";
                sql += "IS\nBegin\n\nEnd;\n";
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

        jScrollPane1 = new javax.swing.JScrollPane();
        textProcedimiento = new javax.swing.JTextArea();
        botonGuardar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        textParametros = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        textProcedimiento.setColumns(20);
        textProcedimiento.setRows(5);
        jScrollPane1.setViewportView(textProcedimiento);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(textParametros, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
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
        try {
            op.GuardarProcedimiento(nombre, textProcedimiento.getText(), nuevo);
            nuevo = false;
            JOptionPane.showMessageDialog(null, "Operacion exitosa", "Exitoso", 1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar procedimiento", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", 0);
        }
    }//GEN-LAST:event_botonGuardarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            op.ejecutarUpdate(op.getGeneradorSQL().LlamarProcedimiento(nombre, textParametros.getText()));
            JOptionPane.showMessageDialog(null, "Operacion exitosa", "Exitoso", 1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al ejecutar procedimiento", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", 0);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonGuardar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField textParametros;
    private javax.swing.JTextArea textProcedimiento;
    // End of variables declaration//GEN-END:variables
}
