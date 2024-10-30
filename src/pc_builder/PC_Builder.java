/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package pc_builder;
import java.sql.SQLException;
import java.util.*;
/**
 *
 * @author Admin
 */
public class PC_Builder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        Login.main(args);
        Login login = new Login();
        login.getAdminAccountList();
        ThietBi thietBi_1 = new ThietBi("Man hinh A", "Man hinh may tinh", "ASUS", 1300000);
        ThietBi thietBi_2 = new ThietBi("Ban phim A", "Ban phim may tinh", "ASUS", 400000);
        ThietBi thietBi_3 = new ThietBi("Chuot A", "Chuot may tinh", "ASUS", 200000);
        
        System.out.println(thietBi_1.toString());
        System.out.println(thietBi_2.toString());
        System.out.println(thietBi_3.toString());
        
        System.out.println("");
        
        thietBi_1.themSanPhamTuongThich(thietBi_3);
        thietBi_1.lietKeCacSanPhamTuongThich(sc);
        
        thietBi_3.setTen("Chuot B");
        thietBi_1.lietKeCacSanPhamTuongThich(sc);
        
        System.out.println("");
        
        TuongTacNguoiDung GUI = new TuongTacNguoiDung();
        GUI.setVisible(true);
    }
}
