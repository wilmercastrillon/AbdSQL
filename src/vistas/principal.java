package vistas;

import clases.ConexionMySql;
import clases.operaciones;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class principal extends javax.swing.JFrame implements KeyListener {

    private DefaultTreeModel modelo_arbol;
    private DefaultMutableTreeNode raiz;
    private Vector<DefaultMutableTreeNode> nodos;
    private Vector<JPanel> paneles;
    private final operaciones op;
    private JPopupMenu menu2, menu1, menu3, menu4;
    public inicio ini;

    public principal(operaciones x, inicio i) {
        op = x;
        ini = i;
        initComponents();
        cargar();

        menu1 = new JPopupMenu();
        menu2 = new JPopupMenu();
        menu3 = new JPopupMenu();
        menu4 = new JPopupMenu();

        JMenuItem agregar = new JMenuItem("Crear tabla");
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
                        op.getConexion().SelectDataBase(h2);
                        op.getConexion().CrearTabla(h);
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
        JMenuItem agregarTrigger = new JMenuItem("Crear trigger");
        agregarTrigger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("evento");
                TreePath path = jTree1.getSelectionPath();
                if (path.getPathCount() == 2) {
                    System.out.println("entra");
                    String h = JOptionPane.showInputDialog(null, "Nombre del trigger:");
                    if (h == null) {
                        return;
                    }

                    Object ob = path.getLastPathComponent();
                    DefaultMutableTreeNode d = (DefaultMutableTreeNode) ob;
                    agregarPanel(d.toString(), h, "TriggerNuevo");
                }
                System.out.println("fin");
            }
        });
        JMenuItem agregarProcedimiento = new JMenuItem("Crear procedimiento");
        agregarProcedimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TreePath path = jTree1.getSelectionPath();
                if (path.getPathCount() == 2) {
                    String h = JOptionPane.showInputDialog(null, "Nombre del procedimiento:");
                    if (h == null) {
                        return;
                    }
                    Object ob = path.getLastPathComponent();
                    DefaultMutableTreeNode d = (DefaultMutableTreeNode) ob;
                    agregarPanel(d.toString(), h, "ProcedimientoNuevo");
                }
            }
        });
        JMenuItem borrar = new JMenuItem("Borrar tabla");
        borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {

                TreePath path = jTree1.getSelectionPath();
                if (path.getPathCount() == 3) {
                    if (JOptionPane.showConfirmDialog(null, "Seguro que quiere\nborrar la tabla")
                            != JOptionPane.YES_OPTION) {
                        return;
                    }
                    try {
                        op.getConexion().SelectDataBase(path.getPathComponent(1).toString());
                        op.getConexion().BorrarTabla(path.getPathComponent(2).toString());
                        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (path.getLastPathComponent());
                        modelo_arbol.removeNodeFromParent(currentNode);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al borrar tabla", "Error", 0);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un\nerror inesperado", "Error", 0);
                        System.out.println(ex.getMessage() + "\n");
                    }
                }
            }
        });
        JMenuItem renombrarTabla = new JMenuItem("Renombrar tabla");
        renombrarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {

                TreePath path = jTree1.getSelectionPath();
                if (path.getPathCount() == 3) {
                    String str = JOptionPane.showInputDialog(null, "Ingrese nuevo nombre");
                    if (str == null) {
                        return;
                    }
                    try {
                        op.getConexion().SelectDataBase(path.getPathComponent(1).toString());
                        op.getConexion().renombrarTabla(path.getPathComponent(2).toString(), str);
                        JOptionPane.showMessageDialog(null, "Nombre cambiado", "Exitoso", 1);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al renombrar tabla", "Error", 0);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un\nerror inesperado", "Error", 0);
                        System.out.println("\nError: princiapl: constructor: renombrarTabla.addActionListener");
                        System.out.println(ex.getMessage() + "\n");
                    }
                }
            }
        });
        JMenuItem borrarTrigger = new JMenuItem("Borrar Trigger");
        borrarTrigger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TreePath path = jTree1.getSelectionPath();
                if (path.getPathCount() == 4) {
                    if (JOptionPane.showConfirmDialog(null, "Seguro que quiere\nborrar el Trigger")
                            != JOptionPane.YES_OPTION) {
                        return;
                    }
                    borrarNodoJtree(path, "trigger", "Triggers");
                }
            }
        });
        JMenuItem borrarProcedimiento = new JMenuItem("Borrar procedimiento");
        borrarProcedimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TreePath path = jTree1.getSelectionPath();
                if (path.getPathCount() == 4) {
                    if (JOptionPane.showConfirmDialog(null, "Seguro que quiere\nborrar el procedimiento")
                            != JOptionPane.YES_OPTION) {
                        return;
                    }
                    borrarNodoJtree(path, "procedimiento", "Procedimientos");
                }
            }
        });

        if (op.getConexion() instanceof ConexionMySql) {
            JMenuItem borrarBD = new JMenuItem("borrar Base de Datos");
            borrarBD.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    TreePath path = jTree1.getSelectionPath();
                    String bd = path.getLastPathComponent().toString();
                    int q = JOptionPane.showConfirmDialog(null, "Seguro que la desea borrar");

                    if (q == JOptionPane.YES_OPTION) {
                        int q2 = JOptionPane.showConfirmDialog(null, "Segurisimo que desea borrar\nla base de datos\n" + bd);
                        if (q2 == JOptionPane.YES_OPTION) {
                            System.out.println("se borraria");
                            ConexionMySql c = (ConexionMySql) op.getConexion();
                            try {
                                c.BorrarDataBase(bd);
                                cargar();
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "Error al borrar\nbase de datos", "Error", 0);
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                            }
                        }
                    }
                }
            });

            JMenuItem crearBD = new JMenuItem("Crear Base de Datos");
            crearBD.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    String h = JOptionPane.showInputDialog(null, "Inserte nombre");
                    if (h == null) {
                        return;
                    }
                    try {
                        op.getConexion().CrearDataBase(h);
                        cargar();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al crear\nla base de datos", "Error", 0);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                    }
                }
            });

            menu1.add(crearBD);
            menu2.add(borrarBD);
        }
        JMenuItem CrearMVC = new JMenuItem("Crear plantilla MVC");
        CrearMVC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser chooser = new JFileChooser("Seleccione carpeta destino");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int seleccion = chooser.showOpenDialog(null);
                if (seleccion != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                setCursor(Cursor.WAIT_CURSOR);
                TreePath path = jTree1.getSelectionPath();
                String bd = path.getLastPathComponent().toString();
                File fichero = chooser.getSelectedFile();
                File m = new File(fichero.getPath() + "\\Models");
                m.mkdirs();
                File c = new File(fichero.getPath() + "\\Controllers");
                c.mkdirs();
                
                try {
                    if(op.generaraMVC(op.getTablesDataBase(bd), fichero.getPath())){
                        JOptionPane.showMessageDialog(null, "Modelos y controladores\nCreados", "Exitoso", 1);
                    }else{
                        JOptionPane.showMessageDialog(null, "Error al cargar datos", "Error", 0);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al cargar datos", "Error", 0);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
                }
                setCursor(Cursor.DEFAULT_CURSOR);
            }
        });

        menu2.add(agregar);
        menu2.add(agregarTrigger);
        menu2.add(agregarProcedimiento);
        menu2.add(CrearMVC);
        menu3.add(borrar);
        menu3.add(renombrarTabla);
        menu4.add(borrarTrigger);
        menu4.add(borrarProcedimiento);

        eventojtree();
        jTree1.addKeyListener(this);
        TreePath p = jTree1.getPathForRow(0);
        jTree1.expandPath(p);

        setTitle("Bases de datos");
        setLocationRelativeTo(null);
    }

    private void borrarNodoJtree(TreePath path, String tipo, String parent) {
        try {
            if (!path.getPathComponent(2).toString().equals(parent)) {
                return;
            }

            op.getConexion().SelectDataBase(path.getPathComponent(1).toString());
            if (tipo.equalsIgnoreCase("trigger")) {
                op.getConexion().borrarTrigger(path.getPathComponent(3).toString());
            } else {
                op.getConexion().borrarProcedimiento(path.getPathComponent(3).toString());
            }
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (path.getLastPathComponent());
            modelo_arbol.removeNodeFromParent(currentNode);
            if (modelo_arbol.isLeaf(path.getPathComponent(2))) {
                modelo_arbol.removeNodeFromParent((DefaultMutableTreeNode) path.getPathComponent(2));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al borrar " + tipo, "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un\nerror inesperado", "Error", 0);
            System.out.println(ex.getMessage() + "\n");
        }
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
            setCursor(Cursor.WAIT_CURSOR);
            raiz = new DefaultMutableTreeNode("Bases de Datos");
            modelo_arbol = new DefaultTreeModel(raiz);
            jTree1.setModel(modelo_arbol);

            nodos = new Vector<>();
            paneles = new Vector<>();
            int pos2;
            Vector<String> bd = op.getBasesDeDatos();
            for (int i = 0; i < bd.size(); i++) {
                DefaultMutableTreeNode nuevo = new DefaultMutableTreeNode(bd.get(i));
                modelo_arbol.insertNodeInto(nuevo, raiz, i);
                nodos.add(nuevo);
            }

            for (int i = 0; i < nodos.size(); i++) {
                try {
                    op.getConexion().SelectDataBase(nodos.get(i).toString());
                    ResultSet res2 = op.getConexion().GetTables();
                    pos2 = 0;
                    System.out.println("tabla " + nodos.get(i).toString());

                    while (res2.next()) {
                        String h2 = res2.getString(1);
                        modelo_arbol.insertNodeInto(new DefaultMutableTreeNode(h2), nodos.get(i), pos2);
                        pos2++;
                    }

                    Vector<DefaultMutableTreeNode> v = op.getTriggers();
                    if (v.size() > 0) {
                        DefaultMutableTreeNode triggers = new DefaultMutableTreeNode("Triggers");
                        modelo_arbol.insertNodeInto(triggers, nodos.get(i), 0);
                        for (int j = 0; j < v.size(); j++) {
                            modelo_arbol.insertNodeInto(new DefaultMutableTreeNode(v.get(j)), triggers, j);
                        }
                    }

                    Vector<DefaultMutableTreeNode> v2 = op.getProcedimientos(nodos.get(i).toString());
                    if (v2.size() > 0) {
                        DefaultMutableTreeNode Proc = new DefaultMutableTreeNode("Procedimientos");
                        modelo_arbol.insertNodeInto(Proc, nodos.get(i), 0);
                        for (int j = 0; j < v2.size(); j++) {
                            modelo_arbol.insertNodeInto(new DefaultMutableTreeNode(v2.get(j)), Proc, j);
                        }
                    }
                } catch (Exception e) {
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
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
                            if (op.getConexion() instanceof ConexionMySql) {
                                menu1.show(me.getComponent(), me.getX(), me.getY());
                            }
                            break;
                        case 2:
                            menu2.show(me.getComponent(), me.getX(), me.getY());
                            break;
                        case 3:
                            if (!modelo_arbol.isLeaf(tp.getLastPathComponent())) {
                                return;
                            }
                            menu3.show(me.getComponent(), me.getX(), me.getY());
                            break;
                        case 4:
                            menu4.show(me.getComponent(), me.getX(), me.getY());
                            break;
                    }
                    return;
                }

                if (me.getClickCount() == 1 || me.getClickCount() > 2) {
                    return;
                }

                if (tp.getPathCount() == 3) {
                    Object nodo = tp.getLastPathComponent();
                    if (modelo_arbol.isLeaf(nodo)) {
                        agregarPanel(tp.getPathComponent(1).toString(), tp.getPathComponent(2).toString(), "Tabla");
                    }
                    return;
                }

                if (tp.getPathCount() == 4) {
                    Object nodo = tp.getPathComponent(2);
                    if (nodo.toString().equals("Procedimientos")) {
                        agregarPanel(tp.getPathComponent(1).toString(), tp.getPathComponent(3).toString(), "Procedimiento");
                    } else {
                        agregarPanel(tp.getPathComponent(1).toString(), tp.getPathComponent(3).toString(), "Trigger");
                    }
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

    public int containsTabla(String tab) {
        for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {
            if (jTabbedPane1.getTitleAt(i).equals(tab)) {
                return i;
            }
        }
        return -1;
    }

    public void agregarPanel(String DB, String nombre, String tipo) {
        int l = containsTabla(nombre);
        if (l != -1) {
            jTabbedPane1.setSelectedIndex(l);
            return;
        }
        setCursor(Cursor.WAIT_CURSOR);
        try {
            if (!op.getConexion().BaseDeDatosSeleccionada.equals(DB)) {
                op.getConexion().SelectDataBase(DB);
            }

            JPanel pt;
            if (tipo.equals("Tabla")) {
                pt = new panelTabla(op, DB, nombre, op.getConexion().GetDatosTabla(nombre));
            } else if (tipo.equals("TriggerNuevo")) {
                pt = new panelTrigger(op, DB, nombre, true);
            } else if (tipo.equals("Trigger")) {
                pt = new panelTrigger(op, DB, nombre, false);
            } else if (tipo.equals("ProcedimientoNuevo")) {
                pt = new panelProcedimiento(op, DB, nombre, true);
            } else {
                pt = new panelProcedimiento(op, DB, nombre, false);
            }
            JButton tabButton = new JButton(new ImageIcon(getClass().getResource("/imagenes/cerrar.png")));
            jTabbedPane1.addTab(nombre, pt);
            paneles.add(pt);

            tabButton.setPreferredSize(new Dimension(15, 15));
            tabButton.setContentAreaFilled(false);
            tabButton.setToolTipText("Cerrar");
            tabButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cerrarTab(nombre);
                }
            });

            JPanel pnl = new JPanel();
            pnl.setOpaque(false);
            JLabel label = new JLabel(nombre);
            pnl.add(label);
            pnl.add(tabButton);
            jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1, pnl);
            jTabbedPane1.setSelectedIndex(jTabbedPane1.getTabCount() - 1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error inseperado", "Error", 0);
            System.out.println("\nError: principal: Agregar panel");
            System.out.println(ex.getMessage() + "\n");
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }

    private void cerrarVentanas() {
        for (int i = 0; i < paneles.size(); i++) {
            if (paneles.get(i) instanceof panelTabla) {
                ((panelTabla) paneles.get(i)).cerrarVentana();
            }
        }
        paneles.clear();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        salir = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        botonCerrartab = new javax.swing.JButton();
        botonRecargar = new javax.swing.JButton();
        botonCerrarVentanas = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        botonCerrartab.setText("Cerrar pestañas");
        botonCerrartab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCerrartabActionPerformed(evt);
            }
        });

        botonRecargar.setText("Recargar");
        botonRecargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRecargarActionPerformed(evt);
            }
        });

        botonCerrarVentanas.setText("Cerrar ventanas");
        botonCerrarVentanas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCerrarVentanasActionPerformed(evt);
            }
        });

        jSplitPane1.setDividerLocation(190);
        jSplitPane1.setDividerSize(15);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jSplitPane1.setRightComponent(jTabbedPane1);

        jScrollPane1.setViewportView(jTree1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(botonRecargar)
                        .addGap(191, 191, 191)
                        .addComponent(botonCerrartab)
                        .addGap(18, 18, 18)
                        .addComponent(botonCerrarVentanas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(104, 104, 104)
                        .addComponent(salir))
                    .addComponent(jSplitPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salir)
                    .addComponent(jButton1)
                    .addComponent(botonCerrartab)
                    .addComponent(botonRecargar)
                    .addComponent(botonCerrarVentanas))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        cerrarVentanas();
        op.getConexion().desconectar();
        this.setVisible(false);
        ini.setVisible(true);
    }//GEN-LAST:event_salirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        op.mostrarConsola();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void botonCerrartabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCerrartabActionPerformed
        jTabbedPane1.removeAll();
    }//GEN-LAST:event_botonCerrartabActionPerformed

    private void botonRecargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRecargarActionPerformed
        cargar();
    }//GEN-LAST:event_botonRecargarActionPerformed

    private void botonCerrarVentanasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCerrarVentanasActionPerformed
        cerrarVentanas();
    }//GEN-LAST:event_botonCerrarVentanasActionPerformed

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
            java.util.logging.Logger.getLogger(principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JButton botonCerrarVentanas;
    private javax.swing.JButton botonCerrartab;
    private javax.swing.JButton botonRecargar;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
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
                    Object nodo = p.getLastPathComponent();
                    if (modelo_arbol.isLeaf(nodo)) {
                        agregarPanel(p.getPathComponent(1).toString(), p.getPathComponent(2).toString(), "Tabla");
                        return;
                    }
                } else {
                    if (p.getPathCount() == 4) {
                        if (p.getPathComponent(2).equals("Procedimientos")) {
                            agregarPanel(p.getPathComponent(1).toString(), p.getPathComponent(3).toString(), "Procedimiento");
                        } else {
                            agregarPanel(p.getPathComponent(1).toString(), p.getPathComponent(3).toString(), "Trigger");
                        }
                        return;
                    }
                }

                if (jTree1.isExpanded(p)) {
                    jTree1.collapsePath(p);
                } else {
                    jTree1.expandPath(p);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
