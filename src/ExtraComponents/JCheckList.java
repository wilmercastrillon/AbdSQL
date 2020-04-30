package ExtraComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
//import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

public class JCheckList extends JList{

    private DefaultListModel<CheckableItem> modelo;
    
    public JCheckList() {
        modelo = new DefaultListModel<>();
        this.setModel(modelo);
        
        detalles();
    }
    
    public JCheckList(String[] strs) {
        //String[] strs = {"swinpublic JCheckList(String[] strs) {g", "home", "basic", "metal", "JList"};
        modelo = new DefaultListModel<>();
        for(String str : strs){
            modelo.addElement(new CheckableItem(str));
        }
        this.setModel(modelo);
        detalles();
    }
    
    private void detalles(){
        this.setCellRenderer(new CheckListRenderer());
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setBorder(new EmptyBorder(0, 4, 0, 0));
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(!isEnabled()){
                    System.out.println("desabilitado");
                    return;
                }
                int index = locationToIndex(e.getPoint());
                CheckableItem item = (CheckableItem) getModel().getElementAt(index);
                item.setSelected(!item.isSelected());
                Rectangle rect = getCellBounds(index, index);
                repaint(rect);
            }
        });
    }

    public void addCheck(String str) {
        modelo.addElement(new CheckableItem(str));
    }
    
    public ArrayList<String> getCheckedItems(){
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < modelo.size(); i++) {
            CheckableItem item = modelo.getElementAt(i);
            if(item.isSelected){
                items.add(item.text);
            }
        }
        return  items;
    }
    
    public ArrayList<Integer> getCheckedPositions(){
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < modelo.size(); i++) {
            CheckableItem item = modelo.getElementAt(i);
            if(item.isSelected){
                idx.add(i);
            }
        }
        return idx;
    }
    
    @Override
    public void removeAll(){
        modelo.removeAllElements();
    }

    class CheckableItem {
        private String text;
        private boolean isSelected;
        private Icon icon;

        public CheckableItem(String text) {
            this.text = text;
            isSelected = false;
        }

        public void setSelected(boolean b) {
            isSelected = b;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public String toString() {
            return text;
        }

        public void setIcon(Icon icon) {
            this.icon = icon;
        }

        public Icon getIcon() {
            return icon;
        }
    }

    class CheckListRenderer extends CheckRenderer implements ListCellRenderer {
        public Component getListCellRendererComponent( JList list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            check.setSelected(((CheckableItem) value).isSelected());
            label.setText(value.toString());
            label.setSelected(isSelected);
            return (this);
        }
    }
}

class CheckRenderer extends JPanel implements TreeCellRenderer {
    protected JCheckBox check;
    protected TreeLabel label;

    public CheckRenderer() {
        setLayout(null);
        add(check = new JCheckBox());
        add(label = new TreeLabel());
        check.setBackground(new Color(255, 255, 255));
        label.setBackground(new Color(255, 255, 255));
        label.setForeground(new Color(0, 0, 0));
        setBackground(new Color(255, 255, 255));
    }

    public JComponent getTreeCellRendererComponent(JTree tree, Object value,
            boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        String stringValue = tree.convertValueToText(value, isSelected,
                expanded, leaf, row, hasFocus);
        setEnabled(tree.isEnabled());
        check.setSelected(((CheckNode) value).isSelected());
        label.setFont(tree.getFont());
        label.setText(stringValue);
        label.setSelected(isSelected);
        label.setFocus(hasFocus);
        if (leaf) {
            label.setIcon(UIManager.getIcon("Tree.leafIcon"));
        } else if (expanded) {
            label.setIcon(UIManager.getIcon("Tree.openIcon"));
        } else {
            label.setIcon(UIManager.getIcon("Tree.closedIcon"));
        }
        return this;
    }

    public Dimension getPreferredSize() {
        Dimension d_check = check.getPreferredSize();
        Dimension d_label = label.getPreferredSize();
        return new Dimension(d_check.width + d_label.width,
                (d_check.height < d_label.height ? d_label.height
                        : d_check.height));
    }

    public void doLayout() {
        Dimension d_check = check.getPreferredSize();
        Dimension d_label = label.getPreferredSize();
        int y_check = 0;
        int y_label = 0;
        if (d_check.height < d_label.height) {
            y_check = (d_label.height - d_check.height) / 2;
        } else {
            y_label = (d_check.height - d_label.height) / 2;
        }
        check.setLocation(0, y_check);
        check.setBounds(0, y_check, d_check.width, d_check.height);
        label.setLocation(d_check.width, y_label);
        label.setBounds(d_check.width, y_label, d_label.width, d_label.height);
    }

    public void setBackground(Color color) {
        if (color instanceof ColorUIResource) {
            color = null;
        }
        super.setBackground(color);
    }

    public class TreeLabel extends JLabel {
        boolean isSelected;
        boolean hasFocus;

        public TreeLabel() {
        }

        public void setBackground(Color color) {
            if (color instanceof ColorUIResource) {
                color = null;
            }
            super.setBackground(color);
        }

        public void paint(Graphics g) {
            String str;
            if ((str = getText()) != null) {
                if (0 < str.length()) {
                    if (isSelected) {
                        g.setColor(new Color(255, 255, 255));
                    } else {
                        g.setColor(new Color(255, 255, 255));
                    }
                    Dimension d = getPreferredSize();
                    int imageOffset = 0;
                    Icon currentI = getIcon();
                    if (currentI != null) {
                        imageOffset = currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
                    }
                    g.fillRect(imageOffset, 0, d.width - 1 - imageOffset, d.height);
                    if (hasFocus) {
                        g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
                        g.drawRect(imageOffset, 0, d.width - 1 - imageOffset, d.height - 1);
                    }
                }
            }
            super.paint(g);
        }

        public Dimension getPreferredSize() {
            Dimension retDimension = super.getPreferredSize();
            if (retDimension != null) {
                retDimension = new Dimension(retDimension.width + 3,
                        retDimension.height);
            }
            return retDimension;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public void setFocus(boolean hasFocus) {
            this.hasFocus = hasFocus;
        }
    }
}

class CheckNode extends DefaultMutableTreeNode {
    public final static int SINGLE_SELECTION = 0;
    public final static int DIG_IN_SELECTION = 4;
    protected int selectionMode;
    protected boolean isSelected;

    public CheckNode() {
        this(null);
    }

    public CheckNode(Object userObject) {
        this(userObject, true, false);
    }

    public CheckNode(Object userObject, boolean allowsChildren,
            boolean isSelected) {
        super(userObject, allowsChildren);
        this.isSelected = isSelected;
        setSelectionMode(DIG_IN_SELECTION);
    }

    public void setSelectionMode(int mode) {
        selectionMode = mode;
    }

    public int getSelectionMode() {
        return selectionMode;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;

        if ((selectionMode == DIG_IN_SELECTION) && (children != null)) {
            Enumeration e = children.elements();
            while (e.hasMoreElements()) {
                CheckNode node = (CheckNode) e.nextElement();
                node.setSelected(isSelected);
            }
        }
    }

    public boolean isSelected() {
        return isSelected;
    }
}
