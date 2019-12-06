package vistas;

import clases.Fachada;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class principal extends javax.swing.JFrame implements KeyListener {

    private DefaultTreeModel modelo_arbol;
    private DefaultMutableTreeNode raiz;
    private Vector<DefaultMutableTreeNode> nodos;
    private Vector<JPanel> paneles;
    private Vector<String> bases_de_datos;
    private final Fachada op;
    private JPopupMenu menu2, menu1, menu3, menu4;
    private ventanaMVC vm;
    private ventanaDD vd;
    public inicio ini;

    public principal(Fachada x, inicio i) {
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
                        op.seleccionarBD(h2);
                        op.ejecutarUpdate(op.getGeneradorSQL().createTable(h));
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
                        op.seleccionarBD(path.getPathComponent(1).toString());
                        op.ejecutarUpdate(op.getGeneradorSQL().dropTable(path.getPathComponent(2).toString()));
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
                        op.seleccionarBD(path.getPathComponent(1).toString());
                        op.ejecutarUpdate(op.getGeneradorSQL().renameTable(path.getPathComponent(2).toString(), str));
                        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (path.getLastPathComponent());
                        currentNode.setUserObject(str + "");
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

        if (op.esConexionMysql()) {
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
                            GeneradorSQL.GeneradorMySQL g = (GeneradorSQL.GeneradorMySQL) op.getGeneradorSQL();
                            try {
                                op.ejecutarUpdate(g.dropDataBase(bd));
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
                        op.ejecutarUpdate(op.getGeneradorSQL().createDataBase(h));
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

        menu2.add(agregar);
        menu2.add(agregarTrigger);
        menu2.add(agregarProcedimiento);
        menu3.add(borrar);
        menu3.add(renombrarTabla);
        menu4.add(borrarTrigger);
        menu4.add(borrarProcedimiento);

        jTabbedPane1.addKeyListener(this);
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

            op.seleccionarBD(path.getPathComponent(1).toString());
            if (tipo.equalsIgnoreCase("trigger")) {
                op.ejecutarUpdate(op.getGeneradorSQL().dropTrigger(path.getPathComponent(3).toString()));
            } else {
                op.ejecutarUpdate(op.getGeneradorSQL().dropProcedure(path.getPathComponent(3).toString()));
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

    public void cargar() {
        try {
            setCursor(Cursor.WAIT_CURSOR);
            raiz = new DefaultMutableTreeNode("Bases de Datos");
            modelo_arbol = new DefaultTreeModel(raiz);
            jTree1.setModel(modelo_arbol);

            nodos = new Vector<>();
            paneles = new Vector<>();
            bases_de_datos = op.getBasesDeDatos();
            for (int i = 0; i < bases_de_datos.size(); i++) {
                DefaultMutableTreeNode nuevo = new DefaultMutableTreeNode(bases_de_datos.get(i));
                modelo_arbol.insertNodeInto(nuevo, raiz, i);
                nodos.add(nuevo);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }

    private void cargarBD(String bd) {
        setCursor(Cursor.WAIT_CURSOR);
        try {
            op.seleccionarBD(bd);
            ResultSet res2 = op.ejecutarConsulta(op.getGeneradorSQL().getTables());
            int pos2 = 0, index;
            for (index = 0; index < nodos.size(); index++) {
                if (nodos.get(index).toString().equals(bd)) {
                    break;
                }
            }
            System.out.println("tabla " + nodos.get(index).toString());

            while (res2.next()) {
                String h2 = res2.getString(1);
                modelo_arbol.insertNodeInto(new DefaultMutableTreeNode(h2), nodos.get(index), pos2);
                pos2++;
            }

            try {
                Vector<DefaultMutableTreeNode> v = op.getTriggers();
                if (v.size() > 0) {
                    DefaultMutableTreeNode triggers = new DefaultMutableTreeNode("Triggers");
                    modelo_arbol.insertNodeInto(triggers, nodos.get(index), 0);
                    for (int j = 0; j < v.size(); j++) {
                        modelo_arbol.insertNodeInto(new DefaultMutableTreeNode(v.get(j)), triggers, j);
                    }
                }
            } catch (Exception ex) {
            }
            try {
                Vector<DefaultMutableTreeNode> v2 = op.getProcedimientos(nodos.get(index).toString());
                if (v2.size() > 0) {
                    DefaultMutableTreeNode Proc = new DefaultMutableTreeNode("Procedimientos");
                    modelo_arbol.insertNodeInto(Proc, nodos.get(index), 0);
                    for (int j = 0; j < v2.size(); j++) {
                        modelo_arbol.insertNodeInto(new DefaultMutableTreeNode(v2.get(j)), Proc, j);
                    }
                }
            } catch (Exception ex) {
            }

            jTree1.expandPath(jTree1.getSelectionPath());
        } catch (Exception e) {
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
                            if (op.esConexionMysql()) {
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

                if (tp.getPathCount() == 2) {
                    Object nodo = tp.getLastPathComponent();
                    if (modelo_arbol.isLeaf(nodo)) {
//                        System.out.println("debe cargar la tabla " + tp.getPathComponent(1).toString());
                        cargarBD(tp.getPathComponent(1).toString());
                    }
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

    public void cerrarTab(Component comp) {
        jTabbedPane1.remove(comp);
//        for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {
//            if (jTabbedPane1.getTitleAt(i).equals(titulo)) {
//                jTabbedPane1.remove(i);
//            }
//        }
    }

    public int containsTabla(String bd, String tab) {
        JPanel jp;
        for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {
            jp = (JPanel) jTabbedPane1.getComponentAt(i);
            if (!jp.getName().equals(tab)) {
                continue;
            }
            if (jp instanceof panelTabla) {
                if (((panelTabla) jp).BaseDeDatos.equals(bd)) {
                    return i;
                }
            }
            if (jp instanceof panelTrigger) {
                if (((panelTrigger) jp).BaseDeDatos.equals(bd)) {
                    return i;
                }
            }
            if (jp instanceof panelProcedimiento) {
                if (((panelProcedimiento) jp).BaseDeDatos.equals(bd)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void agregarPanel(String bd, String nombre, String tipo) {
        int l = containsTabla(bd, nombre);
        if (l != -1) {
            jTabbedPane1.setSelectedIndex(l);
            return;
        }
        setCursor(Cursor.WAIT_CURSOR);
        try {
            if (!op.getBDseleccionada().equals(bd)) {
                op.seleccionarBD(bd);
            }

            JPanel pt;
            if (tipo.equals("Tabla")) {
                pt = new panelTabla(op, bd, nombre, op.ejecutarConsulta(op.getGeneradorSQL().selectRowsTable(nombre)));
            } else if (tipo.equals("TriggerNuevo")) {
                pt = new panelTrigger(op, bd, nombre, true);
            } else if (tipo.equals("Trigger")) {
                pt = new panelTrigger(op, bd, nombre, false);
            } else if (tipo.equals("ProcedimientoNuevo")) {
                pt = new panelProcedimiento(op, bd, nombre, true);
            } else {
                pt = new panelProcedimiento(op, bd, nombre, false);
            }
            pt.setName(nombre);
            jTabbedPane1.addTab(nombre, pt);
            paneles.add(pt);

            JButton tabButton = new JButton(new ImageIcon(getClass().getResource("/imagenes/cerrar.png")));
            tabButton.setPreferredSize(new Dimension(15, 15));
            tabButton.setContentAreaFilled(false);
            tabButton.setToolTipText("Cerrar");
            tabButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cerrarTab(pt);
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
            System.out.println("Error: principal: Agregar panel, SQLException");
            System.out.println(ex.getMessage() + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error inseperado", "Error", 0);
            System.out.println("Error: principal: Agregar panel, Exception");
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menuGenerarMVC = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        menuExpotarSQL = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

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

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setDividerSize(15);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseReleased(evt);
            }
        });
        jSplitPane1.setRightComponent(jTabbedPane1);

        jScrollPane1.setViewportView(jTree1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jMenu1.setText("Conexion");

        jMenuItem1.setText("Salir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Herramientas");

        menuGenerarMVC.setText("Generar MVC");
        menuGenerarMVC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuGenerarMVCActionPerformed(evt);
            }
        });
        jMenu2.add(menuGenerarMVC);

        jMenuItem2.setText("Exportar a MySQL");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        menuExpotarSQL.setText("Exportar a PostgreSQL");
        menuExpotarSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExpotarSQLActionPerformed(evt);
            }
        });
        jMenu2.add(menuExpotarSQL);

        jMenuItem3.setText("Generar Diccionario");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 208, Short.MAX_VALUE)
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
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
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

    private void jTabbedPane1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseReleased
        int destino, inicio = jTabbedPane1.getSelectedIndex();
        for (destino = 0; destino < jTabbedPane1.getTabCount(); destino++) {
            Rectangle r = jTabbedPane1.getTabComponentAt(destino).getBounds();
            if (r.contains(evt.getPoint())) {
                break;
            }
        }

        if (inicio == destino || destino == jTabbedPane1.getTabCount()) {
            return;
        }

        Component[] panels = new Component[jTabbedPane1.getTabCount()];
        Component[] tabs = new Component[jTabbedPane1.getTabCount()];
        for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {
            if (i == destino) {
                panels[inicio] = jTabbedPane1.getComponentAt(i);
                tabs[inicio] = jTabbedPane1.getTabComponentAt(i);
            } else if (i == inicio) {
                panels[destino] = jTabbedPane1.getComponentAt(i);
                tabs[destino] = jTabbedPane1.getTabComponentAt(i);
            } else {
                panels[i] = jTabbedPane1.getComponentAt(i);
                tabs[i] = jTabbedPane1.getTabComponentAt(i);
            }
        }
        jTabbedPane1.removeAll();

        for (int i = 0; i < panels.length; i++) {
            jTabbedPane1.addTab(panels[i].getName(), panels[i]);
            jTabbedPane1.setTabComponentAt(i, tabs[i]);
        }
        jTabbedPane1.setSelectedIndex(destino);
    }//GEN-LAST:event_jTabbedPane1MouseReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        salirActionPerformed(null);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void menuGenerarMVCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuGenerarMVCActionPerformed
        if (vm != null) {
            if (vm.isVisible()) {
                return;
            }
            vm.dispose();
        }
        vm = new ventanaMVC(op, bases_de_datos);
        vm.setVisible(true);
    }//GEN-LAST:event_menuGenerarMVCActionPerformed

    private void menuExpotarSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExpotarSQLActionPerformed
        TreePath path = jTree1.getSelectionPath();
        if (path == null) {
            JOptionPane.showMessageDialog(null, "Conectese a una base de datos", "Error", 0);
            return;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (path.getPathCount() < 2 || (path.getPathCount() == 2 && node.isLeaf())) {
            JOptionPane.showMessageDialog(null, "Conectese a una base de datos", "Error", 0);
            return;
        }
        JFileChooser chooser = new JFileChooser("Seleccione carpeta destino");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int seleccion = chooser.showOpenDialog(null);
        if (seleccion != JFileChooser.APPROVE_OPTION) {
            return;
        }
        setCursor(Cursor.WAIT_CURSOR);
        File fichero = chooser.getSelectedFile();

        System.out.println(op.getBDseleccionada());
        if (op.convertirPostgreSQL(fichero.getPath())) {
            JOptionPane.showMessageDialog(null, "Conversion terminada", "Exitoso", 1);
        } else {
            JOptionPane.showMessageDialog(null, "Error al exportar", "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_menuExpotarSQLActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        TreePath path = jTree1.getSelectionPath();
        if (path == null) {
            JOptionPane.showMessageDialog(null, "Conectese a una base de datos", "Error", 0);
            return;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (path.getPathCount() < 2 || (path.getPathCount() == 2 && node.isLeaf())) {
            JOptionPane.showMessageDialog(null, "Conectese a una base de datos", "Error", 0);
            return;
        }
        JFileChooser chooser = new JFileChooser("Seleccione carpeta destino");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int seleccion = chooser.showOpenDialog(null);
        if (seleccion != JFileChooser.APPROVE_OPTION) {
            return;
        }
        setCursor(Cursor.WAIT_CURSOR);
        File fichero = chooser.getSelectedFile();

        System.out.println(op.getBDseleccionada());
        
        try {
            op.convertirMySQL(fichero.getPath());
            JOptionPane.showMessageDialog(null, "Conversion terminada", "Exitoso", 1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cosultar datos", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al exportar archivo", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
        
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if (vd != null) {
            if (vd.isVisible()) {
                return;
            }
            vd.dispose();
        }
        vd = new ventanaDD(op, bases_de_datos);
        vd.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

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
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTree jTree1;
    private javax.swing.JMenuItem menuExpotarSQL;
    private javax.swing.JMenuItem menuGenerarMVC;
    private javax.swing.JButton salir;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getComponent() == jTree1) {
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

        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_W) {
            if (e.getComponent() instanceof JTabbedPane) {
                cerrarTab(jTabbedPane1.getSelectedComponent());
                //cerrarTab(jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
