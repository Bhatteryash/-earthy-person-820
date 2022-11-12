package com.Shopping.Services;

import org.springframework.stereotype.Service;

import com.Shopping.Exception.CartException;
import com.Shopping.Exception.CustomerException;
import com.Shopping.Model.Cart;
import com.Shopping.Model.Product;

@Service
public interface CartService {
	
	
public Product removeproductFromCart(Integer pid,String key ,Integer cid) throws Exception;
	
	
	public Cart updateProductQuantity(Integer cid, Product product, Integer quantity, String key) throws Exception;
	
	public String addProductToCart(Integer pid, Integer cusId, String key) throws CustomerException;
}
