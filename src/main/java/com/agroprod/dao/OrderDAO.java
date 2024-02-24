package com.agroprod.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agroprod.model.Orders;

@Repository
public interface OrderDAO extends JpaRepository<Orders, Integer>
{

}
