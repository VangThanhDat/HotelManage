/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ASUS
 */
public class Loai {

     public String LoaiPhong;
     public String MaLoai;

     public String getLoaiPhong() {
          return LoaiPhong;
     }

     public void setLoaiPhong(String LoaiPhong) {
          this.LoaiPhong = LoaiPhong;
     }

     public String getMaLoai() {
          return MaLoai;
     }

     public void setMaLoai(String MaLoai) {
          this.MaLoai = MaLoai;
     }

     public Loai(String LoaiPhong, String MaLoai) {
          this.LoaiPhong = LoaiPhong;
          this.MaLoai = MaLoai;
     }

     public Loai() {
     }


     public List<Loai> getAll() {
          List<Loai> list = new ArrayList<>();
          String user = "admin";
          String pass = "123";
          String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyPhongKhachSan;encrypt=true;trustServerCertificate=true";
          try {
               Connection con = DriverManager.getConnection(url, user, pass);
               String sql = "select * from loai";
               Statement st = con.createStatement();
               ResultSet rs = st.executeQuery(sql);
               while (rs.next()) {
                    String MaLoai = rs.getString(1);
                    String LoaiPhong = rs.getString(2);
                    list.add(new Loai(MaLoai, LoaiPhong));
               }
               rs.close();
               con.close();
               //System.out.println(list.get(0).email);
          } catch (Exception e) {
               System.out.println(e);
          }
          return list;
     }
}
