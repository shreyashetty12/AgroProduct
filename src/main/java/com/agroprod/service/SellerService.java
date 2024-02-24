package com.agroprod.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agroprod.dao.SellerDAO;
import com.agroprod.model.Buyer;
import com.agroprod.model.Seller;
import com.agroprod.model.User;

@Service
public class SellerService {
	@Autowired
	private SellerDAO sellerDAO;

	@Autowired
	private UserService userService;

	public List<Seller> findAll() {
		return sellerDAO.findAll();
	}

	public void save(Seller theSeller) {
		sellerDAO.save(theSeller);
		User user = new User(theSeller.getSellerName(), theSeller.getSellerPassword(), "SELLER");
		userService.save(user);
		System.out.println("Saved User : " + user);
	}

	public Seller getCurrentSeller() {
		String currentUsername = userService.getCurrentUsername();
		Seller currentSeller = sellerDAO.findBySellerName(currentUsername);
		return currentSeller;
	}

	public boolean isSeller() {
		if (getCurrentSeller() == null) {
			return false;
		}
		return true;
	}

	public Seller findBySellerId(int sellerId) {
		return sellerDAO.findBySellerId(sellerId);
	}

	public boolean existingSeller(String name) {
		boolean existingSeller = false;
		Seller seller = sellerDAO.findBySellerName(name);

		if (seller != null) {
			existingSeller = seller.getSellerName().equalsIgnoreCase(name);
		}

		return existingSeller;

	}
}
