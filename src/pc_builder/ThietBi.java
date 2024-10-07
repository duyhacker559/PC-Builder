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
    private static int id = 1;
    
    // Du lieu san pham
    
    private String ten;
    private String loai;
    private String hang;
    private String ma;
    private long gia;

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

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        ThietBi.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public long getGia() {
        return gia;
    }

    public void setGia(long gia) {
        this.gia = gia;
    }

    public ArrayList<ThietBi> getSanPhamTuongThich() {
        return sanPhamTuongThich;
    }

    public void setSanPhamTuongThich(ArrayList<ThietBi> sanPhamTuongThich) {
        this.sanPhamTuongThich = sanPhamTuongThich;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s, %s",ma, ten);
    }
}
