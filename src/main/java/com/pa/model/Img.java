package com.pa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Img {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Lob
	private String pic;
	@ManyToOne
	private Product product;
	
	public Img() {
	}
	public Img(String pic, Product product) {
		this.pic = pic;
		this.product = product;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Product getP() {
		return product;
	}
	public void setP(Product p) {
		this.product = p;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	@Override
	public String toString() {
		return "Img [id=" + id + ", pic=" + pic + ", product=" + product + "]";
	}
}
