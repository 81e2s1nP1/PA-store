package com.pa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Size {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	private String size_ao;
	@NotNull
	@Min(value = 0)
	private int soLuongSize;
	@ManyToOne
	private Product product;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSize_ao() {
		return size_ao;
	}
	public void setSize_ao(String size_ao) {
		this.size_ao = size_ao;
	}
	public int getSoLuongSize() {
		return soLuongSize;
	}
	public void setSoLuongSize(int soLuongSize) {
		this.soLuongSize = soLuongSize;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	@Override
	public String toString() {
		return "Size [id=" + id + ", size_ao=" + size_ao + ", soLuongSize=" + soLuongSize + ", product=" + product
				+ "]";
	} 
}
