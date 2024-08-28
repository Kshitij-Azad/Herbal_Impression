package com.pro.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pro.dao.ProductRepository;
import com.pro.dao.UserRepository;
import com.pro.entites.Product;
import com.pro.entites.User;

@Controller
@RequestMapping("/user")
public class UserController 
{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	
	
	
	/* Homepage */
	@RequestMapping("/index")
	public String dashboard()
	{
		return "user/user_dashboard";
	}
	
	/* Profile */
	@RequestMapping("/profile")
	public String profile(Model model,Principal principal) 
	{
		try 
		{
			String userName = principal.getName();
	        System.out.println("USERNAME " + userName);
	        //get all data by username(email)
	        User user =this.userRepository.getUserByUserName(userName);
	        System.out.println("USER "+user);
	        model.addAttribute("user", user);
	        
	        return "user/userprofile";
	    } 
		catch (Exception e) 
		{
	        // Log the exception
	        e.printStackTrace();
	        // Optionally, return an error view or redirect to an error page
	        return "error";
	    }
	 }
	
	/* Categories */
	@RequestMapping("/hair")
	public String hair(Model m,Principal principal)
	{
		try {
			//User list
			String userName = principal.getName();
			User user = this.userRepository.getUserByUserName(userName);
			List<Product> productList = this.productRepository.findCategoryByhair("hair");
			m.addAttribute("productList", productList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "user/hair";
	}
	@RequestMapping("/body")
	public String body(Model m,Principal principal)
	{
		try {
			//User list
			String userName = principal.getName();
			User user = this.userRepository.getUserByUserName(userName);
			List<Product> productList = this.productRepository.findCategoryBybody("body");
			m.addAttribute("productList", productList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "user/body";
	}
	@RequestMapping("/face")
	public String face(Model m,Principal principal)
	{
		try {
			//User list
			String userName = principal.getName();
			User user = this.userRepository.getUserByUserName(userName);
			List<Product> productList = this.productRepository.findCategoryByface("face");
			m.addAttribute("productList", productList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "user/face";
	}
	@RequestMapping("/lips")
	public String lips(Model m,Principal principal)
	{
		try {
			//User list
			String userName = principal.getName();
			User user = this.userRepository.getUserByUserName(userName);
			List<Product> productList = this.productRepository.findCategoryBylips("lips");
			m.addAttribute("productList", productList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "user/lips";
	}
	@RequestMapping("/other")
	public String other(Model m,Principal principal)
	{
		try {
			//User list
			String userName = principal.getName();
			User user = this.userRepository.getUserByUserName(userName);
			List<Product> productList = this.productRepository.findCategoryByother("other");
			m.addAttribute("productList", productList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "user/other";
	}
	
	/* Cart */
	@RequestMapping("/cart")
	public String cart()
	{
		return "user/cart";
	}
	
	/* Product */
	@RequestMapping("/product")
	public String product(Model m,Principal principal )
	{
		try {
			List<Product> productList = this.productRepository.getAllProducts();
			m.addAttribute("productList", productList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "user/allProducts";
	}
}
