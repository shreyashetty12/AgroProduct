package com.agroprod.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agroprod.dao.OrderDAO;
import com.agroprod.model.Orders;
import com.agroprod.model.Product;
import com.agroprod.model.Seller;
import com.agroprod.model.Stock;

@Service
public class OrderService {
	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private SellerService sellerService;


	public void save(Orders theOrder) {
		orderDAO.save(theOrder);

	}

	public List<Orders> getGoodPriceForProduct() {
		System.out.println("Inside ");
		Seller currentSeller = sellerService.getCurrentSeller();
		List<Stock> stocks = currentSeller.getStocks();

		List<Orders> allOrders = orderDAO.findAll();

		HashMap<Integer, String> hashMap = new HashMap<>();

		HashMap<Integer, Integer> hashMapFinal = new HashMap<>();

		for (Stock stock : stocks) {
			int stockProdId = stock.getProduct().getProductId();

			for (Orders order : allOrders) {

				List<Product> products = order.getProducts();

				for (Product product : products) {

					if (stockProdId == product.getProductId()) {

//						if (!order.getStatus().equalsIgnoreCase("REJECTED")
//								&& !order.getStatus().equalsIgnoreCase("ACCEPTED")) {

						if (order.getStatus().equalsIgnoreCase("PENDING WITH SELLER")) {

							if (hashMapFinal.containsKey(product.getProductId())) {

								if (order.getRequestedPrice() > hashMapFinal.get(product.getProductId())) {

									hashMapFinal.put(product.getProductId(), order.getOrderId());
								}

							}

							if (!hashMapFinal.containsKey(product.getProductId())) {
								hashMapFinal.put(product.getProductId(), order.getOrderId());
							}

							hashMap.put(order.getOrderId(), product.getProductId() + " " + order.getRequestedPrice());

						}
					}
				}

			}

		}
		System.out.println("hashMap is " + hashMap);
		System.out.println("hashMapFinal is " + hashMapFinal);

		Iterator<Integer> it = hashMapFinal.keySet().iterator();
		List<Integer> arrayList = new ArrayList<>();
		while (it.hasNext()) {
			int key = (int) it.next();
			Integer val = hashMapFinal.get(key);

			arrayList.add(val);
		}

		List<Orders> findAllById = this.orderDAO.findAllById(arrayList);

		System.out.println(findAllById);
		return findAllById;

	}

	public List<Orders> getGoodPriceForProductByProdId(Integer prodID) {
		System.out.println("Inside ");

		List<Orders> allOrders = orderDAO.findAll();

		HashMap<Integer, Integer> hashMap = new HashMap<>();

		for (Orders order : allOrders) {

			List<Product> products = order.getProducts();

			for (Product product : products) {

				if (prodID == product.getProductId()) {

//					if (!order.getStatus().equalsIgnoreCase("REJECTED")
//							&& !order.getStatus().equalsIgnoreCase("ACCEPTED")) {
//						hashMap.put(order.getOrderId(), product.getProductId());
//					}

					if (order.getStatus().equalsIgnoreCase("PENDING WITH SELLER")) {
						hashMap.put(order.getOrderId(), product.getProductId());
					}

				}

			}

		}

		System.out.println("hashMap is " + hashMap);

		Iterator<Integer> it = hashMap.keySet().iterator();
		List<Integer> arrayList = new ArrayList<>();
		while (it.hasNext()) {
			int key = (int) it.next();
			Integer val = hashMap.get(key);

			arrayList.add(key);
		}

		List<Orders> findAllById = this.orderDAO.findAllById(arrayList);

		System.out.println(findAllById);
		return findAllById;

	}

	public Orders getOrderById(Integer orderId) {

		Orders order = orderDAO.getOne(orderId);

		return order;

	}

	public void deletOrderById(Integer orderId) {

		orderDAO.deleteById(orderId);

	}

}
