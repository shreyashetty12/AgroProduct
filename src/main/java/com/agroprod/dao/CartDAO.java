package com.agroprod.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agroprod.model.Cart;

@Repository
public interface CartDAO extends JpaRepository<Cart, Integer> 
{
	//public Cart findByBuyerId(int buyerId);
}
