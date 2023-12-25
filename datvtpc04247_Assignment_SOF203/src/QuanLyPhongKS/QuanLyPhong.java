package QuanLyPhongKS;

import data.Loai;
import data.ThongTinPhong;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.imageio.ImageIO;

/**
 *
 * @author ASUS
 */
public class QuanLyPhong extends javax.swing.JFrame {

    String filename = null;
    byte[] anh_phong = null;
    ArrayList<ThongTinPhong> list = new ArrayList<>();
    int index = -1;
    private String header[] = {"Mã phòng", "Tên phòng", "Loại phòng", "Địa điểm", "Giá", "Trạng thái", "Ngày ở"};
    private DefaultTableModel tblModel = new DefaultTableModel(header, 0);
    String userName = "admin";
    String password = "123";
    String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyPhongKhachSan;encrypt=true;trustServerCertificate=true";

    public QuanLyPhong() {
        initComponents();
        setLocationRelativeTo(null);
        new Thread() {
            public void run() {
                while (true) {
                    Calendar cal = new GregorianCalendar();
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int min = cal.get(Calendar.MINUTE);
                    int sec = cal.get(Calendar.SECOND);

                    String time = String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);

                    lblTime.setText(time);
                }
            }
        }.start();
        LoadDataToTable();
        LoadtocboLoai();
        LoadtocboTrangThai();
    }

    public void LoadDataToTable() {
        try {
            tblModel.setRowCount(0);
            Connection con = DriverManager.getConnection(url, userName, password);
            Statement st = con.createStatement();
            String sql = "select a.MaPhong,a.TenPhong,b.LoaiPhong,a.DiaDiem,a.gia,c.TrangThai,a.NgayO from PhongO a \n"
                    + "inner join Loai b on a.MaLoai=b.MaLoai\n"
                    + "inner join TrangThai c on a.MaTrangThai= c.MaTrangThai";
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

    String dburl = "jdbc:sqlserver://localhost:1433;databaseName=edusysdao;encrypt=true;trustServerCertificate=true";
    String user = "edusys";
    String pass = "123";

    public void LoadtocboLoai() {
        try {
            Connection con = DriverManager.getConnection(dburl, user, pass);
            String sql = "select MACD from loai";
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
            String sql = "select MaCD from chuyen_de";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                c.addItem(rs.getString(1));
            }
        } catch (Exception e) {
        }
    }

    public void nutThem() {
        if (checkText()) {
            int them = JOptionPane.showConfirmDialog(this, "Bạn chắc chắc muốn thêm phòng mới?", "Thêm phòng ?", JOptionPane.YES_NO_OPTION, JOptionPane.YES_OPTION);
            if (them == JOptionPane.YES_OPTION) {
                if (txtMaPhong.getText().equals("")) {
                    JOptionPane.showMessageDialog(this, "Không được bỏ trống mã phòng");
                } else {
                    try {
                        String l = null;
                        String th = null;
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        Connection con = DriverManager.getConnection(url, userName, password);
                        String sql = "insert into PhongO values(?,?,?,?,?,?,?)";
                        PreparedStatement st = con.prepareStatement(sql);
                        if (cboLoaiPhong.getSelectedItem().equals("Bình thường")) {
                            l = "L01";
                        } else {
                            l = "L02";
                        }
                        if (cboTrangThai.getSelectedItem().equals("Đã đặt")) {
                            th = "TH01";
                        } else {
                            th = "TH04";
                        }
                        st.setString(1, txtMaPhong.getText());
                        st.setString(2, txtTenPhong.getText());
                        st.setString(3, l);
                        st.setString(4, txtDiaDiem.getText());
                        st.setString(5, txtGia.getText());
                        st.setString(6, th);
                        st.setString(7, txtNgay.getText());
                        st.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Thêm thành công");
                        con.close();
                        this.LoadDataToTable();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Thêm thất bại" + e);
                    }
                }
            }
        }
    }

    public void NutCapNhat() {
        if (btnCapNhat.getText().equals("Cập nhật")) {
            try {
                int sua = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn sửa thông tin phòng này?", "Sửa thông tin", JOptionPane.YES_NO_OPTION, JOptionPane.YES_OPTION);
                if (sua == JOptionPane.YES_OPTION) {
                    String th = null;
                    String l = null;
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    Connection con = DriverManager.getConnection(url, userName, password);
                    String sql = "update PhongO set TenPhong= ?, maLoai = ?, DiaDiem = ?, Gia = ?, MaTrangThai = ?, NgayO=? where MaPhong = ?";
                    PreparedStatement st = con.prepareStatement(sql);
                    if (cboLoaiPhong.getSelectedItem().equals("Bình thường")) {
                        l = "L01";
                    } else {
                        l = "L02";
                    }
                    if (cboTrangThai.getSelectedItem().equals("Đã đặt")) {
                        th = "TH01";
                    } else {
                        th = "TH04";
                    }
                    st.setString(7, txtMaPhong.getText());
                    st.setString(1, txtTenPhong.getText());
                    st.setString(2, l);
                    st.setString(3, txtDiaDiem.getText());
                    st.setString(4, txtGia.getText());
                    st.setString(5, th);
                    st.setString(6, txtNgay.getText());
                    st.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Sửa thông tin tài khoản thành công");
                    con.close();
                    this.LoadDataToTable();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi sửa thông tin" + e);
            }
        }
    }

    public void nutXoa() {
        try {
            int index = tblDanhSachPhong.getSelectedRow();
            int ketQua = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa không?", "Xóa sản phẩm", JOptionPane.YES_NO_OPTION);
            if (ketQua == JOptionPane.YES_OPTION) {
                try {
                    Connection con = DriverManager.getConnection(url, userName, password);
                    String sql = "delete from PhongO where MaPhong=?";
                    PreparedStatement st = con.prepareStatement(sql);
                    st.setString(1, txtMaPhong.getText());
                    st.executeUpdate();
                    con.close();
                    LoadDataToTable();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                JOptionPane.showMessageDialog(this, "Xóa thành công");
            } else {
                JOptionPane.showMessageDialog(this, "Bạn đã hủy thao tác");
            }
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(this, "Chưa chọn phòng để xóa");
        }
    }

    public void NutTimKiem() {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(url, userName, password);
            String sql = "select a.MaPhong,a.TenPhong,b.LoaiPhong,a.DiaDiem,a.gia,c.TrangThai,a.NgayO from PhongO a \n"
                    + "inner join Loai b on a.MaLoai=b.MaLoai\n"
                    + "inner join TrangThai c on a.MaTrangThai= c.MaTrangThai";
            if (txtMaPhong.getText().length() > 0) {
                sql = sql + " where MaPhong like N'%" + txtMaPhong.getText() + "%'";
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

    public boolean checkText() {
        if (txtMaPhong.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa nhập mã phòng");
            txtMaPhong.requestFocus();
            return false;
        } else if (txtTenPhong.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa nhập tên phòng");
            txtMaPhong.requestFocus();
            return false;
        } else if (txtDiaDiem.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa nhập địa điểm phòng");
            txtDiaDiem.requestFocus();
            return false;
        } else if (txtGia.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa nhập giá phòng");
            txtGia.requestFocus();
            return false;
        } else if (txtNgay.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa nhập ngày ở");
            txtNgay.requestFocus();
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
     // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
     private void initComponents() {

          btgTrangThai = new javax.swing.ButtonGroup();
          lblQuanLy = new javax.swing.JLabel();
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
          pnlChucNang = new javax.swing.JPanel();
          btnThem = new javax.swing.JButton();
          btnXoa = new javax.swing.JButton();
          btnTimKiem = new javax.swing.JButton();
          btnCapNhat = new javax.swing.JButton();
          btnThoat = new javax.swing.JButton();
          btnMain = new javax.swing.JButton();
          btnLamMoi = new javax.swing.JButton();
          SpnlDanhSach = new javax.swing.JScrollPane();
          tblDanhSachPhong = new javax.swing.JTable();
          btnFirst = new javax.swing.JButton();
          btnPre = new javax.swing.JButton();
          btnNext = new javax.swing.JButton();
          btnLast = new javax.swing.JButton();
          lblTime = new javax.swing.JLabel();
          lblHinh = new javax.swing.JLabel();
          btnChiTiet = new javax.swing.JButton();

          setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
          addWindowListener(new java.awt.event.WindowAdapter() {
               public void windowClosing(java.awt.event.WindowEvent evt) {
                    formWindowClosing(evt);
               }
          });
          getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

          lblQuanLy.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
          lblQuanLy.setForeground(new java.awt.Color(255, 0, 51));
          lblQuanLy.setText("QUẢN LÝ PHÒNG KHÁCH SẠN");
          getContentPane().add(lblQuanLy, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, -1, -1));

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

          getContentPane().add(pnlTimKiem1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 430, 280));

          pnlChucNang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18), new java.awt.Color(0, 0, 153))); // NOI18N

          btnThem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
          btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Add.png"))); // NOI18N
          btnThem.setText("Thêm");
          btnThem.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnThemActionPerformed(evt);
               }
          });

          btnXoa.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
          btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Delete.png"))); // NOI18N
          btnXoa.setText("Xóa");
          btnXoa.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnXoaActionPerformed(evt);
               }
          });

          btnTimKiem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
          btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Search.png"))); // NOI18N
          btnTimKiem.setText("Tìm kiếm");
          btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnTimKiemActionPerformed(evt);
               }
          });

          btnCapNhat.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
          btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Edit.png"))); // NOI18N
          btnCapNhat.setText("Cập nhật");
          btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnCapNhatActionPerformed(evt);
               }
          });

          btnThoat.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
          btnThoat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Log out.png"))); // NOI18N
          btnThoat.setText("Thoát");
          btnThoat.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnThoatActionPerformed(evt);
               }
          });

          btnMain.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
          btnMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Home.png"))); // NOI18N
          btnMain.setText("Main");
          btnMain.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnMainActionPerformed(evt);
               }
          });

          btnLamMoi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
          btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Refresh.png"))); // NOI18N
          btnLamMoi.setText("Làm mới");
          btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnLamMoiActionPerformed(evt);
               }
          });

          javax.swing.GroupLayout pnlChucNangLayout = new javax.swing.GroupLayout(pnlChucNang);
          pnlChucNang.setLayout(pnlChucNangLayout);
          pnlChucNangLayout.setHorizontalGroup(
               pnlChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(pnlChucNangLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(pnlChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                         .addGroup(pnlChucNangLayout.createSequentialGroup()
                              .addGroup(pnlChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                   .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                   .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                              .addGroup(pnlChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                   .addComponent(btnCapNhat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                   .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                              .addGap(0, 0, Short.MAX_VALUE))
                         .addGroup(pnlChucNangLayout.createSequentialGroup()
                              .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                              .addComponent(btnMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addContainerGap())
               .addGroup(pnlChucNangLayout.createSequentialGroup()
                    .addGap(69, 69, 69)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          );
          pnlChucNangLayout.setVerticalGroup(
               pnlChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(pnlChucNangLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(btnThem)
                         .addComponent(btnXoa))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(pnlChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(btnCapNhat)
                         .addComponent(btnTimKiem))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btnLamMoi)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(pnlChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(btnThoat)
                         .addComponent(btnMain))
                    .addContainerGap())
          );

          getContentPane().add(pnlChucNang, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, -1, -1));

          tblDanhSachPhong.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
          tblDanhSachPhong.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
          tblDanhSachPhong.setModel(new javax.swing.table.DefaultTableModel(
               new Object [][] {

               },
               new String [] {
                    "Mã Phòng", "Địa điểm", "số lượng người", "Giá", "Trạng Thái"
               }
          ));
          tblDanhSachPhong.addMouseListener(new java.awt.event.MouseAdapter() {
               public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tblDanhSachPhongMouseClicked(evt);
               }
          });
          SpnlDanhSach.setViewportView(tblDanhSachPhong);

          getContentPane().add(SpnlDanhSach, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 800, 173));

          btnFirst.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
          btnFirst.setText("<<");
          btnFirst.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnFirstActionPerformed(evt);
               }
          });
          getContentPane().add(btnFirst, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, -1, -1));

          btnPre.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
          btnPre.setText("<");
          btnPre.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnPreActionPerformed(evt);
               }
          });
          getContentPane().add(btnPre, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 440, -1, -1));

          btnNext.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
          btnNext.setText(">");
          btnNext.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnNextActionPerformed(evt);
               }
          });
          getContentPane().add(btnNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 440, -1, -1));

          btnLast.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
          btnLast.setText(">>");
          btnLast.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnLastActionPerformed(evt);
               }
          });
          getContentPane().add(btnLast, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 440, -1, -1));

          lblTime.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
          lblTime.setForeground(new java.awt.Color(255, 0, 0));
          getContentPane().add(lblTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 134, 25));

          lblHinh.setText("Hình ảnh");
          lblHinh.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
          lblHinh.addMouseListener(new java.awt.event.MouseAdapter() {
               public void mouseClicked(java.awt.event.MouseEvent evt) {
                    lblHinhMouseClicked(evt);
               }
          });
          getContentPane().add(lblHinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 330, 180, 110));

          btnChiTiet.setText("Danh sách phòng đã được đặt");
          btnChiTiet.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnChiTietActionPerformed(evt);
               }
          });
          getContentPane().add(btnChiTiet, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 400, -1, -1));

          pack();
     }// </editor-fold>//GEN-END:initComponents

     private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
         nutThem();
     }//GEN-LAST:event_btnThemActionPerformed

     private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
         nutXoa();
     }//GEN-LAST:event_btnXoaActionPerformed

     private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
         if (txtMaPhong.getText().isEmpty()) {
             JOptionPane.showMessageDialog(this, "Nhập title để tìm kiếm!", "Lỗi tìm kiếm", HEIGHT);
         } else {
             NutTimKiem();
         }
     }//GEN-LAST:event_btnTimKiemActionPerformed

     private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
         NutCapNhat();
     }//GEN-LAST:event_btnCapNhatActionPerformed

     private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
         txtDiaDiem.setText("");
         txtGia.setText("");
         txtMaPhong.setText("");
         txtNgay.setText("");
         txtTenPhong.setText("");
         cboLoaiPhong.setSelectedItem(1);
         cboTrangThai.setSelectedItem(1);
     }//GEN-LAST:event_btnLamMoiActionPerformed

     private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
         int ret = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn thoát chứ?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
         if (ret == JOptionPane.YES_OPTION) {

             System.exit(0);
         } else {
             JOptionPane.showMessageDialog(this, "Thao tác Exit đã hủy");
         }
     }//GEN-LAST:event_btnThoatActionPerformed

     private void btnMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMainActionPerformed
         new QuanLyPhong().setVisible(false);
         this.dispose();
         new main().setVisible(true);
     }//GEN-LAST:event_btnMainActionPerformed

     private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
         int ret = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn thoát chứ?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
         if (ret == JOptionPane.YES_OPTION) {
             System.exit(0);
         } else {
             JOptionPane.showMessageDialog(this, "Thao tác Exit đã hủy");
             setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
         }
     }//GEN-LAST:event_formWindowClosing

     private void tblDanhSachPhongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachPhongMouseClicked
         SapXep();
     }//GEN-LAST:event_tblDanhSachPhongMouseClicked

     private void lblHinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHinhMouseClicked

         int width = lblHinh.getWidth();
         int height = lblHinh.getHeight();
         JFileChooser jfc = new JFileChooser("..\\datvtpc04247_Assignment_SOF203\\src\\Hinh");
         jfc.showOpenDialog(null);
         File file = jfc.getSelectedFile();
         filename = file.getAbsolutePath();
         ImageIcon ImIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
         lblHinh.setText("");
         lblHinh.setIcon(ImIcon);
         try {
             File image = new File(filename);
             FileInputStream fis = new FileInputStream(image);
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             byte[] buf = new byte[1024];
             for (int readnum; (readnum = fis.read(buf)) != -1;) {
                 bos.write(buf, 0, readnum);
             }
             anh_phong = bos.toByteArray();
         } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Vui lòng chọn hình");
         }
     }//GEN-LAST:event_lblHinhMouseClicked

     private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
         firstSP();

     }//GEN-LAST:event_btnFirstActionPerformed

     private void btnPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreActionPerformed
         preSP();

     }//GEN-LAST:event_btnPreActionPerformed

     private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
         nextSP();
     }//GEN-LAST:event_btnNextActionPerformed

     private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
         lastSP();
     }//GEN-LAST:event_btnLastActionPerformed

     private void btnChiTietActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChiTietActionPerformed
         dispose();
         new QuanLyChiTiet().setVisible(true);
     }//GEN-LAST:event_btnChiTietActionPerformed

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
            java.util.logging.Logger.getLogger(QuanLyPhong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyPhong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyPhong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyPhong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyPhong().setVisible(false);
                new Login().setVisible(true);

            }
        });
    }

     // Variables declaration - do not modify//GEN-BEGIN:variables
     private javax.swing.JScrollPane SpnlDanhSach;
     private javax.swing.ButtonGroup btgTrangThai;
     private javax.swing.JButton btnCapNhat;
     private javax.swing.JButton btnChiTiet;
     private javax.swing.JButton btnFirst;
     private javax.swing.JButton btnLamMoi;
     private javax.swing.JButton btnLast;
     private javax.swing.JButton btnMain;
     private javax.swing.JButton btnNext;
     private javax.swing.JButton btnPre;
     private javax.swing.JButton btnThem;
     private javax.swing.JButton btnThoat;
     private javax.swing.JButton btnTimKiem;
     private javax.swing.JButton btnXoa;
     private javax.swing.JComboBox<String> cboLoaiPhong;
     private javax.swing.JComboBox<String> cboTrangThai;
     private javax.swing.JLabel lblDiaDiem;
     private javax.swing.JLabel lblGiaPhong;
     private javax.swing.JLabel lblHinh;
     private javax.swing.JLabel lblMaPhong;
     private javax.swing.JLabel lblMaPhong1;
     private javax.swing.JLabel lblMaPhong2;
     private javax.swing.JLabel lblQuanLy;
     private javax.swing.JLabel lblSoLuongO;
     private javax.swing.JLabel lblTime;
     private javax.swing.JLabel lblTrangThai;
     private javax.swing.JPanel pnlChucNang;
     private javax.swing.JPanel pnlTimKiem1;
     private javax.swing.JTable tblDanhSachPhong;
     private javax.swing.JTextField txtDiaDiem;
     private javax.swing.JTextField txtGia;
     private javax.swing.JTextField txtMaPhong;
     private javax.swing.JTextField txtNgay;
     private javax.swing.JTextField txtTenPhong;
     // End of variables declaration//GEN-END:variables
}
