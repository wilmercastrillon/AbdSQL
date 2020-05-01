package vistas;

import clases.*;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

public class inicio extends javax.swing.JFrame implements KeyListener {

    Fachada op;
    principal p;
    ArrayList<String> conexiones;
    DefaultListModel dlm;

    public inicio() {
        initComponents();
        setResizable(false);
        op = new Fachada();
        setTitle("AbdSQL");
        jButton1.addKeyListener(this);
        password.addKeyListener(this);
        user.addKeyListener(this);
        comboSistemaGestor.addKeyListener(this);
        jButton2.addKeyListener(this);
        cargar();
        setLocationRelativeTo(null);

        JPopupMenu menu = new JPopupMenu();
        JMenuItem borrar = new JMenuItem("Borrar");
        borrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listaConexiones.getSelectedIndex() < 0) {
                    return;
                }
                conexiones.remove(listaConexiones.getAnchorSelectionIndex());
                dlm.removeElementAt(listaConexiones.getSelectedIndex());
                op.guardarDatosConexion(conexiones);
            }
        });
        menu.add(borrar);
        listaConexiones.setComponentPopupMenu(menu);
    }

    private void cargar() {
        conexiones = op.cargarDatosConexion();
        dlm = new DefaultListModel();
        listaConexiones.setModel(dlm);

        if (conexiones == null) {
            return;
        }
        try {
            for (int i = 0; i < conexiones.size(); i++) {
                dlm.addElement(conexiones.get(i));
            }
        } catch (Exception e) {
            textHost.setText("localhost");
            user.setText("root");
            comboSistemaGestor.setSelectedIndex(0);
            conexiones = null;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        textHost = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        user = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        comboSistemaGestor = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaConexiones = new javax.swing.JList<>();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        textPuerto = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();

        jMenuItem2.setText("jMenuItem2");

        jMenu2.setText("jMenu2");

        jMenuItem4.setText("jMenuItem4");

        jMenu3.setText("jMenu3");

        jMenu4.setText("jMenu4");

        jMenuItem5.setText("jMenuItem5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Direccion");

        textHost.setText("localhost");

        jButton1.setText("iniciar sesion");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("usuario");

        user.setText("root");

        jLabel4.setText("password");

        jLabel2.setText("Sistema Gestor");

        comboSistemaGestor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MySql", "Oracle", "PostgreSQL", "SQLite" }));
        comboSistemaGestor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboSistemaGestorActionPerformed(evt);
            }
        });

        listaConexiones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaConexionesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listaConexiones);

        jLabel5.setText("Conexiones guardadas:");

        jButton2.setText("Guardar conexion actual");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setText("Conexion actual:");

        jLabel7.setText("Puerto");

        textPuerto.setText("3306");

        jMenu1.setText("Archivo");

        jMenuItem3.setText("Acerca de");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem1.setText("Salir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu5.setText("Herramientas");

        jMenuItem6.setText("Abrir archivo SQLite");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem6);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jButton2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel7)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboSistemaGestor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(user)
                            .addComponent(password)
                            .addComponent(textPuerto)
                            .addComponent(textHost, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(115, 115, 115))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(comboSistemaGestor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (p != null && p.isVisible()) {
            return;
        }

        setCursor(Cursor.WAIT_CURSOR);
        op.setTipoConexion(comboSistemaGestor.getSelectedIndex() + 1);
        
        if (!op.conectar(textHost.getText(), textPuerto.getText(), user.getText(), password.getText())) {
            setCursor(Cursor.DEFAULT_CURSOR);
            JOptionPane.showMessageDialog(null, "Error al conectar con\nla base de datos", "Error", 0);
            return;
        }

        setCursor(Cursor.DEFAULT_CURSOR);
        p = new principal(op, this);
        p.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        JOptionPane.showMessageDialog(null, "Administrador de bases\nde datos SQL");
        JOptionPane.showMessageDialog(null, "Version 3.66");
        JOptionPane.showMessageDialog(null, "desarrollado por:\nWilmer Castrillon");
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void listaConexionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaConexionesMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            if (evt.getClickCount() == 2) {
                if (listaConexiones.getSelectedIndex() < 0) {
                    return;
                }
                String s[] = listaConexiones.getSelectedValue().split(" ");
                try {
                    textHost.setText(s[0]);
                    textPuerto.setText(s[1]);
                    user.setText(s[2]);
                    for (int i = 0; i < comboSistemaGestor.getItemCount(); i++) {
                        if (s[3].equalsIgnoreCase(comboSistemaGestor.getItemAt(i))) {
                            comboSistemaGestor.setSelectedIndex(i);
                            break;
                        }
                    }
                } catch (Exception e) {
                    textHost.setText("localhost");
                    user.setText("root");
                    comboSistemaGestor.setSelectedIndex(0);
                    conexiones = null;
                }
            }
        }
    }//GEN-LAST:event_listaConexionesMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (conexiones == null) {
            conexiones = new ArrayList<>();
        }
        conexiones.add(textHost.getText() + " " + textPuerto.getText() + " " + user.getText() + " " 
                + comboSistemaGestor.getSelectedItem());
        dlm.addElement(conexiones.get(conexiones.size() - 1));
        op.guardarDatosConexion(conexiones);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void comboSistemaGestorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboSistemaGestorActionPerformed
        switch(comboSistemaGestor.getSelectedIndex()){
            case 0:
                textPuerto.setText("3306");
                break;
            case 1:
                textPuerto.setText("1521");
                break;
            case 2:
                textPuerto.setText("5432");
                break;
            case 3:
                textPuerto.setText("");
                user.setText("");
                password.setText("");
                break;
        }
    }//GEN-LAST:event_comboSistemaGestorActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        JFileChooser chooser = new JFileChooser("Seleccione carpeta destino");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivo de base de datos", "db");
        chooser.setFileFilter(filtro);
        
        int seleccion = chooser.showOpenDialog(null);
        if (seleccion != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File fichero = chooser.getSelectedFile();
        textHost.setText(fichero.getPath());
        textPuerto.setText("");
        user.setText("");
        password.setText("");
        comboSistemaGestor.setSelectedIndex(3);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(inicio.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(inicio.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(inicio.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(inicio.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new inicio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboSistemaGestor;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> listaConexiones;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField textHost;
    private javax.swing.JTextField textPuerto;
    private javax.swing.JTextField user;
    // End of variables declaration//GEN-END:variables

    public void keyTyped(KeyEvent ke) {
    }

    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER && ke.getComponent() == jButton2) {
            jButton2ActionPerformed(null);
        }else if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            jButton1ActionPerformed(null);
        }
    }

    public void keyReleased(KeyEvent ke) {
    }
}
