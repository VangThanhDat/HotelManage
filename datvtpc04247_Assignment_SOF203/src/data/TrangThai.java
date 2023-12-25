/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author ASUS
 */
public class TrangThai {
     public String MaTrangThai;
     public String TrangThai;

     public TrangThai() {
     }

     public TrangThai(String MaTrangThai, String TrangThai) {
          this.MaTrangThai = MaTrangThai;
          this.TrangThai = TrangThai;
     }

     public String getMaTrangThai() {
          return MaTrangThai;
     }

     public void setMaTrangThai(String MaTrangThai) {
          this.MaTrangThai = MaTrangThai;
     }

     public String getTrangThai() {
          return TrangThai;
     }

     public void setTrangThai(String TrangThai) {
          this.TrangThai = TrangThai;
     }
     
}
