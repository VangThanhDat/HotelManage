/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuanLyPhongKS;

import data.ThongTinChiTiet;
import data.ThongTinPhong;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ASUS
 */
public class QuanLyChiTiet extends javax.swing.JFrame {

     String hinhanh = null;
     ArrayList<ThongTinPhong> list = new ArrayList<>();
     int index = -1;
     private String header[] = {"Mã phòng", "Tên phòng", "Loại phòng", "Địa điểm", "Giá", "Trạng thái", "Ngày ở"};
     private DefaultTableModel tblModel = new DefaultTableModel(header, 0);
     String userName = "admin";
     String password = "123";
     String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyPhongKhachSan;encrypt=true;trustServerCertificate=true";

     public QuanLyChiTiet() {
          initComponents();
          setLocationRelativeTo(null);
          LoadDataToTable();
          LoadtocboLoai();
          LoadtocboTrangThai();
          lamMo();
     }

     public void lamMo() {
          txtDiaDiem.setEditable(false);
          txtGia.setEditable(false);
          txtMaPhong.setEditable(false);
          txtNgay.setEditable(false);
          txtTenPhong.setEditable(false);
          txtTinhTien.setEditable(false);
          cboLoaiPhong.setEditable(false);
          cboTrangThai.setEditable(false);
     }

     public void LoadDataToTable() {
          try {
               tblModel.setRowCount(0);
               Connection con = DriverManager.getConnection(url, userName, password);
               Statement st = con.createStatement();
               String sql = "select a.MaPhong,a.TenPhong,b.LoaiPhong,a.DiaDiem,a.gia,c.TrangThai,a.NgayO from PhongO a \n"
                       + "inner join Loai b on a.MaLoai=b.MaLoai\n"
                       + "inner join TrangThai c on a.MaTrangThai= c.MaTrangThai\n"
                       + "where TrangThai like N'Đã đặt'";
               ResultSet rs = st.executeQuery(sql);
               while (rs.next()) {
                    Vector row = new Vector();
                    row.add(rs.getString(1));
                    row.add(rs.getString(2));
                    row.add(rs.getString(3));
                    row.add(rs.getString(4));
                    row.add(rs.getString(5));
                    row.add(rs.getString(6));
                    row.add(rs.getString(7));
                    tblModel.addRow(row);
               }
               tblDanhSachPhong.setModel(tblModel);
               con.close();
          } catch (Exception e) {
               System.out.println(e);
          }
     }

     public void LoadtocboLoai() {
          try {
               Connection con = DriverManager.getConnection(url, userName, password);
               String sql = "select LoaiPhong from loai";
               Statement st = con.createStatement();

               ResultSet rs = st.executeQuery(sql);
               while (rs.next()) {
                    cboLoaiPhong.addItem(rs.getString(1));
               }
          } catch (Exception e) {
          }
     }

     public void LoadtocboTrangThai() {
          try {
               Connection con = DriverManager.getConnection(url, userName, password);
               String sql = "select TrangThai from TrangThai";
               Statement st = con.createStatement();

               ResultSet rs = st.executeQuery(sql);
               while (rs.next()) {
                    cboTrangThai.addItem(rs.getString(1));
               }
          } catch (Exception e) {
          }
     }

     public void nutXoa() {
          try {
               int index = tblDanhSachPhong.getSelectedRow();
               int ketQua = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa không?", "Xóa sản phẩm", JOptionPane.YES_NO_OPTION);
               if (ketQua == JOptionPane.YES_OPTION) {
                    try {
                         Connection con = DriverManager.getConnection(url, userName, password);
                         String sql = "delete from ChiTietPhong where MaPhong=?";
                         PreparedStatement st = con.prepareStatement(sql);
                         st.setString(1, txtMaPhong.getText());
                         st.executeUpdate();
                         con.close();
                         LoadDataToTable();
                    } catch (Exception ex) {
                         System.out.println(ex);
                    }
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                    LoadDataToTable();
               } else {
                    JOptionPane.showMessageDialog(this, "Ban da huy thao tac");
               }
               return;
          } catch (ArrayIndexOutOfBoundsException e) {
               JOptionPane.showMessageDialog(this, "Chua chon san pham");
          }
     }

     public void NutTimKiem() {
          Connection con = null;
          Statement st = null;
          ResultSet rs = null;
          try {
               con = DriverManager.getConnection(url, userName, password);
               String sql = "select a.MaPhong,a.TenPhong,LoaiPhong,a.DiaDiem,a.SoLuongNguoiO,a.gia,a.TrangThai,NgayO from PhongO a inner join ChiTietPhong b on a.MaPhong=b.MaPhong where a.TrangThai = 'false'";
               if (txtTimKiem.getText().length() > 0) {
                    sql = sql + " and a.MaPhong like N'%" + txtTimKiem.getText() + "%'";
               }
               st = con.createStatement();
               rs = st.executeQuery(sql);
               Vector data = null;
               tblModel.setRowCount(0);
               if (rs.isBeforeFirst() == false) {
                    LoadDataToTable();
                    JOptionPane.showMessageDialog(this, "Mã phòng không tồn tại!");
                    LoadDataToTable();
                    return;
               }
               while (rs.next()) {
                    Vector row = new Vector();
                    row.add(rs.getString(1));
                    row.add(rs.getString(2));
                    row.add(rs.getString(3));
                    row.add(rs.getString(4));
                    row.add(rs.getString(5));
                    row.add(rs.getString(6));
                    row.add(rs.getString(7));
                    row.add(rs.getString(8));
                    tblModel.addRow(row);
               }
               tblDanhSachPhong.setModel(tblModel);
          } catch (Exception e) {
               e.printStackTrace();
          } finally {
               try {
                    if (con != null) {
                         con.close();
                    }
                    if (st != null) {
                         st.close();
                    }
                    if (rs != null) {
                         rs.close();
                    }
               } catch (Exception ex) {
                    ex.printStackTrace();
               }
          }
     }

     public void firstSP() {
          if (list.size() != 0) {
               index = 0;
               tblDanhSachPhong.setRowSelectionInterval(index, index);
          } else {

          }
     }

     public void lastSP() {
          if (list.size() != 0) {
               index = list.size() - 1;
               tblDanhSachPhong.setRowSelectionInterval(index, index);

          } else {

          }
     }

     public void preSP() {
          if (list.size() != 0) {
               if (index == 0) {
                    lastSP();
               } else {
                    index--;
               }
               tblDanhSachPhong.setRowSelectionInterval(index, index);

          } else {
          }
     }

     public void nextSP() {
          if (list.size() != 0) {
               if (index == list.size() - 1) {
                    firstSP();
               } else {
                    index++;
               }
               tblDanhSachPhong.setRowSelectionInterval(index, index);

          } else {
          }
     }

     public void SapXep() {
          int row = tblDanhSachPhong.getSelectedRow();
          txtMaPhong.setText(tblDanhSachPhong.getValueAt(row, 0).toString());
          txtTenPhong.setText(tblDanhSachPhong.getValueAt(row, 1).toString());
          cboLoaiPhong.setSelectedItem(tblDanhSachPhong.getValueAt(row, 2));
          txtDiaDiem.setText(tblDanhSachPhong.getValueAt(row, 3).toString());
          txtGia.setText(tblDanhSachPhong.getValueAt(row, 4).toString());
          cboTrangThai.setSelectedItem(tblDanhSachPhong.getValueAt(row, 5));
          txtNgay.setText(tblDanhSachPhong.getValueAt(row, 6).toString());
     }

     @SuppressWarnings("unchecked")
     // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
     private void initComponents() {

          jPanel2 = new javax.swing.JPanel();
          jLabel1 = new javax.swing.JLabel();
          jPanel1 = new javax.swing.JPanel();
          jLabel2 = new javax.swing.JLabel();
          txtTimKiem = new javax.swing.JTextField();
          btnTim = new javax.swing.JButton();
          btnXoa = new javax.swing.JButton();
          jScrollPane1 = new javax.swing.JScrollPane();
          tblDanhSachPhong = new javax.swing.JTable();
          btnFirst = new javax.swing.JButton();
          btnPre = new javax.swing.JButton();
          btnLast = new javax.swing.JButton();
          btnNext = new javax.swing.JButton();
          btnQuanLy = new javax.swing.JButton();
          btnLamMoi = new javax.swing.JButton();
          jLabel8 = new javax.swing.JLabel();
          txtTinhTien = new javax.swing.JTextField();
          btnTinhTien = new javax.swing.JButton();
          jButton1 = new javax.swing.JButton();
          pnlTimKiem1 = new javax.swing.JPanel();
          lblMaPhong = new javax.swing.JLabel();
          txtMaPhong = new javax.swing.JTextField();
          lblDiaDiem = new javax.swing.JLabel();
          txtDiaDiem = new javax.swing.JTextField();
          lblTrangThai = new javax.swing.JLabel();
          lblSoLuongO = new javax.swing.JLabel();
          txtNgay = new javax.swing.JTextField();
          lblGiaPhong = new javax.swing.JLabel();
          txtGia = new javax.swing.JTextField();
          cboTrangThai = new javax.swing.JComboBox<>();
          lblMaPhong1 = new javax.swing.JLabel();
          txtTenPhong = new javax.swing.JTextField();
          lblMaPhong2 = new javax.swing.JLabel();
          cboLoaiPhong = new javax.swing.JComboBox<>();

          setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
          setPreferredSize(new java.awt.Dimension(791, 595));

          jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

          jLabel1.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
          jLabel1.setForeground(new java.awt.Color(255, 0, 51));
          jLabel1.setText("CHI TIẾT PHÒNG Ở ĐÃ ĐƯỢC ĐẶT");
          jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, -1, -1));

          jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18))); // NOI18N

          jLabel2.setText("Tìm kiếm");

          btnTim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Search.png"))); // NOI18N
          btnTim.setText("Tìm kiếm");
          btnTim.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnTimActionPerformed(evt);
               }
          });

          javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
          jPanel1.setLayout(jPanel1Layout);
          jPanel1Layout.setHorizontalGroup(
               jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(35, 35, 35)
                    .addComponent(jLabel2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(39, 39, 39)
                    .addComponent(btnTim)
                    .addContainerGap(30, Short.MAX_VALUE))
          );
          jPanel1Layout.setVerticalGroup(
               jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(jLabel2)
                         .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                         .addComponent(btnTim))
                    .addGap(0, 6, Short.MAX_VALUE))
          );

          jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 500, 70));

          btnXoa.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
          btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Delete.png"))); // NOI18N
          btnXoa.setText("Xóa");
          btnXoa.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnXoaActionPerformed(evt);
               }
          });
          jPanel2.add(btnXoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 140, 120, -1));

          tblDanhSachPhong.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
          tblDanhSachPhong.setModel(new javax.swing.table.DefaultTableModel(
               new Object [][] {

               },
               new String [] {
                    "Title 1", "Title 2", "Title 3", "Title 4"
               }
          ));
          tblDanhSachPhong.addMouseListener(new java.awt.event.MouseAdapter() {
               public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tblDanhSachPhongMouseClicked(evt);
               }
          });
          jScrollPane1.setViewportView(tblDanhSachPhong);

          jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 410, 790, 150));

          btnFirst.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
          btnFirst.setText("<<");
          btnFirst.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnFirstActionPerformed(evt);
               }
          });
          jPanel2.add(btnFirst, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 370, -1, 30));

          btnPre.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
          btnPre.setText("<");
          btnPre.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnPreActionPerformed(evt);
               }
          });
          jPanel2.add(btnPre, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 370, -1, 30));

          btnLast.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
          btnLast.setText(">>");
          btnLast.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnLastActionPerformed(evt);
               }
          });
          jPanel2.add(btnLast, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 370, -1, 30));

          btnNext.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
          btnNext.setText(">");
          btnNext.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnNextActionPerformed(evt);
               }
          });
          jPanel2.add(btnNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 370, -1, 30));

          btnQuanLy.setText("Quản lý phòng");
          btnQuanLy.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnQuanLyActionPerformed(evt);
               }
          });
          jPanel2.add(btnQuanLy, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 300, -1, -1));

          btnLamMoi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
          btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Refresh.png"))); // NOI18N
          btnLamMoi.setText("Làm mới");
          btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnLamMoiActionPerformed(evt);
               }
          });
          jPanel2.add(btnLamMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 180, 120, -1));

          jLabel8.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
          jLabel8.setText("Tổng tiền :");
          jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 270, -1, 20));

          txtTinhTien.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
          jPanel2.add(txtTinhTien, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 270, 120, 20));

          btnTinhTien.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
          btnTinhTien.setText("Tính tiền");
          btnTinhTien.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnTinhTienActionPerformed(evt);
               }
          });
          jPanel2.add(btnTinhTien, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 270, 90, 20));

          jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
          jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Exit.png"))); // NOI18N
          jButton1.setText("Thoát");
          jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 220, 120, -1));

          pnlTimKiem1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin phòng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18), new java.awt.Color(0, 0, 204))); // NOI18N

          lblMaPhong.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
          lblMaPhong.setText("Mã phòng:");

          txtMaPhong.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

          lblDiaDiem.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
          lblDiaDiem.setText("Địa điểm:");

          txtDiaDiem.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

          lblTrangThai.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
          lblTrangThai.setText("Trạng thái phòng:");

          lblSoLuongO.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
          lblSoLuongO.setText("Ngày ở");

          txtNgay.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

          lblGiaPhong.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
          lblGiaPhong.setText("Giá Phòng");

          txtGia.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

          cboTrangThai.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
          cboTrangThai.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

          lblMaPhong1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
          lblMaPhong1.setText("Tên phòng:");

          txtTenPhong.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

          lblMaPhong2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
          lblMaPhong2.setText("Loại phòng:");

          cboLoaiPhong.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
          cboLoaiPhong.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

          javax.swing.GroupLayout pnlTimKiem1Layout = new javax.swing.GroupLayout(pnlTimKiem1);
          pnlTimKiem1.setLayout(pnlTimKiem1Layout);
          pnlTimKiem1Layout.setHorizontalGroup(
               pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(pnlTimKiem1Layout.createSequentialGroup()
                    .addGroup(pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                         .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlTimKiem1Layout.createSequentialGroup()
                              .addComponent(lblMaPhong)
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                              .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                         .addGroup(pnlTimKiem1Layout.createSequentialGroup()
                              .addComponent(lblDiaDiem)
                              .addGap(101, 101, 101)
                              .addComponent(txtDiaDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                         .addGroup(pnlTimKiem1Layout.createSequentialGroup()
                              .addGroup(pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                   .addComponent(lblSoLuongO)
                                   .addComponent(lblGiaPhong)
                                   .addComponent(lblTrangThai))
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                              .addGroup(pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                   .addComponent(txtNgay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                   .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                   .addComponent(cboTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                         .addGroup(pnlTimKiem1Layout.createSequentialGroup()
                              .addGroup(pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                   .addComponent(lblMaPhong1)
                                   .addComponent(lblMaPhong2))
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                              .addGroup(pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                   .addComponent(cboLoaiPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                   .addComponent(txtTenPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(0, 55, Short.MAX_VALUE))
          );
          pnlTimKiem1Layout.setVerticalGroup(
               pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(pnlTimKiem1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(lblMaPhong)
                         .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(lblMaPhong1)
                         .addComponent(txtTenPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(lblMaPhong2)
                         .addComponent(cboLoaiPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(lblDiaDiem)
                         .addComponent(txtDiaDiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(cboTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                         .addComponent(lblTrangThai))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(lblGiaPhong)
                         .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(pnlTimKiem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(lblSoLuongO)
                         .addComponent(txtNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          );

          jPanel2.add(pnlTimKiem1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 430, 280));

          javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
          getContentPane().setLayout(layout);
          layout.setHorizontalGroup(
               layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          );
          layout.setVerticalGroup(
               layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)
          );

          pack();
     }// </editor-fold>//GEN-END:initComponents

     private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
          nutXoa();
     }//GEN-LAST:event_btnXoaActionPerformed

     private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
          lastSP();
     }//GEN-LAST:event_btnLastActionPerformed

     private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
          firstSP();
     }//GEN-LAST:event_btnFirstActionPerformed

     private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
          nextSP();
     }//GEN-LAST:event_btnNextActionPerformed

     private void btnPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreActionPerformed
          preSP();
     }//GEN-LAST:event_btnPreActionPerformed

     private void tblDanhSachPhongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachPhongMouseClicked
          SapXep();
     }//GEN-LAST:event_tblDanhSachPhongMouseClicked

     private void btnQuanLyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyActionPerformed
          dispose();
          new QuanLyPhong().setVisible(true);
     }//GEN-LAST:event_btnQuanLyActionPerformed

     private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
          txtDiaDiem.setText("");
          txtGia.setText("");
          txtMaPhong.setText("");
          txtNgay.setText("");
          txtTenPhong.setText("");
          cboLoaiPhong.setSelectedItem(1);
          cboTrangThai.setSelectedItem(1);
     }//GEN-LAST:event_btnLamMoiActionPerformed

     private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
          NutTimKiem();
     }//GEN-LAST:event_btnTimActionPerformed

     private void btnTinhTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTinhTienActionPerformed

     }//GEN-LAST:event_btnTinhTienActionPerformed

     /**
      * @param args the command line arguments
      */
     public static void main(String args[]) {
          /* Set the Nimbus look and feel */
          //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
          /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
           */
          try {
               for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Windows".equals(info.getName())) {
                         javax.swing.UIManager.setLookAndFeel(info.getClassName());
                         break;
                    }
               }
          } catch (ClassNotFoundException ex) {
               java.util.logging.Logger.getLogger(QuanLyChiTiet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
          } catch (InstantiationException ex) {
               java.util.logging.Logger.getLogger(QuanLyChiTiet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
          } catch (IllegalAccessException ex) {
               java.util.logging.Logger.getLogger(QuanLyChiTiet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
          } catch (javax.swing.UnsupportedLookAndFeelException ex) {
               java.util.logging.Logger.getLogger(QuanLyChiTiet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
          }
          //</editor-fold>
          //</editor-fold>

          /* Create and display the form */
          java.awt.EventQueue.invokeLater(new Runnable() {
               public void run() {
                    new QuanLyChiTiet().setVisible(false);
                    new Login().setVisible(true);
               }
          });
     }

     // Variables declaration - do not modify//GEN-BEGIN:variables
     private javax.swing.JButton btnFirst;
     private javax.swing.JButton btnLamMoi;
     private javax.swing.JButton btnLast;
     private javax.swing.JButton btnNext;
     private javax.swing.JButton btnPre;
     private javax.swing.JButton btnQuanLy;
     private javax.swing.JButton btnTim;
     private javax.swing.JButton btnTinhTien;
     private javax.swing.JButton btnXoa;
     private javax.swing.JComboBox<String> cboLoaiPhong;
     private javax.swing.JComboBox<String> cboTrangThai;
     private javax.swing.JButton jButton1;
     private javax.swing.JLabel jLabel1;
     private javax.swing.JLabel jLabel2;
     private javax.swing.JLabel jLabel8;
     private javax.swing.JPanel jPanel1;
     private javax.swing.JPanel jPanel2;
     private javax.swing.JScrollPane jScrollPane1;
     private javax.swing.JLabel lblDiaDiem;
     private javax.swing.JLabel lblGiaPhong;
     private javax.swing.JLabel lblMaPhong;
     private javax.swing.JLabel lblMaPhong1;
     private javax.swing.JLabel lblMaPhong2;
     private javax.swing.JLabel lblSoLuongO;
     private javax.swing.JLabel lblTrangThai;
     private javax.swing.JPanel pnlTimKiem1;
     private javax.swing.JTable tblDanhSachPhong;
     private javax.swing.JTextField txtDiaDiem;
     private javax.swing.JTextField txtGia;
     private javax.swing.JTextField txtMaPhong;
     private javax.swing.JTextField txtNgay;
     private javax.swing.JTextField txtTenPhong;
     private javax.swing.JTextField txtTimKiem;
     private javax.swing.JTextField txtTinhTien;
     // End of variables declaration//GEN-END:variables

}
