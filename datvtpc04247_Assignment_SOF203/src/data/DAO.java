/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import QuanLyPhongKS.Login;
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
public class DAO {

     public List<TaiKhoan> getAll() {
          List<TaiKhoan> list = new ArrayList<>();
          String userName = "admin";
          String password = "123";
          String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyPhongKhachSan;encrypt=true;trustServerCertificate=true";
          try {
               Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
               Connection con = DriverManager.getConnection(url, userName, password);
               Statement st = con.createStatement();
               String sql = "select * from TAIKHOAN";
               ResultSet rs = st.executeQuery(sql);
               list.clear();
               while (rs.next()) {
                    String tentaikhoan = rs.getString(1);
                    String matkhau = rs.getString(2);
                    boolean quyen = rs.getBoolean(3);
                    list.add(new TaiKhoan(tentaikhoan, matkhau, quyen));
               }
               con.close();
          } catch (Exception ex) {
               System.out.println(ex);
          }
          return list;
     }

     public int login(String username, String password) {
          DAO acc = new DAO();
          for (TaiKhoan tk : acc.getAll()) {
               if (tk.getTentaikhoan().equals(username) && tk.getMatkhau().equals(password)) {
                    if (tk.isQuyen()) {
                         return 1;
                    }
               }
          }
          return 0;
     }

}
