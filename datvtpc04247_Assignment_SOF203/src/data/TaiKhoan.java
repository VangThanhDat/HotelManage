/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class TaiKhoan {

     String tentaikhoan;
     String matkhau;
     boolean quyen; 

     public TaiKhoan(String tentaikhoan, String matkhau, boolean quyen) {
          this.tentaikhoan = tentaikhoan;
          this.matkhau = matkhau;
          this.quyen = quyen;
     }

     public TaiKhoan() {
     }

     public String getTentaikhoan() {
          return tentaikhoan;
     }

     public void setTentaikhoan(String tentaikhoan) {
          this.tentaikhoan = tentaikhoan;
     }

     public String getMatkhau() {
          return matkhau;
     }

     public void setMatkhau(String matkhau) {
          this.matkhau = matkhau;
     }

     public boolean isQuyen() {
          return quyen;
     }

     public void setQuyen(boolean quyen) {
          this.quyen = quyen;
     }

     

}
