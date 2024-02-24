package com.agroprod.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.agroprod.model.Product;
import com.agroprod.model.Stock;

@Repository
@Transactional
public interface StockDAO extends JpaRepository<Stock, Integer> {
	public Stock findByStockId(int stockId);

	@Modifying // to mark delete or update query
	@Query(value = "DELETE FROM Stock s WHERE s.stockId = :stockId") // it will delete all the record with specific name
	int deleteBystockId(@Param("stockId") int stockId);

}