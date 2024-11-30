/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class ModifyData extends javax.swing.JDialog {
    private Object parent;
    private String targetKey;
    private JSONObject modifyObject;

    /**
     * Creates new form ModifyData
     */
    
    @SuppressWarnings("empty-statement")
    
    private String FixType(String data) {
        String type[] = {"string","integer","boolean","json"};
        for (String v: type) {
            if (data.toLowerCase().compareTo(v)==0) return v;
        }
        return type[0];
    }
    
    private String GetType(Object data) {
        if (data instanceof Integer) {
            return "integer";
        } else if (data instanceof Long) {
            return "long";
        } else if (data instanceof Float) {
            return "float";
        } else if (data instanceof Double) {
            return "double";
        } else if (data instanceof Boolean) {
            return "boolean";
        } else if (data instanceof JSONObject) {
            return "json";
        } else {
            String returnValue = "string";
            try {
                Integer.parseInt((String)data);
                return "integer";
            } catch (Exception e) {
            }
            try {
                Long.parseLong((String)data);
                return "long";
            } catch (Exception e) {
            }
            try {
                Float.parseFloat((String)data);
                return "float";
            } catch (Exception e) {
            }
            try {
                Double.parseDouble((String)data);
                return "double";
            } catch (Exception e) {
            }
            return returnValue;
        }
    }
    
    public void getTable() {
        DefaultTableModel tabelModel = (DefaultTableModel)ObjectTable.getModel();
        tabelModel.setRowCount(0);
        for (String key : modifyObject.keySet()) {
            String type = GetType(modifyObject.get(key));
            String data[] = {key, type, modifyObject.get(key).toString()};
            tabelModel.addRow(data);
        }
        //modifyObject.put(key, modifyObject.get(key));  // Add new fields or update existing ones
    }
    
    public void eventListener() {
        ObjectTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Check if the event is an update
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow(); // The row that was edited
                    int col = e.getColumn();  // The column that was edited

                    // Get the updated value
                    Object updatedValue = ObjectTable.getValueAt(row, col);

                    if (updatedValue != null) {
                        String fix = FixType((String) ObjectTable.getValueAt(row, 1));
                        if (fix.compareTo("json")==0) {
                            modifyObject.put((String) ObjectTable.getValueAt(row, 0), new JSONObject((String)ObjectTable.getValueAt(row, 2)));
                        } else if (fix.compareTo("boolean")==0) {
                            modifyObject.put((String) ObjectTable.getValueAt(row, 0), Boolean.parseBoolean((String) ObjectTable.getValueAt(row, 2)));
                        } else {
                            modifyObject.put((String) ObjectTable.getValueAt(row, 0), ObjectTable.getValueAt(row, 2));
                        }
                        getTable();
                    }
                    
                }
            }
        });
    }
    
    public ModifyData(JFrame parent, JSONObject object, String targetKey) {
        super((Frame)parent, "Child Dialog", Dialog.ModalityType.APPLICATION_MODAL);
        this.modifyObject = object;
        this.parent = parent;
        this.targetKey = targetKey;
        initComponents();
        getTable();
        eventListener();
        this.setTitle("Modify - "+targetKey);
    }
    
    public ModifyData(Dialog parent, JSONObject object, String targetKey) {
        super((Dialog)parent, "Child Dialog", Dialog.ModalityType.APPLICATION_MODAL);
        this.modifyObject = object;
        this.parent = parent;
        this.targetKey = targetKey;
        initComponents();
        getTable();
        eventListener();
        this.setTitle("Modify - "+targetKey);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ObjectTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        ApplyButton = new javax.swing.JButton();
        ChangeButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        AddButton = new javax.swing.JButton();
        Attribute = new javax.swing.JTextField();
        RemoveButton = new javax.swing.JButton();
        EditButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modify");
        setPreferredSize(new java.awt.Dimension(400, 500));
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(250, 450));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(250, 350));

        ObjectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Attribute", "Type", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ObjectTable.setColumnSelectionAllowed(true);
        ObjectTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(ObjectTable);
        ObjectTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (ObjectTable.getColumnModel().getColumnCount() > 0) {
            ObjectTable.getColumnModel().getColumn(0).setResizable(false);
            ObjectTable.getColumnModel().getColumn(1).setResizable(false);
            ObjectTable.getColumnModel().getColumn(2).setResizable(false);
        }

        ApplyButton.setText("Apply");
        ApplyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApplyButtonActionPerformed(evt);
            }
        });
        jPanel2.add(ApplyButton);

        ChangeButton.setText("Cancel");
        ChangeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangeButtonActionPerformed(evt);
            }
        });
        jPanel2.add(ChangeButton);

        AddButton.setText("Add");
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });
        jPanel3.add(AddButton);

        Attribute.setPreferredSize(new java.awt.Dimension(100, 22));
        jPanel3.add(Attribute);

        RemoveButton.setText("Remove");
        RemoveButton.setPreferredSize(new java.awt.Dimension(90, 23));
        RemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveButtonActionPerformed(evt);
            }
        });
        jPanel3.add(RemoveButton);

        EditButton.setText("Edit");
        EditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditButtonActionPerformed(evt);
            }
        });
        jPanel3.add(EditButton);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void updateTable(String key, Object newData) {
        modifyObject.put(key, newData);
        getTable();
    }
    
    private void EditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditButtonActionPerformed
        // TODO add your handling code here:
        if (ObjectTable.getSelectedRow()>=0) {
            if (modifyObject.get((String) ObjectTable.getValueAt(ObjectTable.getSelectedRow(), 0)) instanceof JSONObject) {
                ModifyData jframe = new ModifyData((Dialog)this, (JSONObject) modifyObject.getJSONObject((String) ObjectTable.getValueAt(ObjectTable.getSelectedRow(), 0)), (String) ObjectTable.getValueAt(ObjectTable.getSelectedRow(), 0));
                jframe.setTitle("Modify - "+(String) ObjectTable.getValueAt(ObjectTable.getSelectedRow(), 0));
                jframe.setLocation(this.getLocation().x+this.getWidth(), this.getLocation().y);
                jframe.setVisible(true);
            } else {
                GetData jframe = new GetData(this, (String) ObjectTable.getValueAt(ObjectTable.getSelectedRow(), 0), (String) ObjectTable.getValueAt(ObjectTable.getSelectedRow(), 2));
                jframe.setTitle("Modify - "+(String) ObjectTable.getValueAt(ObjectTable.getSelectedRow(), 0));
                jframe.setLocationRelativeTo(null);
                jframe.setVisible(true);
            }
        }
    }//GEN-LAST:event_EditButtonActionPerformed

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        // TODO add your handling code here:
        modifyObject.put(Attribute.getText(), "N/A");
        getTable();
    }//GEN-LAST:event_AddButtonActionPerformed

    private void ChangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChangeButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_ChangeButtonActionPerformed

    private void ApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApplyButtonActionPerformed
        // TODO add your handling code here:
        if (parent instanceof ModifyData) {
            ModifyData name = (ModifyData)parent;
            name.updateTable(targetKey, modifyObject);
        } else {
            Admin name = (Admin)parent;
            name.updateTable(targetKey, modifyObject);
        }
        this.dispose();
    }//GEN-LAST:event_ApplyButtonActionPerformed

    private void RemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveButtonActionPerformed
        // TODO add your handling code here:
        if (ObjectTable.getSelectedRow()>=0) {
            String key = (String) ObjectTable.getValueAt(ObjectTable.getSelectedRow(), 0);
            modifyObject.remove(key);
            getTable();
        }
    }//GEN-LAST:event_RemoveButtonActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(ModifyData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModifyData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModifyData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModifyData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new ModifyData(this,null,null).setVisible(true);
            }
        });
    }
    
    // Add custom action for the Enter key

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JButton ApplyButton;
    private javax.swing.JTextField Attribute;
    private javax.swing.JButton ChangeButton;
    private javax.swing.JButton EditButton;
    private javax.swing.JTable ObjectTable;
    private javax.swing.JButton RemoveButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
