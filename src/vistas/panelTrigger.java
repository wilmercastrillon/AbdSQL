package vistas;

import clases.operaciones;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class panelTrigger extends javax.swing.JPanel {

    private final operaciones op;
    public final String nombre, BaseDeDatos;
    public boolean nuevo;

    public panelTrigger(operaciones op, String BD, String nombre, boolean nuevo) {
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
                String sql = "CREATE ";
                if (op.esConexionMysql()) {
                    sql += "DEFINER=`" + op.getUsuario() + "`@`%`";
                }else{
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

        jScrollPane1 = new javax.swing.JScrollPane();
        textTrigger = new javax.swing.JTextArea();
        botonGuardar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        textTrigger.setColumns(20);
        textTrigger.setRows(5);
        jScrollPane1.setViewportView(textTrigger);

        botonGuardar.setText("Guardar");
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });

        jLabel4.setText("Sql:");

        jLabel1.setText("En desarrollo...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botonGuardar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(botonGuardar)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarActionPerformed
        try {
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
    }//GEN-LAST:event_botonGuardarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea textTrigger;
    // End of variables declaration//GEN-END:variables
}
