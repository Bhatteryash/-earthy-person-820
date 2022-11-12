package com.Shopping.Services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Shopping.Exception.CartException;
import com.Shopping.Exception.CustomerException;
import com.Shopping.Model.Cart;
import com.Shopping.Model.Customer;
import com.Shopping.Model.Product;
import com.Shopping.Model.SellerProducts;
import com.Shopping.Repository.CartRepo;
import com.Shopping.Repository.CustomerRepo;
import com.Shopping.Repository.ProductRepo;
import com.Shopping.Repository.SellerProductRepo;

import lombok.Data;

@Service
public class CartServiceImple implements CartService {
	
	@Autowired
	private CartRepo cartrepo;
	
	@Autowired
	private CustomerRepo crepo;
	
	@Autowired
	private ProductRepo prepo;
 
	@Autowired
	private SellerProductRepo spr;
	
	@Override
	public Product removeproductFromCart(Integer pid,String key ,Integer cid ) throws CustomerException {
		
		 Optional<Customer> opt = crepo.findById(cid);
		 
		 if(opt.isPresent()) {
			
              Customer cur = opt.get();
			 
			 Cart cart_cus= cur.getCart();
		
			 List<Product> li= cart_cus.getProductList();
			 
			boolean flag=false;
			 
			 for(int i=0;i<li.size();i++){
				 if(li.get(i).getProductId()==pid) {
					 li.remove(li.get(i));
					 flag=true;
					 break;
				 }
			 }
			 
			 cart_cus.setProductList(li);
			 
			Optional<Product> optp= prepo.findById(pid);
			if(optp.isPresent()) {
				Product pro=optp.get();
				prepo.delete(pro);
				 return pro;
			}
		 else {
				throw new CustomerException("Inavlid product Id");
			}
			 
			
		 }
		  
		throw new CustomerException("Invalid customerId");
	}
	
	

	@Override
	public Cart updateProductQuantity(Integer cid, Product product, Integer quantity, String key) throws CustomerException{
				
		Optional<Customer> opt = crepo.findById(cid);
		
		 if(opt.isPresent()) {
			 Customer cur = opt.get();
			 
			 Cart cart_cus= cur.getCart();
			 List<Product> li= cart_cus.getProductList();
			 
				boolean flag=false;
				 
				 for(int i=0;i<li.size();i++){
					 if(li.get(i).getProductId()==product.getProductId()) {
						 li.get(i).setQuantity(li.get(i).getQuantity()+quantity);
						 flag=true;
						 break;
					 }
				 }
				 if(!flag) throw new CustomerException("Product Not found");
				 
				 cart_cus.setProductList(li);
				 cur.setCart(cart_cus);
				 crepo.save(cur);
		 
				 return cart_cus;
		 }
				 throw new CustomerException("Invalid customerId");
}

	@Override
	public String addProductToCart(Integer pid, Integer cusId, String key) throws CustomerException {
		
		Optional<SellerProducts> Spopt= spr.findById(pid);
		
		  Customer customer= crepo.findByCustomerId(cusId);
		  
		  if(Spopt.isPresent()&&customer!=null) {
			  
			  SellerProducts sproduct=Spopt.get();
			  
			  Product p= new Product();
			  System.out.println("Inside");
//			  p.setProductId(sproduct.getProductId());
			  p.setCategory(sproduct.getCategory());
				p.setColor(sproduct.getColour());
				p.setDimension(sproduct.getDimension());
				p.setManufacturer(sproduct.getManufacutrer());
				p.setPrice(sproduct.getPrice());
				p.setProductName(sproduct.getProductName());
				p.setQuantity(1);
				p.setSpecification(sproduct.getSpecification());
				
//				Product pro= prepo.save(p);
				
				if(customer.getCart()==null) {
					Cart c=new Cart();
					customer.setCart(c);
					c.setCustomer(customer);
					p.setCart(c);
					customer.getCart().getProductList().add(p);
					crepo.save(customer);
				
				}else {
					
					customer.getCart().setCustomer(customer);
					p.setCart(customer.getCart());
					customer.getCart().getProductList().add(p);
					crepo.save(customer);
					
				}
				return "Added to cart";
		  }
		
		
		throw new CustomerException("Invalid Credentials...");
		 
	}
}