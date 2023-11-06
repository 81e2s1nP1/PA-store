package com.pa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pa.model.Orders;
import com.pa.model.User;
import com.pa.repository.OrdersRepository;

@Service
public class OrdersDao {
		@Autowired
		private OrdersRepository ordersRepository;
		
		public Orders save(Orders orders) {
			return ordersRepository.save(orders);
		}
		
		public List<Orders> getByMyPurchase(User user) {
			return ordersRepository.getMyPurchase(user);
		}
}
