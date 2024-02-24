package com.agroprod.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Product")
public class Product {
	@Id
	@Column(name = "productId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int productId;

	@Column(name = "productName")
	String productName;

	@Column(name = "productPrice")
	int productPrice;

	@Column(name = "productDesc")
	String productDesc;

	@Column(name = "productImage")
	String productImage;

	@ManyToMany(mappedBy = "products")
	private List<Cart> carts = new ArrayList<Cart>();

	@ManyToMany(mappedBy = "products")
	private List<Orders> orders = new ArrayList<Orders>();

	@OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
	private Stock stock;
	
	
	public Product() {
	}

	public Product(String productName, int productPrice, String productDesc,String productImage) {
		this.productName = productName;
		this.productPrice = productPrice;
		this.productDesc = productDesc;
		this.productImage = productImage;
	}

	
	
	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productPrice=" + productPrice
				+ ", productDesc=" + productDesc + ",productImage=" + productImage + "]";
	}

	public int getProductId() {
		return productId;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
}
