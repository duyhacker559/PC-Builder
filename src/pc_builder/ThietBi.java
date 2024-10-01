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
    
    // Du lieu san pham
    
    String ten;
    String loai;
    String hang;
    private String ma;
    long gia;

    private ArrayList<ThietBi> sanPhamTuongThich = new ArrayList<ThietBi>();
    
    // Khoi tao san pham moi
    
    private void khoiTaoMaSanPham() {
        String goi = "SP000000";
        int l = Integer.toString(id).length();
        ma = goi.substring(0, goi.length()-l-1)+id;
    } 
    
    public ThietBi() {
        khoiTaoMaSanPham();
        id += 1;
    }
    
    public ThietBi(String ten, String loai, String hang, long gia) {
        this.ten = ten;
        this.loai = loai;
        this.hang = hang;
        this.gia = gia;
        khoiTaoMaSanPham ();
        id += 1;
    }
    
    // Cac ham thao tac voi du lieu
    
    public void lietKeCacSanPhamTuongThich(Scanner sc) {
        for (ThietBi i: sanPhamTuongThich) {
            System.out.println(i.toString());
        }
    }
    
    public void themSanPhamTuongThich(ThietBi item) {
        sanPhamTuongThich.add(item);
    }
    
    public void xoaSanPhamTuongThich(ThietBi item) {
        sanPhamTuongThich.remove(item);
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s, %s",ma, ten);
    }
}
