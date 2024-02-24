package com.agroprod.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agroprod.dao.StockDAO;
import com.agroprod.model.Product;
import com.agroprod.model.Stock;

@Service
public class StockService {
	@Autowired
	private StockDAO stockDAO;

	public void save(Stock stock) {
		stockDAO.save(stock);
	}

	public Stock findByStockId(int stockId) {
		return stockDAO.findByStockId(stockId);
	}

	public int findByProductId(Product product) {
		int stockId = 0;
		List<Stock> stocks = stockDAO.findAll();

		for (Stock s : stocks) {
			if (s.getProduct().getProductId() == product.getProductId()) {
				stockId = s.getProduct().getProductId();
			}
		}

		return stockId;
	}

	public void delete(Stock theStock) {

		int deleteBystockId = stockDAO.deleteBystockId(theStock.getStockId());

		System.out.println("deleteBystockId " + deleteBystockId);
	}

}
