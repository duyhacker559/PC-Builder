/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;

import static UI.BuildItem.getBufferedImageFromJson;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class DeviceInformation extends javax.swing.JFrame {
    private JSONObject Device;
    private Home parent;
    /**
     * Creates new form DeviceInformation
     */
    public static BufferedImage getBufferedImageFromJson(JSONObject jsonObject, String key) throws IOException {
        if (!jsonObject.has(key)) {
            throw new IllegalArgumentException("Key not found in JSONObject: " + key);
        }

        String base64Image = jsonObject.getString(key); // Get string Base64 from JSON
        byte[] imageBytes = Base64.getDecoder().decode(base64Image); // Decode Base64

        // Change into BufferedImage
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(bis);
        }
    }
    
    public DeviceInformation(Home parent, JSONObject data) {
        initComponents();
        this.parent = parent;
        this.Device = data;
        
        TypeTittle.setText(data.getString("type"));
        BrandTittle.setText(data.getString("brand"));
        DeviceName.setText(data.getString("name"));
        this.setTitle("Information - "+data.getString("name"));
        DescriptionText.setText(data.getString("des"));
        
        float price = data.getFloat("price");
        if (data.has("forSale")) {
            if (data.getBoolean("forSale")) {
                price = (1-data.getFloat("sale"))*price;
            }
        }
        PurchaseButton.setText(String.format("Get for $%.2f", price));

        try {
            BufferedImage originalImage = getBufferedImageFromJson(data, "icon");
            BufferedImage resizedImage = new BufferedImage(100, 100, originalImage.getType());
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, 100, 100, null);
            g2d.dispose();
            ImageIcon originalIcon = new ImageIcon(resizedImage);

            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(scaledIcon);
            imageLabel.setBounds(0, 0, 100, 100);
            
            DeviceIcon.add(imageLabel);
        } catch (Exception e) {
        }
    }

    public void purchase() {
        parent.purchase(Device);
        this.dispose();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DeviceIcon = new javax.swing.JPanel();
        TypeTittle = new javax.swing.JLabel();
        BrandTittle = new javax.swing.JLabel();
        DeviceName = new javax.swing.JLabel();
        PurchaseButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DescriptionText = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(400, 500));
        setMinimumSize(new java.awt.Dimension(300, 500));
        setPreferredSize(new java.awt.Dimension(320, 480));
        getContentPane().setLayout(null);

        DeviceIcon.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout DeviceIconLayout = new javax.swing.GroupLayout(DeviceIcon);
        DeviceIcon.setLayout(DeviceIconLayout);
        DeviceIconLayout.setHorizontalGroup(
            DeviceIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );
        DeviceIconLayout.setVerticalGroup(
            DeviceIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );

        getContentPane().add(DeviceIcon);
        DeviceIcon.setBounds(10, 10, 100, 100);

        TypeTittle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        TypeTittle.setForeground(new java.awt.Color(102, 102, 0));
        TypeTittle.setText("Type");
        getContentPane().add(TypeTittle);
        TypeTittle.setBounds(115, 60, 180, 30);

        BrandTittle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BrandTittle.setForeground(new java.awt.Color(102, 0, 0));
        BrandTittle.setText("Brand");
        getContentPane().add(BrandTittle);
        BrandTittle.setBounds(115, 35, 180, 30);

        DeviceName.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        DeviceName.setForeground(new java.awt.Color(0, 0, 102));
        DeviceName.setText("DeviceName");
        DeviceName.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        getContentPane().add(DeviceName);
        DeviceName.setBounds(115, 10, 180, 40);

        PurchaseButton.setBackground(new java.awt.Color(0, 204, 0));
        PurchaseButton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        PurchaseButton.setForeground(new java.awt.Color(204, 255, 204));
        PurchaseButton.setText("Get for $1000");
        PurchaseButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PurchaseButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PurchaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PurchaseButtonActionPerformed(evt);
            }
        });
        getContentPane().add(PurchaseButton);
        PurchaseButton.setBounds(10, 390, 280, 40);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Description:");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 130, 90, 20);

        DescriptionText.setEditable(false);
        DescriptionText.setBackground(new java.awt.Color(232, 232, 232));
        DescriptionText.setLineWrap(true);
        DescriptionText.setTabSize(0);
        DescriptionText.setText("This will show user info about this device\n");
        DescriptionText.setWrapStyleWord(true);
        DescriptionText.setAutoscrolls(false);
        DescriptionText.setFocusable(false);
        DescriptionText.setMargin(new java.awt.Insets(6, 6, 6, 6));
        DescriptionText.setRequestFocusEnabled(false);
        DescriptionText.setSelectionColor(new java.awt.Color(242, 242, 242));
        DescriptionText.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(DescriptionText);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 150, 280, 230);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PurchaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PurchaseButtonActionPerformed
        // TODO add your handling code here:
        Confirm newConfirm = new Confirm(this, rootPaneCheckingEnabled, "Are you sure you want to continue at purchasing this product?");
        newConfirm.setLocationRelativeTo(null);
        newConfirm.setVisible(true);
    }//GEN-LAST:event_PurchaseButtonActionPerformed

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
            java.util.logging.Logger.getLogger(DeviceInformation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DeviceInformation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DeviceInformation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DeviceInformation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new DeviceInformation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BrandTittle;
    private javax.swing.JTextArea DescriptionText;
    private javax.swing.JPanel DeviceIcon;
    private javax.swing.JLabel DeviceName;
    private javax.swing.JButton PurchaseButton;
    private javax.swing.JLabel TypeTittle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}