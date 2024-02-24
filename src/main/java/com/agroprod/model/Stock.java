package com.agroprod.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Stock {
	@Id
	@Column(name = "stockId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int stockId;

	@Column(name = "stockQuantity")
	int stockQuantity;

	@Column(name = "uom")
	String uom;

	@OneToOne
	private Product product;

//	@ManyToMany
//	@JoinTable(name = "STOCK_PRODUCT",
//		joinColumns = @JoinColumn(name = "stockId"),
//		inverseJoinColumns = @JoinColumn(name = "productId"))
//	private List<Product> product = new ArrayList<Product>();

	@ManyToOne
	private Seller seller;

	public Stock() {
	}

	public Stock(int stockQuantity, String uom) {
		this.stockQuantity = stockQuantity;
		this.uom = uom;
	}

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

//	public List<Product> getProduct() {
//		return product;
//	}
//
//	public void setProduct(List<Product> product) {
//		this.product = product;
//	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	@Override
	public String toString() {
		return "Stock [stockId=" + stockId + ", stockQuantity=" + stockQuantity + ", uom=" + uom + ", product="
				+ product + ", seller=" + seller + "]";
	}

}
