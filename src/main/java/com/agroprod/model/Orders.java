package com.agroprod.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Orders {
	@Id
	@Column(name = "orderId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int orderId;

	@Column(name = "date")
	@CreationTimestamp
	Date date;
	@Column(name = "reqPrice")
	int requestedPrice;

	@Column(name = "reqQuantity")
	int requestedQuantity;

	@Column(name = "amountPaid")
	int amountPaid;

	@Column(name = "availableStock")
	int availableStock;

	@Column(name = "status")
	String status;

	@Column(name = "acceptedTime")
	String acceptedTime;

	@ManyToOne
	private Buyer buyer;

	@ManyToMany
	@JoinTable(name = "ORDER_PRODUCT", joinColumns = @JoinColumn(name = "orderId"), inverseJoinColumns = @JoinColumn(name = "productId"))
	private List<Product> products = new ArrayList<Product>();

	public String getAcceptedTime() {
		return acceptedTime;
	}

	public void setAcceptedTime(String acceptedTime) {
		this.acceptedTime = acceptedTime;
	}

	public int getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(int availableStock) {
		this.availableStock = availableStock;
	}

	public int getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(int amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getRequestedQuantity() {
		return requestedQuantity;
	}

	public void setRequestedQuantity(int requestedQuantity) {
		this.requestedQuantity = requestedQuantity;
	}

	public int getRequestedPrice() {
		return requestedPrice;
	}

	public void setRequestedPrice(int requestedPrice) {
		this.requestedPrice = requestedPrice;
	}

	public Orders() {
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Buyer getBuyer() {
		return buyer;
	}

	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}

	@Override
	public String toString() {
		return "Orders [orderId=" + orderId + ", date=" + date + ", requestedPrice=" + requestedPrice
				+ ", requestedQuantity=" + requestedQuantity + ", amountPaid=" + amountPaid + ", availableStock="
				+ availableStock + ", status=" + status + ", acceptedTime=" + acceptedTime + ", buyer=" + buyer
				+ ", products=" + products + "]";
	}

}
