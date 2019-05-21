package vistas;

import clases.Fachada;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class modificarTabla extends javax.swing.JFrame implements KeyListener {

    private final Fachada op;
    private final String tabla;
    private boolean flag = false;

    public modificarTabla(Fachada c, String table) {
        op = c;
        tabla = table;
        initComponents();
        cargar();
        setLocationRelativeTo(null);
        setTitle(tabla);
        radioDefault.addKeyListener(this);
        radioNoNulo.addKeyListener(this);
        botonBorrarColumna.addKeyListener(this);
        botonCrearColumna.addKeyListener(this);
        botonCrearForanea.addKeyListener(this);
        botonCrearLlaveUnica.addKeyListener(this);
        botonCrearPrimaria.addKeyListener(this);
        
        if (!op.esConexionMysql()) {
            labelMover.setVisible(false);
            radioDespues.setVisible(false);
            radioPrimera.setVisible(false);
            comboDespuesDe.setVisible(false);
        }
    }

    protected void cargar() {
        try {
            //cargar columnas 
            comboColumnasBorrar.removeAllItems();
            comboColumnasBorrar.addItem("------------");
            ComboColumnaForanea.removeAllItems();
            ComboColumnaForanea.addItem("------------");
            comboColumnaPrimaria.removeAllItems();
            comboColumnaPrimaria.addItem("------------");
            comboLlaveUnica.removeAllItems();
            comboLlaveUnica.addItem("------------");
            comboIncrement.removeAllItems();
            comboIncrement.addItem("------------");
            comboActualizar.removeAllItems();
            comboActualizar.addItem("------------");
            ResultSet res = op.ejecutarConsulta(op.getGeneradorSQL().GetColumnasTabla(tabla));
            String h;
            while (res.next()) {
                h = res.getString(1);
                comboColumnasBorrar.addItem(h);
                comboColumnaPrimaria.addItem(h);
                ComboColumnaForanea.addItem(h);
                comboLlaveUnica.addItem(h);
                comboIncrement.addItem(h);
                comboActualizar.addItem(h);
            }

            //cargar otras tablas
            flag = true;
            comboOtrasTablas.removeAllItems();
            comboOtrasTablas.addItem("------------");
            ResultSet rs = op.ejecutarConsulta(op.getGeneradorSQL().GetTables());
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
            op.llenarTableModel(op.ejecutarConsulta(op.getGeneradorSQL().GetColumnasTabla(tabla)), dft);
            jTable2.setModel(dft);
            addPoppupMenu(jTable2);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los\natributos de la tabla.", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado", "Error", 0);
            System.out.println("\nError: modificarTabla: cargar");
            System.out.println(ex.getMessage() + "\n");
        }
        flag = false;
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
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
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        ComboColumnaForanea = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        comboOtrasTablas = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        comboAtributosReferencia = new javax.swing.JComboBox<>();
        botonCrearForanea = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        comboColumnasBorrar = new javax.swing.JComboBox<>();
        botonBorrarColumna = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        comboColumnaPrimaria = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        botonCrearPrimaria = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        botonCrearLlaveUnica = new javax.swing.JButton();
        comboLlaveUnica = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        comboIncrement = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTabbedPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        radioDefault.setText("Default");

        radioNoNulo.setText("No nulo");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
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
                                .addComponent(textLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(botonCrearColumna))
                .addContainerGap(112, Short.MAX_VALUE))
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
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Columnas", jPanel1);

        jLabel8.setText("Crear llave foranea");

        jLabel7.setText("Atributo llave");

        ComboColumnaForanea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

        jLabel9.setText("Tabla de referencia");

        comboOtrasTablas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));
        comboOtrasTablas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboOtrasTablasActionPerformed(evt);
            }
        });

        jLabel10.setText("Atributo de referencia");

        comboAtributosReferencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

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
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(ComboColumnaForanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(comboOtrasTablas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(comboAtributosReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(botonCrearForanea))
                .addContainerGap(214, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(ComboColumnaForanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(comboOtrasTablas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(comboAtributosReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(botonCrearForanea)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Foraneas", jPanel3);

        jLabel5.setText("Borrar atributo");

        comboColumnasBorrar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

        botonBorrarColumna.setText("Borrar");
        botonBorrarColumna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBorrarColumnaActionPerformed(evt);
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
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(comboColumnasBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonBorrarColumna)))
                .addContainerGap(459, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboColumnasBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonBorrarColumna))
                .addContainerGap(118, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Borrar", jPanel4);

        comboColumnaPrimaria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

        jLabel6.setText("Crear llave primaria");

        botonCrearPrimaria.setText("Crear llave");
        botonCrearPrimaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearPrimariaActionPerformed(evt);
            }
        });

        jLabel12.setText("Crear llave unica");

        botonCrearLlaveUnica.setText("Crear llave");
        botonCrearLlaveUnica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearLlaveUnicaActionPerformed(evt);
            }
        });

        comboLlaveUnica.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

        jLabel11.setText("Crear auto_increment");

        comboIncrement.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));

        jButton2.setText("Crear llave");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(comboLlaveUnica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(botonCrearLlaveUnica))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(comboColumnaPrimaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(botonCrearPrimaria))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(comboIncrement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)))
                .addContainerGap(314, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboColumnaPrimaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(botonCrearPrimaria))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonCrearLlaveUnica)
                    .addComponent(comboLlaveUnica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(comboIncrement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Indices", jPanel2);

        jLabel13.setText("Columna a actualizar:");

        comboActualizar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------" }));
        comboActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboActualizarActionPerformed(evt);
            }
        });

        jLabel14.setText("Nombre");

        jLabel15.setText("Tipo de dato");

        comboNuevoTipoDato.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-------------", "SMALLINT", "INT", "BIGINT", "FLOAT", "DOUBLE", "DECIMAL", "CHAR", "VARCHAR", "TEXT", "DATE", "TIME", "YEAR", "DATETIME", "TIMESTAMP", "BYNARY", "BLOB", "LONGBLOB" }));

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
                                        .addComponent(TextNuevoLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(112, Short.MAX_VALUE))))
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
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BotonActualizar)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Actualizar", jPanel5);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(249, 249, 249)
                        .addComponent(jButton3))
                    .addComponent(jTabbedPane1)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
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
            op.ejecutarUpdate(op.getGeneradorSQL().agregarColumnaTabla(tabla, comboTiposDatos.getSelectedItem().toString(),
                    textNombreColumna.getText(), lon, Def, radioNoNulo.isSelected()));
            cargar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error verifique los datos", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_botonCrearColumnaActionPerformed

    private void botonBorrarColumnaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBorrarColumnaActionPerformed
        if (comboColumnasBorrar.getSelectedIndex() == 0) {
            return;
        }

        setCursor(Cursor.WAIT_CURSOR);
        try {
            op.ejecutarUpdate(op.getGeneradorSQL().borrarColumnaTabla(tabla, comboColumnasBorrar.getSelectedItem().toString()));
            cargar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar", "Error", 0);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_botonBorrarColumnaActionPerformed

    private void botonCrearPrimariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearPrimariaActionPerformed
        if (comboColumnaPrimaria.getSelectedIndex() == 0) {
            return;
        }

        setCursor(Cursor.WAIT_CURSOR);
        try {
            op.ejecutarUpdate(op.getGeneradorSQL().crearLlavePrimaria(tabla, comboColumnaPrimaria.getSelectedItem().toString()));
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
            op.ejecutarUpdate(op.getGeneradorSQL().crearLlaveForanea(tabla, ComboColumnaForanea.getSelectedItem().toString(),
                    comboOtrasTablas.getSelectedItem().toString(), comboAtributosReferencia.getSelectedItem().toString()));
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
            ResultSet rs = op.ejecutarConsulta(op.getGeneradorSQL().GetColumnasTabla(
                    comboOtrasTablas.getSelectedItem().toString()));
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

    private void botonCrearLlaveUnicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearLlaveUnicaActionPerformed
        if (comboLlaveUnica.getSelectedIndex() == 0) {
            return;
        }

        setCursor(Cursor.WAIT_CURSOR);
        try {
            op.ejecutarUpdate(op.getGeneradorSQL().crearLlaveUnique(tabla, comboLlaveUnica.getSelectedItem().toString()));
            cargar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al crear\nllave unica", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_botonCrearLlaveUnicaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        op.mostrarConsola();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (comboIncrement.getSelectedIndex() == 0) {
            return;
        }

        setCursor(Cursor.WAIT_CURSOR);
        try {
            op.ejecutarUpdate(op.getGeneradorSQL().CrearAuto_increment(tabla, comboIncrement.getSelectedItem().toString(),
                    jTable2.getValueAt(comboIncrement.getSelectedIndex() - 1, 1).toString()));
            cargar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al crear\nauto_increment", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_jButton2ActionPerformed

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
        }else{
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
                String despues = (radioDespues.isSelected())? comboDespuesDe.getSelectedItem().toString() : null;
                op.actualizarAtributoMysql(tabla, comboActualizar.getSelectedItem().toString(),
                        comboNuevoTipoDato.getSelectedItem().toString(), textNuevoNombre.getText(), TextNuevoLongitud.getText(),
                        def, RadioNuevoNull.isSelected(), radioPrimera.isSelected(), despues);
            } else {
                op.ejecutarUpdate(op.getGeneradorSQL().actualizarAtributo(tabla, comboActualizar.getSelectedItem().toString(),
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

    private void radioPrimeraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioPrimeraActionPerformed
        if (radioPrimera.isSelected()) {
            radioDespues.setSelected(false);
        }
    }//GEN-LAST:event_radioPrimeraActionPerformed

    private void radioDespuesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioDespuesActionPerformed
        if (radioDespues.isSelected()) {
            radioPrimera.setSelected(false);
        }
    }//GEN-LAST:event_radioDespuesActionPerformed

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
    private javax.swing.JButton BotonActualizar;
    private javax.swing.JComboBox<String> ComboColumnaForanea;
    private javax.swing.JRadioButton RadioNuevoDefault;
    private javax.swing.JRadioButton RadioNuevoNull;
    private javax.swing.JTextField TextNuevoDefault;
    private javax.swing.JTextField TextNuevoLongitud;
    private javax.swing.JButton botonBorrarColumna;
    private javax.swing.JButton botonCrearColumna;
    private javax.swing.JButton botonCrearForanea;
    private javax.swing.JButton botonCrearLlaveUnica;
    private javax.swing.JButton botonCrearPrimaria;
    private javax.swing.JComboBox<String> comboActualizar;
    private javax.swing.JComboBox<String> comboAtributosReferencia;
    private javax.swing.JComboBox<String> comboColumnaPrimaria;
    private javax.swing.JComboBox<String> comboColumnasBorrar;
    private javax.swing.JComboBox<String> comboDespuesDe;
    private javax.swing.JComboBox<String> comboIncrement;
    private javax.swing.JComboBox<String> comboLlaveUnica;
    private javax.swing.JComboBox<String> comboNuevoTipoDato;
    private javax.swing.JComboBox<String> comboOtrasTablas;
    private javax.swing.JComboBox<String> comboTiposDatos;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
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
            if (e.getComponent() == botonCrearLlaveUnica) {
                botonCrearLlaveUnicaActionPerformed(null);
                return;
            }
            if (e.getComponent() == botonCrearPrimaria) {
                botonCrearPrimariaActionPerformed(null);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
