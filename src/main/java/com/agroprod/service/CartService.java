package com.agroprod.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agroprod.dao.CartDAO;
import com.agroprod.model.Buyer;
import com.agroprod.model.Cart;

@Service
public class CartService 
{
	@Autowired
	private CartDAO cartDAO;
	
	@Autowired
	private BuyerService buyerService;
	
	public void createBuyerCart(Buyer buyer)
	{
		Cart theCart = new Cart();
		cartDAO.save(theCart);
		buyer.setCart(theCart);
		buyerService.save(buyer);
	}
	
	public Cart getCart()
	{
		Buyer currentBuyer = buyerService.getCurrentBuyer();
		Cart theCart = currentBuyer.getCart();
		return theCart;
	}
	
	public void save(Cart theCart)
	{
		cartDAO.save(theCart);
	}
}
