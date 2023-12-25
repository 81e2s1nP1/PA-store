package com.pa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pa.model.Orders;
import com.pa.model.Product;
import com.pa.model.User;
import com.pa.repository.OrdersRepository;
import com.pa.repository.Product_Repository;

@Service
public class OrdersDao {
		@Autowired
		private OrdersRepository ordersRepository;
		
		@Autowired
		private Product_Repository product_Repository;
		
		public Orders save(Orders orders) {
			return ordersRepository.save(orders);
		}
		
		public List<Orders> getByMyPurchase(User user) {
			return ordersRepository.getMyPurchase(user);
		}
		
		public Orders updateOrder(Orders orders, int id)  {
			Orders ord = ordersRepository.findById(id).get();
			if(ord != null) {
				ord.setId(id);
				ord.setAddress(orders.getAddress());
				ord.setSize(orders.getSize());
				Orders updatedOrder = ordersRepository.save(ord);
				return updatedOrder;
			}else {
				return null;
			}
		}
		
		public Boolean deleteOrder(int id) {
			Orders order = ordersRepository.findById(id).get();
			boolean isDelete = false; 
			if(order != null ) {
				ordersRepository.delete(order);
				isDelete = true;
			}
			return isDelete;
		}
		
		public List<Product> findTop4Ordered() {
			if(ordersRepository.findTop4ProductsByCount().size() >= 4) {
				return ordersRepository.findTop4ProductsByCount().subList(0, 4);
			}else {				
				return product_Repository.get4ProductFirst().subList(0, 4);
			}
		}
		
		public List<Orders> getOrdersByPage(int page, int pageSize, User user) {
			List<Orders> orders = ordersRepository.getOrdersByPage(page, pageSize , user.getId());
	        return orders;
	    }
		
		public List<Orders> getOrdersByPageAdmin(int page, int pageSize) {
			List<Orders> orders = ordersRepository.getOrdersByPageAdmin(page, pageSize);
	        return orders;
	    }
}
