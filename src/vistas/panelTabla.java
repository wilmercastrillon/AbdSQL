package vistas;

import clases.conexion;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wilmer
 */
public class panelTabla extends javax.swing.JPanel {

    private final String tabla, BaseDeDatos;
    private final ResultSet res;
    private final conexion con;
    private final modificarTabla ma;

    public panelTabla(conexion c, String bd, String t, ResultSet rs) {
        con = c;
        BaseDeDatos = bd;
        tabla = t;
        res = rs;
        initComponents();
        llenarTabla();
        ma = new modificarTabla(con, tabla);
    }

    public void llenarTabla() {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            ResultSetMetaData rsmd = res.getMetaData();

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                modelo.addColumn(rsmd.getColumnName(i));
            }

            while (res.next()) {
                Vector<String> v = new Vector<>();
                for (int i = 1; i <= modelo.getColumnCount(); i++) {
                    v.add(res.getString(i));
                }
                modelo.addRow(v);
            }

            jTable1.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar tabla", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado", "Error", 0);
            System.out.println("\nError: panelTabla: llenarTabla");
            System.out.println(ex.getMessage() + "\n");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        botonAtributos = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        recargar = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        botonAtributos.setText("Modificar tabla");
        botonAtributos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAtributosActionPerformed(evt);
            }
        });

        jButton2.setText("Agregar Registro");

        recargar.setText("Recargar");
        recargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recargarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(botonAtributos)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(recargar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAtributos)
                    .addComponent(jButton2)
                    .addComponent(recargar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botonAtributosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAtributosActionPerformed
        try {
            if (!BaseDeDatos.equals(con.BaseDeDatosSeleccionada)) {
                con.SelectDataBase(BaseDeDatos);
            }
            ma.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar base de datos", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
    }//GEN-LAST:event_botonAtributosActionPerformed

    private void recargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recargarActionPerformed
        llenarTabla();
    }//GEN-LAST:event_recargarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAtributos;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton recargar;
    // End of variables declaration//GEN-END:variables
}
