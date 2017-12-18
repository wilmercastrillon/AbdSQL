package vistas;

import clases.operaciones;
import java.awt.Cursor;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class modificarTabla extends javax.swing.JFrame {

    private final operaciones op;
    private final String tabla;
    private boolean flag = false;

    public modificarTabla(operaciones c, String table) {
        initComponents();
        op = c;
        tabla = table;
        cargar();
        setLocationRelativeTo(null);
        setTitle(tabla);
    }

    private void cargar() {
        try {
            //cargar columnas 
            comboColumnas.removeAllItems();
            comboColumnas.addItem("------------");
            ComboColumnaForanea.removeAllItems();
            ComboColumnaForanea.addItem("------------");
            comboColumnaPrimaria.removeAllItems();
            comboColumnaPrimaria.addItem("------------");
            ResultSet res = op.getConexion().GetColumnasTabla(tabla);
            String h;
            while (res.next()) {
                h = res.getString(1);
                comboColumnas.addItem(h);
                comboColumnaPrimaria.addItem(h);
                ComboColumnaForanea.addItem(h);
            }

            //cargar otras tablas
            flag = true;
            comboOtrasTablas.removeAllItems();
            comboOtrasTablas.addItem("------------");
            ResultSet rs = op.getConexion().GetTables();
            while (rs.next()) {
                comboOtrasTablas.addItem(rs.getString(1));
            }
            
            //cargar tabla
            DefaultTableModel dft = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            op.llenarTableModel(op.getConexion().GetColumnasTabla(tabla), dft);
            jTable1.setModel(dft);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los\natributos de la tabla.", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado", "Error", 0);
            System.out.println("\nError: modificarAtributos: cargar");
            System.out.println(ex.getMessage() + "\n");
        }
        flag = false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        botonCrearColumna = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        textNombreColumna = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        comboTiposDatos = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        textLongitud = new javax.swing.JTextField();
        radioDefault = new javax.swing.JRadioButton();
        textDefault = new javax.swing.JTextField();
        radioNoNulo = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        botonBorrarColumna = new javax.swing.JButton();
        comboColumnas = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        botonCrearPrimaria = new javax.swing.JButton();
        comboColumnaPrimaria = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        ComboColumnaForanea = new javax.swing.JComboBox<>();
        botonCrearForanea = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        comboOtrasTablas = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        comboAtributosReferencia = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        botonCrearColumna.setText("Crear columna");
        botonCrearColumna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearColumnaActionPerformed(evt);
            }
        });

        jLabel1.setText("Nuevo atributo");

        jLabel2.setText("Nombre");

        jLabel3.setText("Tipo de dato");

        comboTiposDatos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-------------", "SMALLINT", "INT", "BIGINT", "FLOAT", "DOUBLE", "DECIMAL", "CHAR", "VARCHAR", "TEXT", "DATE", "TIME", "YEAR", "DATETIME", "TIMESTAMP", "BYNARY", "BLOB", "LONGBLOB" }));

        jLabel4.setText("Longitud");

        radioDefault.setText("Default");

        radioNoNulo.setText("No nulo");

        jLabel5.setText("Borrar atributo");

        botonBorrarColumna.setText("Borrar");
        botonBorrarColumna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBorrarColumnaActionPerformed(evt);
            }
        });

        comboColumnas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

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

        jLabel6.setText("Crear llave primaria");

        botonCrearPrimaria.setText("Crear llave");
        botonCrearPrimaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearPrimariaActionPerformed(evt);
            }
        });

        comboColumnaPrimaria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

        jLabel7.setText("Atributo llave");

        ComboColumnaForanea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

        botonCrearForanea.setText("Crear llave");
        botonCrearForanea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearForaneaActionPerformed(evt);
            }
        });

        jLabel8.setText("Crear llave foranea");

        jLabel9.setText("Tabla de referencia");

        comboOtrasTablas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));
        comboOtrasTablas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboOtrasTablasActionPerformed(evt);
            }
        });

        jLabel10.setText("Atributo de referencia");

        comboAtributosReferencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(ComboColumnaForanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10))
                            .addComponent(jLabel8)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(comboOtrasTablas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(botonCrearForanea)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 295, Short.MAX_VALUE)
                                .addComponent(jButton1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(comboAtributosReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonCrearColumna)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(botonBorrarColumna)
                                .addGap(18, 18, 18)
                                .addComponent(comboColumnas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(botonCrearPrimaria)
                                .addGap(18, 18, 18)
                                .addComponent(comboColumnaPrimaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(comboTiposDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(textLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(radioDefault)
                                .addGap(18, 18, 18)
                                .addComponent(textDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(radioNoNulo))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(textNombreColumna, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(textNombreColumna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(comboTiposDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(textLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radioDefault)
                            .addComponent(textDefault, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(radioNoNulo))
                        .addGap(18, 18, 18)
                        .addComponent(botonCrearColumna)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(botonBorrarColumna)
                            .addComponent(comboColumnas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboColumnaPrimaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonCrearPrimaria))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addComponent(jLabel8))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(ComboColumnaForanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(comboAtributosReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(comboOtrasTablas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCrearForanea)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonCrearColumnaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearColumnaActionPerformed
        if (comboTiposDatos.getSelectedIndex() == 0 || textNombreColumna.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error verifique los datos", "Error", 0);
            return;
        }
        
        setCursor(Cursor.WAIT_CURSOR);
        String lon = null, Def = null;
        if (!textLongitud.getText().isEmpty()) {
            lon = textLongitud.getText();
        }
        if (radioDefault.isSelected()) {
            Def = textDefault.getText();
        }

        try {
            op.getConexion().agregarColumnaTabla(tabla, comboTiposDatos.getSelectedItem().toString(),
                    textNombreColumna.getText(), lon, Def, radioNoNulo.isSelected());
            cargar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error verifique los datos", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_botonCrearColumnaActionPerformed

    private void botonBorrarColumnaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBorrarColumnaActionPerformed
        if (comboColumnas.getSelectedIndex() == 0) {
            return;
        }

        setCursor(Cursor.WAIT_CURSOR);
        try {
            op.getConexion().borrarColumnaTabla(tabla, comboColumnas.getSelectedItem().toString());
            cargar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_botonBorrarColumnaActionPerformed

    private void botonCrearPrimariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearPrimariaActionPerformed
        if (comboColumnas.getSelectedIndex() == 0) {
            return;
        }

        setCursor(Cursor.WAIT_CURSOR);
        try {
            op.getConexion().crearLlavePrimaria(tabla, comboColumnas.getSelectedItem().toString());
            cargar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al crear\nllave primaria", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_botonCrearPrimariaActionPerformed

    private void botonCrearForaneaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearForaneaActionPerformed
        if (comboOtrasTablas.getSelectedIndex() == 0 || comboAtributosReferencia.getSelectedIndex() == 0
                || ComboColumnaForanea.getSelectedIndex() == 0) {
            return;
        }
        
        setCursor(Cursor.WAIT_CURSOR);
        try {
            op.getConexion().crearLlaveForanea(tabla, ComboColumnaForanea.getSelectedItem().toString(),
                    comboOtrasTablas.getSelectedItem().toString(), comboAtributosReferencia.getSelectedItem().toString());
            cargar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al crear\nllave foranea", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_botonCrearForaneaActionPerformed

    private void comboOtrasTablasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboOtrasTablasActionPerformed
        if (flag) {
            return;
        }
        if (comboOtrasTablas.getSelectedIndex() == 0) {
            comboAtributosReferencia.removeAllItems();
            comboAtributosReferencia.addItem("------------");
            return;
        }

        try {
            ResultSet rs = op.getConexion().GetColumnasTabla(comboOtrasTablas.getSelectedItem().toString());
            while (rs.next()) {
                comboAtributosReferencia.addItem(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los\natributos de la tabla.", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado", "Error", 0);
            System.out.println("\nError: modificarIndices: comboOtrasTablasActionPerformed");
            System.out.println(e.getMessage() + "\n");
        }
    }//GEN-LAST:event_comboOtrasTablasActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(modificarTabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(modificarTabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(modificarTabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(modificarTabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new modificarAtributos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboColumnaForanea;
    private javax.swing.JButton botonBorrarColumna;
    private javax.swing.JButton botonCrearColumna;
    private javax.swing.JButton botonCrearForanea;
    private javax.swing.JButton botonCrearPrimaria;
    private javax.swing.JComboBox<String> comboAtributosReferencia;
    private javax.swing.JComboBox<String> comboColumnaPrimaria;
    private javax.swing.JComboBox<String> comboColumnas;
    private javax.swing.JComboBox<String> comboOtrasTablas;
    private javax.swing.JComboBox<String> comboTiposDatos;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JRadioButton radioDefault;
    private javax.swing.JRadioButton radioNoNulo;
    private javax.swing.JTextField textDefault;
    private javax.swing.JTextField textLongitud;
    private javax.swing.JTextField textNombreColumna;
    // End of variables declaration//GEN-END:variables
}
