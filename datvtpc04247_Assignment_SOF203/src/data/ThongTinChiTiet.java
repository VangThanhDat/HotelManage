/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;

/**
 *
 * @author ASUS
 */
public class ThongTinChiTiet {

     String MaPhong;
     String TenPhong;
     String LoaiPhong;
     String DiaDiem;
     int SoLuongNguoiO;
     double Gia ;
     boolean TrangThai ;
     String NgayO ;

     public ThongTinChiTiet(String MaPhong, String TenPhong, String LoaiPhong, String DiaDiem, int SoLuongNguoiO, double Gia, boolean TrangThai, String NgayO) {
          this.MaPhong = MaPhong;
          this.TenPhong = TenPhong;
          this.LoaiPhong = LoaiPhong;
          this.DiaDiem = DiaDiem;
          this.SoLuongNguoiO = SoLuongNguoiO;
          this.Gia = Gia;
          this.TrangThai = TrangThai;
          this.NgayO = NgayO;
     }

     public ThongTinChiTiet() {
     }

     public String getMaPhong() {
          return MaPhong;
     }

     public void setMaPhong(String MaPhong) {
          this.MaPhong = MaPhong;
     }

     public String getTenPhong() {
          return TenPhong;
     }

     public void setTenPhong(String TenPhong) {
          this.TenPhong = TenPhong;
     }

     public String getLoaiPhong() {
          return LoaiPhong;
     }

     public void setLoaiPhong(String LoaiPhong) {
          this.LoaiPhong = LoaiPhong;
     }

     public String getDiaDiem() {
          return DiaDiem;
     }

     public void setDiaDiem(String DiaDiem) {
          this.DiaDiem = DiaDiem;
     }

     public int getSoLuongNguoiO() {
          return SoLuongNguoiO;
     }

     public void setSoLuongNguoiO(int SoLuongNguoiO) {
          this.SoLuongNguoiO = SoLuongNguoiO;
     }

     public double getGia() {
          return Gia;
     }

     public void setGia(double Gia) {
          this.Gia = Gia;
     }

     public boolean isTrangThai() {
          return TrangThai;
     }

     public void setTrangThai(boolean TrangThai) {
          this.TrangThai = TrangThai;
     }

     public String getNgayO() {
          return NgayO;
     }

     public void setNgayO(String NgayO) {
          this.NgayO = NgayO;
     }
     
}
