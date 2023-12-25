use master
go

CREATE DATABASE QuanLyPhongKhachSan;
USE QuanLyPhongKhachSan;

CREATE TABLE TaiKhoan(
	TenDangNhap nvarchar(255) primary key,
	MatKhau nvarchar(255)
)

CREATE TABLE PhongO(
	MaPhong nvarchar(255) primary key,
	TenPhong nvarchar(255),
	DiaDiem nvarchar(255),
	SoLuongNguoiO int,
	Gia money,
	TrangThai bit
)

CREATE TABLE ChiTietPhong(
	MaPhong nvarchar(255) primary key,
	TenPhong nvarchar(255),
	LoaiPhong nvarchar(255),
	DiaDiem nvarchar(255),
	SoLuongNguoiO int,
	Gia money,
	TrangThai bit,
	NgayO date
)

ALTER TABLE phongo ADD CONSTRAINT maPhong_maPhong_FK FOREIGN KEY (MaPhong) REFERENCES chitietphong(MaPhong)
