package com.pa.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;


@Entity
public class Orders {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private int id;
	@CreationTimestamp
	@NotNull
	private Date ngaydat;
	@NotNull
	private int tong_tien;
	@OneToOne
	private Product product;
	@ManyToOne 
	private User user;
	@NotNull
	private String size;
	@NotNull
	private String sdt;
	@NotNull
	private String address;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getNgaydat() {
		return ngaydat;
	}
	public void setNgaydat(Date ngaydat) {
		this.ngaydat = ngaydat;
	}
	public int getTong_tien() {
		return tong_tien;
	}
	public void setTong_tien(int i) {
		this.tong_tien = i;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getSdt() {
		return sdt;
	}
	public void setSdt(String sdt) {
		this.sdt = sdt;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "Orders [id=" + id + ", ngaydat=" + ngaydat + ", tong_tien=" + tong_tien + ", product=" + product
				+ ", user=" + user + ", size=" + size + ", sdt=" + sdt + ", address=" + address + "]";
	}
	
}
