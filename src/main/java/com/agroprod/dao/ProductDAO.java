package com.agroprod.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.agroprod.model.Product;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> 
{
	public Product findByProductId(int productId);
	
//	@Modifying
//	@Query("update Product p set p.active = false where p.id  :date")
//	void updateProductById(@Param("id") int id);
}
