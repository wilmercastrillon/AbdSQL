package vistas;

import ExtraComponents.JCheckList;
import clases.Fachada;
import conexionBD.Conexion;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class modificarTabla extends javax.swing.JFrame implements KeyListener {

    private final Fachada op;
    private final String BD, tabla;
    private boolean flag = false;
    private GeneradorSQL.GeneradorSQL generador;

    public modificarTabla(Fachada c, String base_de_datos, String table) {
        op = c;
        BD = base_de_datos;
        tabla = table;
        generador = op.getGeneradorSQL();
        initComponents();
        cargar();
        cargarTiposDatos();
        cargarForaneas();
        cargarIndices();

        setLocationRelativeTo(null);
        setTitle(tabla);
        radioDefault.addKeyListener(this);
        radioNoNulo.addKeyListener(this);
        botonBorrarColumna.addKeyListener(this);
        botonCrearColumna.addKeyListener(this);
        botonCrearForanea.addKeyListener(this);
        jButtonCrearIndice.addKeyListener(this);
        jListColumnas.setEnabled(false);

        if (!op.esConexionMysql()) {
            labelMover.setVisible(false);
            radioDespues.setVisible(false);
            radioPrimera.setVisible(false);
            comboDespuesDe.setVisible(false);
        }

        int x, y;
        int nuevoAlto = getHeight() + ((int) (getHeight() * 0.25));
        int nuevoAncho = getWidth() + ((int) (getWidth() * 0.25));
        Dimension screenSize;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        x = (int) (screenSize.width - nuevoAncho) / 2;
        y = (int) (screenSize.height - nuevoAlto) / 2;
        setMaximizedBounds(new Rectangle(x, y, nuevoAncho, nuevoAlto));
    }

    protected void cargar() {
        try {
            //cargar columnas 
            comboColumnasBorrar.removeAllItems();
            comboColumnasBorrar.addItem("------------");
            ComboColumnaForanea.removeAllItems();
            ComboColumnaForanea.addItem("------------");
            comboActualizar.removeAllItems();
            comboActualizar.addItem("------------");
            JCheckList listaCheck = (JCheckList) jListColumnas;
            listaCheck.removeAll();

            ResultSet res = op.ejecutarConsulta(op.getGeneradorSQL().getColumnsTable(tabla));
            String h;
            while (res.next()) {
                h = res.getString(1);
                comboColumnasBorrar.addItem(h);
                ComboColumnaForanea.addItem(h);
                comboActualizar.addItem(h);
                listaCheck.addCheck(h);
            }

            //cargar otras tablas
            flag = true;
            comboOtrasTablas.removeAllItems();
            comboOtrasTablas.addItem("------------");
            ResultSet rs = op.ejecutarConsulta(op.getGeneradorSQL().getTables());
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
            op.llenarTableModel(op.ejecutarConsulta(op.getGeneradorSQL().getColumnsTable(tabla)), dft);
            jTable2.setModel(dft);
            addPoppupMenu(jTable2);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los\natributos de la tabla.", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
            System.err.println("Error: modificarTabla, cargar, SQLException");
            System.err.println("Mensaje: " + e.getMessage() + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado", "Error", 0);
            System.err.println("Error: modificarTabla, cargar, Exception");
            System.err.println("Mensaje: " + ex.getMessage() + "\n");
        }
        flag = false;
    }

    private void cargarTiposDatos() {
        ArrayList<String> tipos;
        if (op.getConexion().tipo == Conexion.MySQL) {
            tipos = op.getMysqlDataTypes();
        } else if (op.getConexion().tipo == Conexion.PostgreSQL) {
            tipos = op.getPostgresDataTypes();
        } else if (op.getConexion().tipo == Conexion.Oracle) {
            tipos = op.getOracleDataTypes();
        } else {
            tipos = op.getSQLiteDataTypes();
        }
        comboTiposDatos.removeAllItems();
        comboNuevoTipoDato.removeAllItems();
        comboTiposDatos.addItem("------------");
        comboNuevoTipoDato.addItem("------------");
        for (int i = 0; i < tipos.size(); i++) {
            comboTiposDatos.addItem(tipos.get(i));
            comboNuevoTipoDato.addItem(tipos.get(i));
        }
    }

    private void cargarForaneas() {
        try {
            jComboForaneas.removeAllItems();
            jComboForaneas.addItem("------------");
            ResultSet rs3 = op.ejecutarConsulta(op.getGeneradorSQL().getForeignKeys(BD, tabla));
            while (rs3.next()) {
                jComboForaneas.addItem(rs3.getString("constraint"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar las\nllaves foraneas de la tabla.", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
            System.err.println("Error: vistas, modificarTabla, cargarForaneas");
            System.err.println("Mensaje: " + ex.getMessage() + "\n");
        }
    }

    private void cargarIndices() {
        try {
            jComboIndices.removeAllItems();
            jComboIndices.addItem("------------");
            ResultSet rs4 = op.ejecutarConsulta(op.getGeneradorSQL().getIndexs(BD, tabla));
            while (rs4.next()) {
                jComboIndices.addItem(rs4.getString("indice"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar los\nindices de la tabla.", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
            System.err.println("Error: vistas, modificarTabla, cargarIndices");
            System.err.println("Mensaje: " + ex.getMessage() + "\n");
        }
    }

    private void addPoppupMenu(JTable jt) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuitem = new JMenuItem("Mover fila");
        menuitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    int pos = Integer.parseInt(JOptionPane.showInputDialog(null, "Mover a posicion"));

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Numero de posicion no valido", "Error", 0);
                }
            }
        });
        menu.add(menuitem);
        jt.setComponentPopupMenu(menu);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel6 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        PanelTabColumnas = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        radioDefault = new javax.swing.JRadioButton();
        textDefault = new javax.swing.JTextField();
        radioNoNulo = new javax.swing.JRadioButton();
        botonCrearColumna = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        textNombreColumna = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        comboTiposDatos = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        textLongitud = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        comboColumnasBorrar = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        botonBorrarColumna = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jCheckNombreForanea = new javax.swing.JCheckBox();
        jTextNombreForanea = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        comboAtributosReferencia = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        comboOtrasTablas = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        ComboColumnaForanea = new javax.swing.JComboBox<>();
        botonCrearForanea = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jComboForaneas = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jTextNombreConstraint = new javax.swing.JTextField();
        jTextAtributollave = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextTablaReferencia = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTextAtributoReferencia = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        JPindicesCheck = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListColumnas = jListColumnas = new JCheckList();
        ;
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jComboTipoIndice = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jTextNombreIndice = new javax.swing.JTextField();
        jButtonCrearIndice = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jComboIndices = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        comboActualizar = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        textNuevoNombre = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        comboNuevoTipoDato = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        TextNuevoLongitud = new javax.swing.JTextField();
        RadioNuevoDefault = new javax.swing.JRadioButton();
        TextNuevoDefault = new javax.swing.JTextField();
        RadioNuevoNull = new javax.swing.JRadioButton();
        BotonActualizar = new javax.swing.JButton();
        labelMover = new javax.swing.JLabel();
        radioPrimera = new javax.swing.JRadioButton();
        radioDespues = new javax.swing.JRadioButton();
        comboDespuesDe = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jMenuItem1.setText("jMenuItem1");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jToggleButton1.setText("jToggleButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        PanelTabColumnas.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelTabColumnas.addTab("Columnas", jPanel7);

        radioDefault.setText("Default");

        radioNoNulo.setText("No nulo");

        botonCrearColumna.setText("Crear columna");
        botonCrearColumna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearColumnaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Nuevo atributo");

        jLabel2.setText("Nombre");

        jLabel3.setText("Tipo de dato");

        comboTiposDatos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-------------" }));

        jLabel4.setText("Longitud");

        comboColumnasBorrar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setText("Borrar atributo");

        botonBorrarColumna.setText("Borrar");
        botonBorrarColumna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBorrarColumnaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(textNombreColumna, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(radioDefault)
                                        .addGap(18, 18, 18)
                                        .addComponent(textDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(comboTiposDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(radioNoNulo)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18)
                                        .addComponent(textLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(botonCrearColumna)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(comboColumnasBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(botonBorrarColumna))
                            .addComponent(jLabel17))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textNombreColumna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(comboTiposDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioDefault)
                    .addComponent(textDefault, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radioNoNulo)
                    .addComponent(jLabel4)
                    .addComponent(textLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(botonCrearColumna)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboColumnasBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonBorrarColumna))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelTabColumnas.addTab("Editar atributos", jPanel1);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Crear llave foranea");

        jCheckNombreForanea.setText("Agregar nombre");

        jLabel10.setText("Atributo de referencia");

        comboAtributosReferencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

        jLabel9.setText("Tabla de referencia");

        comboOtrasTablas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));
        comboOtrasTablas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboOtrasTablasActionPerformed(evt);
            }
        });

        jLabel7.setText("Atributo llave");

        ComboColumnaForanea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

        botonCrearForanea.setText("Crear llave");
        botonCrearForanea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearForaneaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7)
                            .addComponent(jCheckNombreForanea))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ComboColumnaForanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboOtrasTablas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboAtributosReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextNombreForanea, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(botonCrearForanea))
                .addContainerGap(304, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckNombreForanea)
                    .addComponent(jTextNombreForanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(ComboColumnaForanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(comboOtrasTablas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(comboAtributosReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(botonCrearForanea)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelTabColumnas.addTab("Crear foraneas", jPanel3);

        jLabel5.setText("Seleccione la llave foranea");

        jComboForaneas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));
        jComboForaneas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboForaneasActionPerformed(evt);
            }
        });

        jLabel18.setText("Nombre constraint");

        jTextNombreConstraint.setEnabled(false);

        jTextAtributollave.setEnabled(false);

        jLabel19.setText("Atributo llave");

        jTextTablaReferencia.setEnabled(false);

        jLabel20.setText("Tabla de referencia");

        jLabel21.setText("Atributo de referencia");

        jTextAtributoReferencia.setEnabled(false);

        jButton4.setText("Borrar llave foranea");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jComboForaneas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jTextNombreConstraint, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel19)
                                .addComponent(jTextAtributollave, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextTablaReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel21)
                                .addComponent(jTextAtributoReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jComboForaneas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextAtributollave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextTablaReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextAtributoReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextNombreConstraint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        PanelTabColumnas.addTab("Borrar foraneas", jPanel4);

        JPindicesCheck.setBackground(new java.awt.Color(255, 255, 255));
        JPindicesCheck.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane1.setViewportView(jListColumnas);

        javax.swing.GroupLayout JPindicesCheckLayout = new javax.swing.GroupLayout(JPindicesCheck);
        JPindicesCheck.setLayout(JPindicesCheckLayout);
        JPindicesCheckLayout.setHorizontalGroup(
            JPindicesCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        JPindicesCheckLayout.setVerticalGroup(
            JPindicesCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel24.setText("Tipo de indice");

        jComboTipoIndice.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------", "Llave primaria", "Llave unica", "auto_increment" }));
        jComboTipoIndice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboTipoIndiceActionPerformed(evt);
            }
        });

        jLabel11.setText("Nombre del indice");

        jButtonCrearIndice.setText("Crear indice");
        jButtonCrearIndice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCrearIndiceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel24)
                    .addComponent(jLabel11)
                    .addComponent(jTextNombreIndice, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jButtonCrearIndice)
                    .addComponent(jComboTipoIndice, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboTipoIndice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextNombreIndice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonCrearIndice)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Crear indice", jPanel8);

        jLabel12.setText("Nombre de indice");

        jComboIndices.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));
        jComboIndices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboIndicesActionPerformed(evt);
            }
        });

        jButton5.setText("Borrar indice");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jButton5)
                    .addComponent(jComboIndices, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboIndices, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addContainerGap(106, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Borrar indice", jPanel9);

        jLabel6.setText("Columnas del indice");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 217, Short.MAX_VALUE))
                    .addComponent(JPindicesCheck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JPindicesCheck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );

        PanelTabColumnas.addTab("Indices", jPanel2);

        jLabel13.setText("Columna a actualizar:");

        comboActualizar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));
        comboActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboActualizarActionPerformed(evt);
            }
        });

        jLabel14.setText("Nombre");

        jLabel15.setText("Tipo de dato");

        comboNuevoTipoDato.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-------------" }));

        jLabel16.setText("Longitud");

        RadioNuevoDefault.setText("Default");

        RadioNuevoNull.setText("No nulo");

        BotonActualizar.setText("Actualizar");
        BotonActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonActualizarActionPerformed(evt);
            }
        });

        labelMover.setText("Mover columna:");

        radioPrimera.setText("Primera");
        radioPrimera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioPrimeraActionPerformed(evt);
            }
        });

        radioDespues.setText("Despues de:");
        radioDespues.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioDespuesActionPerformed(evt);
            }
        });

        comboDespuesDe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-----------" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(labelMover)
                        .addGap(18, 18, 18)
                        .addComponent(radioPrimera)
                        .addGap(18, 18, 18)
                        .addComponent(radioDespues)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(comboDespuesDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BotonActualizar)
                                .addContainerGap())))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(comboActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                        .addComponent(RadioNuevoDefault)
                                        .addGap(18, 18, 18)
                                        .addComponent(TextNuevoDefault))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addGap(18, 18, 18)
                                        .addComponent(textNuevoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addGap(18, 18, 18)
                                        .addComponent(comboNuevoTipoDato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(RadioNuevoNull)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel16)
                                        .addGap(18, 18, 18)
                                        .addComponent(TextNuevoLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(comboActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(textNuevoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(comboNuevoTipoDato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RadioNuevoDefault)
                    .addComponent(TextNuevoDefault, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RadioNuevoNull)
                    .addComponent(jLabel16)
                    .addComponent(TextNuevoLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMover)
                    .addComponent(radioPrimera)
                    .addComponent(radioDespues)
                    .addComponent(comboDespuesDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(106, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BotonActualizar)
                .addContainerGap())
        );

        PanelTabColumnas.addTab("Actualizar columnas", jPanel5);

        jButton3.setText("Salir");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setText("Consola");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Recargar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelTabColumnas, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelTabColumnas)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        op.mostrarConsola();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void radioDespuesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioDespuesActionPerformed
        if (radioDespues.isSelected()) {
            radioPrimera.setSelected(false);
        }
    }//GEN-LAST:event_radioDespuesActionPerformed

    private void radioPrimeraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioPrimeraActionPerformed
        if (radioPrimera.isSelected()) {
            radioDespues.setSelected(false);
        }
    }//GEN-LAST:event_radioPrimeraActionPerformed

    private void BotonActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonActualizarActionPerformed
        if (comboActualizar.getSelectedIndex() < 1) {
            return;
        }

        setCursor(Cursor.WAIT_CURSOR);
        try {
            String def = null;
            if (RadioNuevoDefault.isSelected()) {
                def = TextNuevoDefault.getText();
            }
            if (op.esConexionMysql()) {
                String despues = (radioDespues.isSelected()) ? comboDespuesDe.getSelectedItem().toString() : null;
                op.actualizarAtributoMysql(tabla, comboActualizar.getSelectedItem().toString(),
                        comboNuevoTipoDato.getSelectedItem().toString(), textNuevoNombre.getText(), TextNuevoLongitud.getText(),
                        def, RadioNuevoNull.isSelected(), radioPrimera.isSelected(), despues);
            } else {
                op.ejecutarUpdate(op.getGeneradorSQL().updateColumn(tabla, comboActualizar.getSelectedItem().toString(),
                        comboNuevoTipoDato.getSelectedItem().toString(), textNuevoNombre.getText(), TextNuevoLongitud.getText(),
                        def, RadioNuevoNull.isSelected()));
            }
            cargar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al crear\nactualizar", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_BotonActualizarActionPerformed

    private void comboActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboActualizarActionPerformed
        //nombre, tipo, default y nulo.
        if (comboActualizar.getSelectedIndex() < 1) {
            return;
        }
        Vector<String> v = op.getDatosColumna(jTable2, comboActualizar.getSelectedItem().toString());
        textNuevoNombre.setText(v.get(0));
        for (int i = 1; i < comboNuevoTipoDato.getItemCount(); i++) {
            if (v.get(1).equals(comboNuevoTipoDato.getItemAt(i))) {
                comboNuevoTipoDato.setSelectedIndex(i);
                break;
            }
        }
        if (v.get(2) != null) {
            RadioNuevoDefault.setSelected(true);
            TextNuevoDefault.setText(v.get(2));
        } else {
            RadioNuevoDefault.setSelected(false);
            TextNuevoDefault.setText("");
        }
        RadioNuevoNull.setSelected(!v.get(3).equalsIgnoreCase("si"));
        TextNuevoLongitud.setText(v.get(4));

        comboDespuesDe.removeAllItems();
        for (int i = 0; i < comboActualizar.getItemCount(); i++) {
            if (i != comboActualizar.getSelectedIndex()) {
                comboDespuesDe.addItem(comboActualizar.getItemAt(i));
            }
        }
    }//GEN-LAST:event_comboActualizarActionPerformed

    private void botonCrearForaneaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearForaneaActionPerformed
        if (comboOtrasTablas.getSelectedIndex() == 0 || comboAtributosReferencia.getSelectedIndex() == 0
                || ComboColumnaForanea.getSelectedIndex() == 0) {
            return;
        }

        setCursor(Cursor.WAIT_CURSOR);
        try {
            if (jCheckNombreForanea.isSelected()) {
                op.ejecutarUpdate(op.getGeneradorSQL().addForeignKey(tabla,
                        ComboColumnaForanea.getSelectedItem().toString(), comboOtrasTablas.getSelectedItem().toString(),
                        comboAtributosReferencia.getSelectedItem().toString(), jTextNombreForanea.getText()));
            } else {
                op.ejecutarUpdate(op.getGeneradorSQL().addForeignKey(tabla, ComboColumnaForanea.getSelectedItem().toString(),
                        comboOtrasTablas.getSelectedItem().toString(), comboAtributosReferencia.getSelectedItem().toString()));
            }
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
        comboAtributosReferencia.removeAllItems();
        comboAtributosReferencia.addItem("------------");
        if (comboOtrasTablas.getSelectedIndex() == 0) {
            return;
        }

        try {
            ResultSet rs = op.ejecutarConsulta(op.getGeneradorSQL().getColumnsTable(
                    comboOtrasTablas.getSelectedItem().toString()));
            while (rs.next()) {
                comboAtributosReferencia.addItem(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los\natributos de la tabla.", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado", "Error", 0);
            System.err.println("Error: modificarTabla: comboOtrasTablasActionPerformed, Exception");
            System.err.println(e.getMessage() + "\n");
        }
    }//GEN-LAST:event_comboOtrasTablasActionPerformed

    private void botonBorrarColumnaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBorrarColumnaActionPerformed
        if (comboColumnasBorrar.getSelectedIndex() == 0) {
            return;
        }

        setCursor(Cursor.WAIT_CURSOR);
        try {
            op.ejecutarUpdate(op.getGeneradorSQL().dropColumnTable(tabla, comboColumnasBorrar.getSelectedItem().toString()));
            cargar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_botonBorrarColumnaActionPerformed

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
            op.ejecutarUpdate(op.getGeneradorSQL().addColumnTable(tabla, comboTiposDatos.getSelectedItem().toString(),
                    textNombreColumna.getText(), lon, Def, radioNoNulo.isSelected()));
            cargar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error verifique los datos", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_botonCrearColumnaActionPerformed

    private void jComboForaneasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboForaneasActionPerformed
        try {
            if (jComboForaneas.getSelectedIndex() < 1) {
                return;
            }
            ResultSet rs = op.ejecutarConsulta(op.getGeneradorSQL().getForeignKey(BD, tabla,
                    jComboForaneas.getSelectedItem().toString()));

            rs.next();
            jTextNombreConstraint.setText(rs.getString("constraint"));
            jTextAtributollave.setText(rs.getString("columna"));
            jTextTablaReferencia.setText(rs.getString("tabla_referencia"));
            jTextAtributoReferencia.setText(rs.getString("columna_referencia"));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
    }//GEN-LAST:event_jComboForaneasActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (jComboForaneas.getSelectedIndex() < 1) {
            JOptionPane.showMessageDialog(null, "Seleccione el constraint", "Error", 1);
            return;
        }

        setCursor(Cursor.WAIT_CURSOR);
        try {
            op.ejecutarUpdate(op.getGeneradorSQL().dropForeignKey(tabla, jComboForaneas.getSelectedItem().toString()));
            cargar();

            jTextAtributollave.setText("");
            jTextTablaReferencia.setText("");
            jTextAtributoReferencia.setText("");
            jTextNombreConstraint.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar base de datos", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
            System.err.println("Error: modificarTabla, jButton4ActionPerformed, SQLException");
            System.err.println(ex.getMessage() + "\n");
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (jComboIndices.getSelectedIndex() < 1) {
            JOptionPane.showMessageDialog(null, "Seleccione el constraint", "Error", 2);
            return;
        }

        setCursor(Cursor.WAIT_CURSOR);
        try {
            op.ejecutarUpdate(op.getGeneradorSQL().dropIndex(tabla, jComboIndices.getSelectedItem().toString()));
            cargar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar base de datos", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
            System.err.println("Error: modificarTabla, jButton5ActionPerformed, SQLException");
            System.err.println(ex.getMessage() + "\n");
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButtonCrearIndiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCrearIndiceActionPerformed
        JCheckList listaCheck = (JCheckList) jListColumnas;
        ArrayList<String> lista = listaCheck.getCheckedItems();
        ArrayList<Integer> listaIdx = listaCheck.getCheckedPositions();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione la columna", "Error", 2);
            return;
        } else if (jComboTipoIndice.getSelectedIndex() < 1) {
            JOptionPane.showMessageDialog(null, "Seleccione el tipo de indice", "Error", 2);
            return;
        }
        setCursor(Cursor.WAIT_CURSOR);

        try {
            String comando;
            switch (jComboTipoIndice.getSelectedIndex()) {
                case 1:
                    comando = op.getGeneradorSQL().addPrimaryKey(tabla, lista, jTextNombreIndice.getText());
                    break;
                case 2:
                    comando = op.getGeneradorSQL().addUniqueKey(tabla, lista, jTextNombreIndice.getText());
                    break;
                default:
                    if (lista.size() != 1) {
                        JOptionPane.showMessageDialog(null, "El autoincrement es para una columna", "Error", 2);
                        setCursor(Cursor.DEFAULT_CURSOR);
                        return;
                    }
                    comando = op.getGeneradorSQL().addAuto_increment(tabla, lista.get(0),
                            jTable2.getValueAt(listaIdx.get(0), 1).toString());
                    break;
            }

            op.ejecutarUpdate(comando);
            cargar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al crear el indice", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_jButtonCrearIndiceActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        setCursor(Cursor.WAIT_CURSOR);
        cargar();
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboTipoIndiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboTipoIndiceActionPerformed
        if (jComboTipoIndice.getSelectedIndex() > 0) {
            JPindicesCheck.setEnabled(true);
            jListColumnas.setEnabled(true);
            JCheckList listaCheck = (JCheckList) jListColumnas;
            listaCheck.removeAllCheck();
            jComboIndices.setSelectedIndex(0);
        }
    }//GEN-LAST:event_jComboTipoIndiceActionPerformed

    private void jComboIndicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboIndicesActionPerformed
        try {
            if (jComboIndices.getSelectedIndex() > 0) {
                JPindicesCheck.setEnabled(false);
                jListColumnas.setEnabled(true);

                JCheckList listaCheck = (JCheckList) jListColumnas;
                listaCheck.removeAllCheck();
                ResultSet rs = op.ejecutarConsulta(generador.getColumsConstraint(
                        BD, tabla, jComboIndices.getSelectedItem().toString()));
                while (rs.next()) {
                    String item = rs.getString("column");
                    listaCheck.setCheckedAt(item, true);
                }
                jListColumnas.setEnabled(false);
                jComboTipoIndice.setSelectedIndex(0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(modificarTabla.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jComboIndicesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonActualizar;
    private javax.swing.JComboBox<String> ComboColumnaForanea;
    private javax.swing.JPanel JPindicesCheck;
    private javax.swing.JTabbedPane PanelTabColumnas;
    private javax.swing.JRadioButton RadioNuevoDefault;
    private javax.swing.JRadioButton RadioNuevoNull;
    private javax.swing.JTextField TextNuevoDefault;
    private javax.swing.JTextField TextNuevoLongitud;
    private javax.swing.JButton botonBorrarColumna;
    private javax.swing.JButton botonCrearColumna;
    private javax.swing.JButton botonCrearForanea;
    private javax.swing.JComboBox<String> comboActualizar;
    private javax.swing.JComboBox<String> comboAtributosReferencia;
    private javax.swing.JComboBox<String> comboColumnasBorrar;
    private javax.swing.JComboBox<String> comboDespuesDe;
    private javax.swing.JComboBox<String> comboNuevoTipoDato;
    private javax.swing.JComboBox<String> comboOtrasTablas;
    private javax.swing.JComboBox<String> comboTiposDatos;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonCrearIndice;
    private javax.swing.JCheckBox jCheckNombreForanea;
    private javax.swing.JComboBox<String> jComboForaneas;
    private javax.swing.JComboBox<String> jComboIndices;
    private javax.swing.JComboBox<String> jComboTipoIndice;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jListColumnas;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextAtributoReferencia;
    private javax.swing.JTextField jTextAtributollave;
    private javax.swing.JTextField jTextNombreConstraint;
    private javax.swing.JTextField jTextNombreForanea;
    private javax.swing.JTextField jTextNombreIndice;
    private javax.swing.JTextField jTextTablaReferencia;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel labelMover;
    private javax.swing.JRadioButton radioDefault;
    private javax.swing.JRadioButton radioDespues;
    private javax.swing.JRadioButton radioNoNulo;
    private javax.swing.JRadioButton radioPrimera;
    private javax.swing.JTextField textDefault;
    private javax.swing.JTextField textLongitud;
    private javax.swing.JTextField textNombreColumna;
    private javax.swing.JTextField textNuevoNombre;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (e.getComponent() == radioDefault) {
                radioDefault.setSelected(!radioDefault.isSelected());
                return;
            }
            if (e.getComponent() == radioNoNulo) {
                radioNoNulo.setSelected(!radioNoNulo.isSelected());
                return;
            }
            if (e.getComponent() == botonBorrarColumna) {
                botonBorrarColumnaActionPerformed(null);
                return;
            }
            if (e.getComponent() == botonCrearColumna) {
                botonCrearColumnaActionPerformed(null);
                return;
            }
            if (e.getComponent() == botonCrearForanea) {
                botonCrearForaneaActionPerformed(null);
                return;
            }
            if (e.getComponent() == jButtonCrearIndice) {
                jButtonCrearIndiceActionPerformed(null);
                return;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
