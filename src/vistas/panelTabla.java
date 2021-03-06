package vistas;

import clases.Fachada;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author wilmer
 */
public class panelTabla extends javax.swing.JPanel implements KeyListener {

    public final String tabla, BaseDeDatos;
    private ResultSet res;
    private final Fachada op;
    private final modificarTabla ma;
    private boolean inserta, nuevaFila;
    private Vector<String> columnas;
    private DefaultTableModel modelo;

    public panelTabla(Fachada c, String bd, String t, ResultSet rs) {
        op = c;
        BaseDeDatos = bd;
        tabla = t;
        res = rs;
        columnas = new Vector<>();
        initComponents();
//        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        llenarTabla();
        inserta = nuevaFila = false;
        ma = new modificarTabla(op, op.getBDseleccionada(), tabla);
        botonAgregarRegistro.addKeyListener(this);
        botonAtributos.addKeyListener(this);
        botonRecargar.addKeyListener(this);
        jTable1.addKeyListener(this);

        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuitem = new JMenuItem("borrar fila(s)");
        menuitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                borrarFilas();
            }
        });
        menu.add(menuitem);
        jTable1.setComponentPopupMenu(menu);
        jTable1.setAutoCreateRowSorter(true);
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado", "Error", 0);
            System.err.println("Error: PanelTabla: llenarTabla");
            System.err.println(e.getMessage() + "\n");
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
                    Vector<String> col = new Vector<>();
                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        if (modelo.getValueAt(fila, i) != null) {
                            col.add(modelo.getColumnName(i));
                            datos.add(modelo.getValueAt(fila, i).toString());
                        }
                    }

                    op.ejecutarUpdate(op.getGeneradorSQL().updateRow(tabla, col, datos, modelo.getColumnName(columna),
                            modelo.getValueAt(fila, columna).toString()));
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

    public void borrarFilas() {
        inserta = true;
        
        int filas[] = jTable1.getSelectedRows();
        try {
            for (int i = 0; i < filas.length; i++) {
                Vector<String> datos = new Vector<>();
                for (int j = 0; j < columnas.size(); j++) {
                    if (jTable1.getValueAt(filas[i]-i, j) == null) {
                        datos.add(null);
                    } else {
                        datos.add(jTable1.getValueAt(filas[i]-i, j).toString());
                    }
                }
                op.ejecutarUpdate(op.getGeneradorSQL().deleteRow(tabla, columnas, datos));
                modelo.removeRow(filas[i]-i);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al borrar fila", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }

        inserta = false;
    }

    public void recargarResultSet() {
        try {
            res = op.ejecutarConsulta(op.getGeneradorSQL().selectRowsTable(tabla));
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
    
    protected void cerrarVentana() {
        ma.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jTable1 = new javax.swing.JTable();
        botonAtributos = new javax.swing.JButton();
        botonAgregarRegistro = new javax.swing.JButton();
        botonRecargar = new javax.swing.JButton();
        RadioAjustar = new javax.swing.JRadioButton();

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
        jTable1.setAutoscrolls(false);
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

        RadioAjustar.setText("Ajustar");
        RadioAjustar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioAjustarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(botonAtributos)
                        .addGap(18, 18, 18)
                        .addComponent(botonAgregarRegistro)
                        .addGap(18, 18, 18)
                        .addComponent(botonRecargar)
                        .addGap(18, 18, 18)
                        .addComponent(RadioAjustar)
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
                    .addComponent(botonRecargar)
                    .addComponent(RadioAjustar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botonAtributosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAtributosActionPerformed
        try {
            if (!BaseDeDatos.equals(op.getBDseleccionada())) {
                op.seleccionarBD(BaseDeDatos);
            }
            ma.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar base de datos", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
    }//GEN-LAST:event_botonAtributosActionPerformed

    private void botonRecargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRecargarActionPerformed
        try {
            if (!BaseDeDatos.equals(op.getBDseleccionada())) {
                op.seleccionarBD(BaseDeDatos);
            }
            recargarResultSet();
            //System.out.println("bd " + BaseDeDatos + " | " + tabla);
            int tam[] = new int[jTable1.getColumnCount()];
            TableColumnModel tcm = jTable1.getColumnModel();
            for (int i = 0; i < tcm.getColumnCount(); i++) {
                tam[i] = tcm.getColumn(i).getWidth();
            }
            llenarTabla();
            if (tcm.getColumnCount() == modelo.getColumnCount()) {
                for (int i = 0; i < tcm.getColumnCount(); i++) {
                    tcm.getColumn(i).setPreferredWidth(tam[i]);
                }
            }
            ma.cargar();
            nuevaFila = false;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar base de datos", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        } catch(Exception ex){
            System.err.println("Error: panelTabla, botonRecargarActionPerformed\n");
            System.err.println(ex.getMessage() + "\n");
        }
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

    private void RadioAjustarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioAjustarActionPerformed
        if (RadioAjustar.isSelected()) {
            jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        } else {
            jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        }
    }//GEN-LAST:event_RadioAjustarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton RadioAjustar;
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
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    try {
                        if (!BaseDeDatos.equals(op.getBDseleccionada())) {
                            op.seleccionarBD(BaseDeDatos);
                        }
                        op.ejecutarUpdate(op.getGeneradorSQL().addRow(tabla, columnas, datosNuevaFila()));
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al insertar nuevo registro", "Error", 0);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                        System.out.println("falla");
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        return;
                    }
                    nuevaFila = false;
                    System.out.println("inserta con exito");
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_DELETE && e.getComponent() == jTable1) {
            if (jTable1.getSelectedRowCount() == 0) {
                return;
            }
            int q = JOptionPane.showConfirmDialog(null, "Borrar fila(s)");
            if (q == JOptionPane.OK_OPTION) {
                borrarFilas();
            }
            return;
        }
        if (e.getComponent() == jTable1 && e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (jTable1.getSelectedRow() == jTable1.getRowCount()-1) {
                return;
            }
            Rectangle cellRect = jTable1.getCellRect(jTable1.getSelectedRow()+1, 0, true);
            jTable1.scrollRectToVisible(cellRect);
            return;
        }
        if (e.getComponent() == jTable1 && e.getKeyCode() == KeyEvent.VK_UP) {
            if (jTable1.getSelectedRow() < 1) {
                return;
            }
            Rectangle cellRect = jTable1.getCellRect(jTable1.getSelectedRow()-1, 0, true);
            jTable1.scrollRectToVisible(cellRect);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
