package vistas;

import clases.operaciones;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wilmer
 */
public class panelTabla extends javax.swing.JPanel implements KeyListener {

    private final String tabla, BaseDeDatos;
    private ResultSet res;
    private final operaciones op;
    private final modificarTabla ma;
    private boolean inserta, nuevaFila;
    private Vector<String> columnas;
    private DefaultTableModel modelo;

    public panelTabla(operaciones c, String bd, String t, ResultSet rs) {
        op = c;
        BaseDeDatos = bd;
        tabla = t;
        res = rs;
        columnas = new Vector<>();
        initComponents();
        llenarTabla();
        inserta = nuevaFila = false;
        ma = new modificarTabla(op, tabla);
        botonAgregarRegistro.addKeyListener(this);
        botonAtributos.addKeyListener(this);
        botonRecargar.addKeyListener(this);
        jTable1.addKeyListener(this);

        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuitem = new JMenuItem("borrar fila(s)");
        menuitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                inserta = true;

                int filas[] = jTable1.getSelectedRows();
                for (int i = 0; i < filas.length; i++) {
                    Vector<String> datos = new Vector<>();
                    try {
                        for (int j = 0; j < columnas.size(); j++) {
                            if (jTable1.getValueAt(filas[i], j) == null) {
                                datos.add(null);
                            } else {
                                datos.add(jTable1.getValueAt(filas[i], j).toString());
                            }
                        }
                        op.getConexion().borrarFila(datos, columnas, tabla);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al borrar fila", "Error", 0);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                    }
                }
                
                if (filas.length == 1) {
                    modelo.removeRow(filas[0]);
                } else {
                    recargarResultSet();
                    llenarTabla();
                }
                inserta = false;
            }
        });
        menu.add(menuitem);
        jTable1.setComponentPopupMenu(menu);
    }

    private void llenarTabla() {
        inserta = true;
        try {
            modelo = new DefaultTableModel();
            op.llenarTableModel(res, modelo);
            EventoJtable(modelo);
            jTable1.setModel(modelo);

            columnas.clear();
            for (int i = 0; i < modelo.getColumnCount(); i++) {
                columnas.add(modelo.getColumnName(i));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar tabla", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
        inserta = false;
    }

    private void EventoJtable(DefaultTableModel m) {
        m.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent tme) {
                if (inserta) {
                    return;
                }

                try {
                    int fila = tme.getFirstRow(), columna = tme.getColumn();
                    if (nuevaFila) {
                        inserta = true;
                        if (fila == modelo.getRowCount() - 1) {
                        } else {
                            modelo.removeRow(modelo.getRowCount() - 1);
                            nuevaFila = false;
                        }
                        inserta = false;
                        return;
                    }

                    Vector<String> datos = new Vector<>();
                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        if (modelo.getValueAt(fila, i) == null) {
                            datos.add(null);
                        } else {
                            datos.add(modelo.getValueAt(fila, i).toString());
                        }
                    }

                    op.getConexion().actualizarFila(tabla, columnas, datos, modelo.getColumnName(columna),
                            modelo.getValueAt(fila, columna).toString());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al modificar los datos", "Error", 0);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un\nerror inesperado", "Error", 0);
                    System.out.println("Error: PanelTabla: eventoJtable");
                    System.out.println(e.getMessage() + "\n");
                }
            }
        });
    }

    public void recargarResultSet() {
        try {
            res = op.getConexion().GetDatosTabla(tabla);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar tabla", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
    }

    private Vector<String> datosNuevaFila() {
        Vector<String> vs = new Vector<>();
        int l = modelo.getColumnCount(), f = modelo.getRowCount();
        for (int i = 0; i < l; i++) {
            if (jTable1.getValueAt(f - 1, i) == null || jTable1.getValueAt(f - 1, i).toString().isEmpty()) {
                vs.add(null);
            } else {
                vs.add(jTable1.getValueAt(f - 1, i).toString());
            }
        }
        return vs;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        botonAtributos = new javax.swing.JButton();
        botonAgregarRegistro = new javax.swing.JButton();
        botonRecargar = new javax.swing.JButton();

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

        botonAgregarRegistro.setText("Agregar Registro");
        botonAgregarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgregarRegistroActionPerformed(evt);
            }
        });

        botonRecargar.setText("Recargar");
        botonRecargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRecargarActionPerformed(evt);
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
                        .addComponent(botonAgregarRegistro)
                        .addGap(18, 18, 18)
                        .addComponent(botonRecargar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAtributos)
                    .addComponent(botonAgregarRegistro)
                    .addComponent(botonRecargar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botonAtributosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAtributosActionPerformed
        try {
            if (!BaseDeDatos.equals(op.getConexion().BaseDeDatosSeleccionada)) {
                op.getConexion().SelectDataBase(BaseDeDatos);
            }
            ma.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar base de datos", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
    }//GEN-LAST:event_botonAtributosActionPerformed

    private void botonRecargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRecargarActionPerformed
        recargarResultSet();
        llenarTabla();
        nuevaFila = false;
    }//GEN-LAST:event_botonRecargarActionPerformed

    private void botonAgregarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgregarRegistroActionPerformed
        if (nuevaFila) {
            return;
        }
        inserta = true;
        modelo.addRow(new Vector());
        inserta = false;
        nuevaFila = true;
    }//GEN-LAST:event_botonAgregarRegistroActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAgregarRegistro;
    private javax.swing.JButton botonAtributos;
    private javax.swing.JButton botonRecargar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (e.getComponent() == botonAtributos) {
                botonAtributosActionPerformed(null);
                return;
            }
            if (e.getComponent() == botonAgregarRegistro) {
                botonAgregarRegistroActionPerformed(null);
                return;
            }
            if (e.getComponent() == botonRecargar) {
                botonRecargarActionPerformed(null);
                return;
            }
            if (e.getComponent() == jTable1) {
                if (nuevaFila) {
                    System.out.println("se inserta la nueva fila");
                    try {
                        if (!op.getConexion().agregarRegistro(tabla, columnas, datosNuevaFila())) {
                            System.out.println("falla");
                            return;
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al insertar nuevo registro", "Error", 0);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                        System.out.println("falla");
                        return;
                    }
                    nuevaFila = false;
                    System.out.println("inserta con exito");
                }
                return;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (nuevaFila) {
                inserta = true;
                modelo.removeRow(modelo.getRowCount() - 1);
                inserta = false;
                nuevaFila = false;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}