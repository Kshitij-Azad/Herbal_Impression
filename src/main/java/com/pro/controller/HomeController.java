package com.pro.controller;

import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pro.dao.UserRepository;
import com.pro.entites.User;
import com.pro.helper.Message;
import com.pro.services.EmailService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private EmailService emailService;
	Random random = new Random(1000);
	
	/* Home Page */ 
	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title", "Home-Herbal Impression");
		return "home";
	}
	
	/* LOGIN */
	@RequestMapping("/login")
	public String login(Model model)
	{
		model.addAttribute("title", "Login-Herbal Impression");
		return "login";
	}
	
	/* Create new Account */
	@RequestMapping("/Register")
	public String Register(Model model)
	{
		model.addAttribute("title", "Register-Herbal Impression");
		model.addAttribute("user",new User());
		return "Register";
	}
	
	/* Handle for register */
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,
	                           @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
	                           Model model, BindingResult result1, HttpSession session) {
	    try {
	        if (!agreement) {
	            throw new Exception("You have not agreed to the terms and conditions");
	        }
	        
	        if(result1.hasErrors())
			{
				System.out.println("ERROR"+result1.toString());
				model.addAttribute("user",user);
				return "Register";
			}

	        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	        user.setEnabled(true);
	        user.setRole(user.getRole());
	        
	        System.out.println("Agreement: " + agreement);
	        System.out.println("User: " + user);
	        
	        User result = this.userRepo.save(user);
	        
	        model.addAttribute("user", new User());
	        session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
//	        return "redirect:/login"; // Redirect to login page after successful registration
	        return "Register";
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("user", user);
	        session.setAttribute("message", new Message("Something Went wrong !! " + e.getMessage(), "alert-danger"));
	        return "Register"; // Return to registration page with error message
	    }
	}
	
	@RequestMapping("/forgot")
	public String forgot(Model model)
	{
		model.addAttribute("title", "Register-Herbal Impression");
		model.addAttribute("user",new User());
		return "forgot";
	}
	
	@PostMapping("/verify")
	public String verify(@RequestParam("email")String email,HttpSession session)
	{
		System.out.println("Email "+email);
		
		//generating 4 digit otp
		
		 int otp = 1000 + random.nextInt(9000);
		System.out.println("OTP "+otp);
		
		//write code for send otp email
		
		String subject = "OTP From Herbal IMpression";
		String message = "<h1> OTP = "+otp+" </h1>";
		String to=email;
		boolean flag= this.emailService.sendEmail(message, subject, to);
		
		if(flag)
		{
			session.setAttribute("myotp",otp);
			session.setAttribute("email", email);
			return "verify";
		}
		else
		{
			return "forgot";
		}
	}
	@PostMapping("/verify_otp")
	public String verify_otp(@RequestParam("otp") int otp,HttpSession session )
	{
		int myOtp =(int) session.getAttribute("myotp");
		String email= (String) session.getAttribute("email");
		if(otp==myOtp)
		{
			return "login";
		}
		else
		{
			return "home";
		}
	}
	
}
