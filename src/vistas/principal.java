package vistas;

import clases.conexion;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class principal extends javax.swing.JFrame implements KeyListener {

    private DefaultTreeModel modelo_arbol;
    private DefaultMutableTreeNode raiz;
    private Vector<DefaultMutableTreeNode> nodos;
    private final conexion con;
    private JPopupMenu menu2, menu1, menu3;
    public inicio ini;

    public principal(conexion x, inicio i) {
        con = x;
        ini = i;
        initComponents();
        cargar();

        menu1 = new JPopupMenu();
        menu2 = new JPopupMenu();
        menu3 = new JPopupMenu();
        JMenuItem crearBD = new JMenuItem("Crear Base de Datos");
        crearBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String h = JOptionPane.showInputDialog(null, "Inserte nombre");
                if (h == null) {
                    return;
                }
                try {
                    con.CrearDataBase(h);
                    cargar();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al crear\nla base de datos", "Error", 0);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                }
            }
        });
        JMenuItem agregar = new JMenuItem("crear tabla");
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TreePath path = jTree1.getSelectionPath();
                if (path.getPathCount() == 2) {
                    String h = JOptionPane.showInputDialog(null, "nombre de la tabla");
                    if (h == null) {
                        return;
                    }

                    try {
                        String h2 = path.getPathComponent(1).toString();
                        con.SelectDataBase(h2);
                        con.CrearTabla(h);
                        for (DefaultMutableTreeNode nodo : nodos) {
                            if (nodo.toString().equals(h2)) {
                                modelo_arbol.insertNodeInto(new DefaultMutableTreeNode(h), nodo, 0);
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al crear tabla", "Error", 0);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                    }
                }
            }
        });
        JMenuItem borrar = new JMenuItem("borrar tabla");
        borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {

                TreePath path = jTree1.getSelectionPath();
                if (path.getPathCount() == 3) {
                    if (JOptionPane.showConfirmDialog(null, "Seguro que quiere\nborrar la tabla")
                            != JOptionPane.YES_OPTION) {
                        return;
                    }
                    try {
                        System.out.println("de la base de datos " + path.getPathComponent(1).toString());
                        System.out.println("se borra la tabla " + path.getPathComponent(2).toString());
                        con.SelectDataBase(path.getPathComponent(1).toString());
                        con.BorrarTabla(path.getPathComponent(2).toString());
                        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (path.getLastPathComponent());
                        modelo_arbol.removeNodeFromParent(currentNode);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al borrar tabla", "Error", 0);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un\nerror inesperado", "Error", 0);
                        System.out.println("\nError: princiapl: constructor: borrar.addActionListener");
                        System.out.println(ex.getMessage() + "\n");
                    }
                }
            }
        });
        JMenuItem borrarBD = new JMenuItem("borrar Base de Datos");
        borrarBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TreePath path = jTree1.getSelectionPath();
                String bd = path.getLastPathComponent().toString();
                int q = JOptionPane.showConfirmDialog(null, "Seguro que la desea borrar");

                if (q == JOptionPane.YES_OPTION) {
                    int q2 = JOptionPane.showConfirmDialog(null, "Segurisimo que desea borrar\nla base de datos\n" + bd);
                    if (q2 == JOptionPane.YES_OPTION) {
                        try {
                            con.BorrarDataBase(bd);
                            cargar();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Error al borrar\nbase de datos", "Error", 0);
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                        }
                    }
                }
            }
        });
        menu1.add(crearBD);
        menu2.add(agregar);
        menu2.add(borrarBD);
        menu3.add(borrar);

        eventojtree();
//        jButton1.addKeyListener(this);
//        jButton2.addKeyListener(this);
        jTree1.addKeyListener(this);

        setTitle("Bases de datos");
        setLocationRelativeTo(null);
    }

    public void cerrarTab(String titulo) {
        for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {
            if (jTabbedPane1.getTitleAt(i).equals(titulo)) {
                jTabbedPane1.remove(i);
            }
        }
    }

    public void cargar() {
        try {
            raiz = new DefaultMutableTreeNode("Bases de Datos");
            modelo_arbol = new DefaultTreeModel(raiz);
            jTree1.setModel(modelo_arbol);

            nodos = new Vector<>();
            ResultSet res = con.GetDataBases();
            String h;
            int pos = 0, pos2;

            while (res.next()) {
                h = res.getString(1);
                DefaultMutableTreeNode nuevo = new DefaultMutableTreeNode(h);
                modelo_arbol.insertNodeInto(nuevo, raiz, pos);
                nodos.add(nuevo);
                pos++;
            }

            for (int i = 0; i < nodos.size(); i++) {
                try {
                    con.SelectDataBase(nodos.get(i).toString());
                    ResultSet res2 = con.GetTables();
                    pos2 = 0;
                    System.out.println("tabla " + nodos.get(i).toString());
                    while (res2.next()) {
                        String h2 = res2.getString("Tables_in_" + nodos.get(i).toString());
                        modelo_arbol.insertNodeInto(new DefaultMutableTreeNode(h2), nodos.get(i), pos2);
                        pos2++;
                    }
                } catch (Exception e) {
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        }
    }

    private void eventojtree() {
        jTree1.addMouseListener(new java.awt.event.MouseListener() {
            public void mouseClicked(MouseEvent me) {
                TreePath tp = jTree1.getPathForLocation(me.getX(), me.getY());
                if (tp == null) {
                    return;
                }

                if (me.getButton() == MouseEvent.BUTTON3) {
                    switch (tp.getPathCount()) {
                        case 1:
                            menu1.show(me.getComponent(), me.getX(), me.getY());
                            break;
                        case 2:
                            menu2.show(me.getComponent(), me.getX(), me.getY());
                            break;
                        case 3:
                            menu3.show(me.getComponent(), me.getX(), me.getY());
                            break;
                    }
                    return;
                }

                if (me.getClickCount() == 1 || me.getClickCount() > 2) {
                    return;
                }

                if (tp.getPathCount() == 3) {
                    agregarPanel(tp.getPathComponent(1).toString(), tp.getPathComponent(2).toString());
                }
            }

            public void mousePressed(MouseEvent me) {
            }

            public void mouseReleased(MouseEvent me) {
            }

            public void mouseEntered(MouseEvent me) {
            }

            public void mouseExited(MouseEvent me) {
            }
        });
    }

    public void agregarPanel(String DB, String tabla) {
        try {
            if (!con.BaseDeDatosSeleccionada.equals(DB)) {
                con.SelectDataBase(DB);
            }
            JButton tabButton = new JButton(new ImageIcon(getClass().getResource("/imagenes/cerrar.png")));
            panelTabla pt = new panelTabla(con, DB, tabla, con.GetDatos(tabla));
            jTabbedPane1.addTab(tabla, pt);

            tabButton.setPreferredSize(new Dimension(15, 15));
            tabButton.setContentAreaFilled(false);
            tabButton.setToolTipText("Cerrar");
            tabButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cerrarTab(tabla);
                }
            });

            JPanel pnl = new JPanel();
            pnl.setOpaque(false);
            JLabel label = new JLabel(tabla);
            pnl.add(label);
            pnl.add(tabButton);
            jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1, pnl);
            jTabbedPane1.setSelectedIndex(jTabbedPane1.getTabCount() - 1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar tabla", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error inseperado", "Error", 0);
            System.out.println("\nError: principal: Agregar panel");
            System.out.println(ex.getMessage() + "\n");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        salir = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(jTree1);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        salir.setText("salir");
        salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirActionPerformed(evt);
            }
        });

        jButton1.setText("consola");
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 283, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(159, 159, 159)
                        .addComponent(salir))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salir)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        con.desconectar();
        this.setVisible(false);
        ini.setVisible(true);
    }//GEN-LAST:event_salirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        con.cons.setVisible(true);
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
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton salir;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (e.getComponent() == jTree1) {
                TreePath p = jTree1.getSelectionPath();
                if (p.getPathCount() == 3) {
                    agregarPanel(p.getPathComponent(1).toString(), p.getPathComponent(2).toString());
                } else {
                    if (jTree1.isExpanded(p)) {
                        jTree1.collapsePath(p);
                    } else {
                        jTree1.expandPath(p);
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
