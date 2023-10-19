package com.pa.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private int id; 
	@NotNull
	@Size(min = 5, max = 30)
	private String username;
	@NotNull
	@Size(min = 10, max = 50)
	private String address;
	@PastOrPresent
	private Date ngaysinh;
	@NotNull
	@Size(min = 10, max = 11)
	private String phone;
	@NotNull
	private String sex;
	private boolean enable;
	private String verification; 
	
	@OneToOne(targetEntity = Account.class, cascade = CascadeType.ALL)
	private Account account;
	
	@OneToMany(targetEntity = Orders.class, cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private Set<Orders> donHang;

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public Date getNgaysinh() {
		return ngaysinh;
	}



	public void setNgaysinh(Date ngaysinh) {
		this.ngaysinh = ngaysinh;
	}



	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSex() {
		return sex;
	}



	public void setSex(String sex) {
		this.sex = sex;
	}

	public Set<Orders> getDonHang() {
		return donHang;
	}

	public void setDonHang(Set<Orders> donHang) {
		this.donHang = donHang;
	}
	
	public Account getAccount() {
		return account;
	}



	public void setAccount(Account account) {
		this.account = account;
	}

	public boolean isEnable() {
		return enable;
	}



	public void setEnable(boolean enable) {
		this.enable = enable;
	}



	public String getVerification() {
		return verification;
	}



	public void setVerification(String verification) {
		this.verification = verification;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", address=" + address + ", ngaysinh=" + ngaysinh
				+ ", phone=" + phone + ", sex=" + sex + ", donHang=" + donHang + "]";
	}
	
}
