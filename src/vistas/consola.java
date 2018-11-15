package vistas;

import clases.Conexion;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.Element;

public class consola extends javax.swing.JFrame implements KeyListener {

    private DefaultListModel<String> modelo_lista;
    private Conexion x4;
    private JPopupMenu menu, menu2, menu3;
    private JTextArea numeros, comando;
    private JScrollPane scroll;

    public consola(Conexion q) {
        x4 = q;
        initComponents();
        modelo_lista = new DefaultListModel<>();
        jList1.setModel(modelo_lista);
        contadorLineas();

        menu = new JPopupMenu();
        menu2 = new JPopupMenu();
        menu3 = new JPopupMenu();

        JMenuItem item = new JMenuItem("Limpiar consola");
        item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                limpiar();
//                System.out.println("index seleccionado: " + jList1.getSelectedIndex());
            }
        });
        JMenuItem item2 = new JMenuItem("copiar");
        item2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent ae) {
//                System.out.println("index seleccionado: " + jList1.getSelectedIndex());
                if (jList1.getSelectedIndex() != -1) {
                    String texto = modelo_lista.get(jList1.getSelectedIndex());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    StringSelection ss = new StringSelection(texto.substring(texto.indexOf(" ") + 1));
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
                }
            }
        });

        JMenuItem item3 = new JMenuItem("pegar");
        item3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                pegar(comando);
            }
        });
        menu.add(item2);
        menu.add(item);
        jList1.setComponentPopupMenu(menu);
        menu2.add(item3);
        comando.setComponentPopupMenu(menu2);

        setTitle("consola");
    }

    private void contadorLineas() {
        comando = new JTextArea();
        numeros = new JTextArea("1");
        numeros.setBackground(Color.LIGHT_GRAY);
        numeros.setEditable(false);
        scroll = new JScrollPane(comando);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setRowHeaderView(numeros);
        panelComandos.setLayout((LayoutManager) new BoxLayout(panelComandos, BoxLayout.Y_AXIS));
        panelComandos.add(scroll);
        comando.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                updateLineNumbers();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                updateLineNumbers();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                updateLineNumbers();
            }
        });
    }

    public void updateLineNumbers() {
        String lineNumbersText = getLineNumbersText();
        numeros.setText(lineNumbersText);
    }

    private String getLineNumbersText() {
        int caretPosition = comando.getDocument().getLength();
        Element root = comando.getDocument().getDefaultRootElement();
        StringBuilder lineNumbersTextBuilder = new StringBuilder();
        lineNumbersTextBuilder.append("1").append(System.lineSeparator());
        int max = root.getElementIndex(caretPosition) + 2;

        for (int elementIndex = 2; elementIndex < max; elementIndex++) {
            lineNumbersTextBuilder.append(elementIndex).append(System.lineSeparator());
        }

        return lineNumbersTextBuilder.toString();
    }

    private void pegar(JTextArea jtex) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = clipboard.getContents(null);

        if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                jtex.setText("" + trans.getTransferData(DataFlavor.stringFlavor));
            } catch (Exception ex) {
            }
        }
    }

    public void agregar(String c) {
        modelo_lista.addElement((modelo_lista.getSize() + 1) + "|  " + c);
    }

    public void limpiar() {
        modelo_lista.clear();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jTable1 = new javax.swing.JTable();
        panelComandos = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        RadioAjustar = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel4.setText("Ingrese comando o consulta");

        jSplitPane1.setDividerLocation(125);
        jSplitPane1.setDividerSize(15);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

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
        jScrollPane2.setViewportView(jTable1);

        jSplitPane1.setRightComponent(jScrollPane2);

        javax.swing.GroupLayout panelComandosLayout = new javax.swing.GroupLayout(panelComandos);
        panelComandos.setLayout(panelComandosLayout);
        panelComandosLayout.setHorizontalGroup(
            panelComandosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 593, Short.MAX_VALUE)
        );
        panelComandosLayout.setVerticalGroup(
            panelComandosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(panelComandos);

        jButton2.setText("Ejecutar Update");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Ejecutar Consulta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        RadioAjustar.setText("Ajustar tabla");
        RadioAjustar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioAjustarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(RadioAjustar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(RadioAjustar))
                .addGap(18, 18, 18)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Consulta", jPanel1);

        jScrollPane1.setViewportView(jList1);

        jLabel1.setText("Historial de comandos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Comandos ejecutados", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setCursor(Cursor.WAIT_CURSOR);
        try {
            ResultSet res = x4.EjecutarConsulta(comando.getText());
            ResultSetMetaData rsmd = res.getMetaData();

            DefaultTableModel modelo = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

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
            JOptionPane.showMessageDialog(null, "Error en el comando", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String comando = this.comando.getText();
        try {
            x4.EjecutarUpdate(comando);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en el comando", "Error", 0);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 0);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void RadioAjustarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioAjustarActionPerformed
        if (RadioAjustar.isSelected()) {
            jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        } else {
            jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        }
    }//GEN-LAST:event_RadioAjustarActionPerformed

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
            java.util.logging.Logger.getLogger(consola.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(consola.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(consola.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(consola.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new consola().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton RadioAjustar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel panelComandos;
    // End of variables declaration//GEN-END:variables

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (e.getComponent() == jButton1) {
                jButton1ActionPerformed(null);
            }
            if (e.getComponent() == jButton2) {
                jButton2ActionPerformed(null);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
