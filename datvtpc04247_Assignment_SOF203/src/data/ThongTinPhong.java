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
public class ThongTinPhong {

     public String MaPhong;
     public String TenPhong;
     public String DiaDiem;
     public String MaLoai;
     public double Gia;
     public String MaTrangThai;
     public String NgayO;
     public byte []Hinh;

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

     public String getDiaDiem() {
          return DiaDiem;
     }

     public void setDiaDiem(String DiaDiem) {
          this.DiaDiem = DiaDiem;
     }

     public String getTenLoai() {
          return MaLoai;
     }

     public void setTenLoai(String MaLoai) {
          this.MaLoai = MaLoai;
     }

     public double getGia() {
          return Gia;
     }

     public void setGia(double Gia) {
          this.Gia = Gia;
     }

     public String getMaTrangThai() {
          return MaTrangThai;
     }

     public void setMaTrangThai(String MaTrangThai) {
          this.MaTrangThai = MaTrangThai;
     }

     public String getNgayO() {
          return NgayO;
     }

     public void setNgayO(String NgayO) {
          this.NgayO = NgayO;
     }

     public ThongTinPhong() {
     }

     public ThongTinPhong(String MaPhong, String TenPhong, String DiaDiem, String MaLoai, double Gia, String MaTrangThai, String NgayO, byte[] Hinh) {
          this.MaPhong = MaPhong;
          this.TenPhong = TenPhong;
          this.DiaDiem = DiaDiem;
          this.MaLoai = MaLoai;
          this.Gia = Gia;
          this.MaTrangThai = MaTrangThai;
          this.NgayO = NgayO;
          this.Hinh = Hinh;
     }

     public String getMaLoai() {
          return MaLoai;
     }

     public void setMaLoai(String MaLoai) {
          this.MaLoai = MaLoai;
     }

     public byte[] getHinh() {
          return Hinh;
     }

     public void setHinh(byte[] Hinh) {
          this.Hinh = Hinh;
     }

    
   
}
