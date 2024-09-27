/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc_builder;
import java.util.*;
/**
 *
 * @author Admin
 */
public class ThietBi {
    static int id = 1;
    
    String ten;
    String loai;
    String hang;
    String ma;
    long gia;
    
    ArrayList<ThietBi> sanPhamTuongThich = new ArrayList<ThietBi>();
    
    private void khoiTaoMaSanPham() {
        String goi = "SP0000";
        int l = Integer.toString(id).length();
        ma = goi.substring(0, goi.length()-l-1)+id;
    } 
    
    public ThietBi() {
        khoiTaoMaSanPham();
        id += 1;
    }
    
    public void docDuLieu(Scanner sc) {
        
    }
}
