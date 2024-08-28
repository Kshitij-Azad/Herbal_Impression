package com.pro.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pro.dao.ProductRepository;
import com.pro.dao.UserRepository;
import com.pro.entites.Product;
import com.pro.entites.User;
import com.pro.helper.Message;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class AdminController 
{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private ProductRepository productRepository;
	
	/* HomePage --------------------------------------------------------------------------->*/
	@RequestMapping("/index")
	public String dashboard()
	{
		return "admin/home";
	}
	
	/* Order --------------------------------------------------------------------------->*/
	
	/* New Order */
	@RequestMapping("/newOrder")
	public String newOrder()
	{
		return "admin/newOrder";
	}

	/* Pending Order */
	@RequestMapping("/pendingOrder")
	public String pendingOrder()
	{
		return "admin/pendingOrder";
	}
	
	/* Complete Order */
	@RequestMapping("/completeOrder")
	public String completeOrder()
	{
		return "admin/completeOrder";
	}
	
	
	
	/* Product---------------------------------------------------------------------------------------------------------------> */
	
	/* Add Product */
	@RequestMapping("/addProduct")
	public String addProduct()
	{
		return "admin/addProduct";
	}
	
	/* Process To Add Product */
	@PostMapping("/process-product")
	public String processProduct(@ModelAttribute Product product,@RequestParam("img") MultipartFile file, Principal principal,HttpSession session)
	{
		try 
		{
			String name  = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
			user.getProduct().add(product);
			product.setUser(user);
			
			//processing and uploading file
			
			if(file.isEmpty())
			{
				//if the file is empty
			}
			else
			{
				//upload the file
				product.setImage(file.getOriginalFilename());
				File savefile =	new ClassPathResource("static/image").getFile();
				Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			
			this.userRepository.save(user);
			System.out.println("Data "+product);
			System.out.println("Added to Data Base");
			//message success......
			session.setAttribute("message", new Message("Product Added ","success"));
			
		} 
		catch (Exception e) 
		{
			System.out.println("Error "+e.getMessage());
			e.printStackTrace();
			//message Fail......
			session.setAttribute("message", new Message("Something went wrong..","danger"));
		}
		return "admin/addProduct";
	}
	
	/* Modify Product */
	
	@RequestMapping("/modifyProduct")
	public String modifyProduct(Model m,Principal principal)
	{
		try {
			List<Product> productList = this.productRepository.getAllProducts();
			m.addAttribute("productList", productList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "admin/modifyProduct";
	}
	@GetMapping("/delete1/{pid}")
	public String deleteProduct(@PathVariable("pid")Integer pid,Model model,HttpSession httpSession)
	{
		Optional<Product> productId = this.productRepository.findById(pid);
		Product product = productId.get();
		this.productRepository.delete(product);
		httpSession.setAttribute("message", new Message("Delete Product Successfully","success"));		
		
		return "redirect:/admin/modifyProduct";
	}
	
	//open update Product
		@PostMapping("/updateProduct/{pid}")
		public String updateFormProduct(@PathVariable("pid")Integer pid,Model model)
		{
			model.addAttribute("title","Update Product");
			Product product = this.productRepository.findById(pid).get();
			
			model.addAttribute("product", product);
			return "admin/updateProduct";
		}
		
		
		@PostMapping("/updatePropductprocess")
		public String updateFormProductProcess(@ModelAttribute Product product)
		{
			 Product existingProduct = this.productRepository.findById(product.getPid())
                     .orElseThrow(() -> new IllegalArgumentException("Invalid product Id"));
			 existingProduct.setPname(product.getPname());
		     existingProduct.setDescription(product.getDescription());
		     existingProduct.setCategory(product.getCategory());
		     existingProduct.setPrice(product.getPrice());
			this.productRepository.save(existingProduct);
			return"redirect:/admin/modifyProduct";
		}
	
	
	
	
	/* All Product */
	@RequestMapping("/showAllProduct")
	public String showAllProduct(Model m,Principal principal)
	{
		try {
			List<Product> productList = this.productRepository.getAllProducts();
			m.addAttribute("productList", productList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "admin/showAllProduct";
	}
	
	/* No. of User ----------------------------------------------------------------------------------------------------->*/
	@RequestMapping("/userlist")
	public String userlist(Model m,Principal principal)
	{
		try {
			//User list
			String userName = principal.getName();
			User user = this.userRepository.getUserByUserName(userName);
			List<User> userList = this.userRepository.findRoleByUser("ROLE_USER");
			m.addAttribute("userList", userList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "admin/userlist";
	}
	
	/* ADMIN ------------------------------------------------------------------------------------------------------------->*/
	/* Add Admin */
	@RequestMapping("/addadmin")
	public String addadmin()
	{
		return "admin/addadmin";
	}
	
	/* Process To Add Admin */
	@PostMapping("/process-admin")
	public String processAdmin(@RequestParam String name, @RequestParam String email, @RequestParam String role,@RequestParam String password,HttpSession session)
	{
		try 
		{
			User u1 = new User();
			u1.setName(name);
			u1.setEmail(email);
			u1.setPassword(bCryptPasswordEncoder.encode(password));
			u1.setRole(role);
			userRepository.save(u1);
			//message success
			session.setAttribute("message", new Message("Admin Added ","success"));
		} 
		catch (Exception e) 
		{
			System.out.println("Error "+e.getMessage());
			e.printStackTrace();
			//message Fail......
			session.setAttribute("message", new Message("Something went wrong..","danger"));
		}
		
		return "admin/addadmin";
	}

	/* Delete Admin */
	@RequestMapping("/deleteadmin")
	public String deleteadmin(Model m,Principal principal)
	{
		try {
			//Admin list
			String userName = principal.getName();
			User user = this.userRepository.getUserByUserName(userName);
			List<User> adminList = this.userRepository.findRoleBy("ROLE_ADMIN");
			m.addAttribute("adminList", adminList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "admin/deleteadmin";
	}
	
	@PostMapping("/delete/{id}")
	public String deleteAdminData(@PathVariable("id")Integer id,Model model,HttpSession httpSession)
	{
		Optional<User> adminumber = this.userRepository.findById(id);
		User user = adminumber.get();
		this.userRepository.delete(user);
		httpSession.setAttribute("message", new Message("Delete Admin Successfully","success"));		
		
		return "redirect:/admin/deleteadmin";
	}
	
	//open update admin
	@PostMapping("/updateAdmin/{id}")
	public String updateFormAdmin(@PathVariable("id")Integer id,Model model)
	{
		model.addAttribute("title","Update Admin");
		User user = this.userRepository.findById(id).get();
		
		model.addAttribute("user", user);
		return "admin/updateadmin";
	}
	
	@PostMapping("/updateAdminprocess")
	public String updateFormAdminProcess(@ModelAttribute User user,Principal principal)
	{
		User existingUser =this.userRepository.getUserByUserName(principal.getName());
		 existingUser.setName(user.getName());
	     existingUser.setEmail(user.getEmail());
	     existingUser.setRole(user.getRole());
		
		this.userRepository.save(existingUser);
		return"redirect:/admin/deleteadmin";
	}
	
	
	
	/* Show All Admin */
	@RequestMapping("/showAllAdmin")
	public String showAllAdmin(Model m,Principal principal)
	{
		try {
			//Admin list
			String userName = principal.getName();
			User user = this.userRepository.getUserByUserName(userName);
			List<User> adminList = this.userRepository.findRoleBy("ROLE_ADMIN");
			m.addAttribute("adminList", adminList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "admin/showAllAdmin";
	}
	
	
	
	
	
	/* Profile ------------------------------------------------------------------------------------------------------->*/
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
	        
	        return "admin/adminprofile";
	    } 
		catch (Exception e) 
		{
	        // Log the exception
	        e.printStackTrace();
	        // Optionally, return an error view or redirect to an error page
	        return "error";
	    }
	}
	
	
	
	
	
	
}
