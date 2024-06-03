package com.smart.smartContactManager.Controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import com.smart.smartContactManager.Helper.Message;
import com.smart.smartContactManager.dao.UserRepository;

import com.smart.smartContactManager.entities.User;


@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userepository;
	
	@RequestMapping("/")
	public String Home(Model model)
	{
		model.addAttribute("title","Home- Smart Contact Manager");
		return "Home";
		
	}
	
	@RequestMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title","About- Smart Contact Manager");
		return "About";
		
	}
	
	@RequestMapping("/signup")
	public String signUp(Model model)
	{
		model.addAttribute("title","Sign Up- Smart Contact Manager");
		 model.addAttribute("user",new User()); 
		return "signUp";
		
	}
	
	 @RequestMapping("/signin")
	 public String customLogin(Model model)
	 {
		 model.addAttribute("title","Custom Login- Smart Contact Manager");
		 return "login";
	 }
	
	 @RequestMapping(value="/do_register" ,method=RequestMethod.POST)
	  public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
			 @RequestParam(value = "agreement",defaultValue = "false") 
	  boolean agreement ,Model model, HttpSession session)
	  {
		 
		try {
			
			 if(!agreement)
			 {
				 System.out.println("You Have Not Accepted Terms and Conditions");
				 throw new  Exception("You Have Not Accepted Terms and Conditions");
			 }
			 
			 if(result1.hasErrors())
			 {
				 System.out.println();
				 model.addAttribute("user", user);
				 return "signup";
			 }
			 
			 user.setRole("ROLE_USER");
			 user.setEnabled(true);
			 user.setImageUrl("default.png");
			 user.setPassword(passwordEncoder.encode(user.getPassword()));
			 System.out.println("Agreement "+agreement);
			 System.out.println("USER" +user);
			 
			 User result =  this.userepository.save(user);
			 model.addAttribute("user",new User());
			 
			 session.setAttribute("message", new Message("Successfully Registered !!","alert-success"));
			  
			 return "signUp";
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went Wrong !! " +e.getMessage(),"alert-danger"));
			return "signup";
		}
	  }
	 
	
	/*@Autowired
	private UserRepository userepo;
	
@GetMapping("/FirstPage")
@ResponseBody
//public String Test() {
	
	//User user = new User();
	//user.setName("ABC");
	//user.setEmail("ABC@GMAIL.COM");
	
	
	 * Contact conatct = new Contact(); user.getContacts().add(conatct);
	 
	
	//userepo.save(user);
	return "Working Fine";*/
}
