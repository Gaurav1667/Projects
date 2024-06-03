package com.smart.smartContactManager.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.smartContactManager.Helper.Message;
import com.smart.smartContactManager.dao.ContactRepository;
import com.smart.smartContactManager.dao.UserRepository;
import com.smart.smartContactManager.entities.Contact;
import com.smart.smartContactManager.entities.User;
//import org.springframework.boot.actuate.trace.http.HttpTrace.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	

	@Autowired
	private ContactRepository contactRepository;
	
	  @ModelAttribute
	  public void addCommonData(Model model, Principal principal)
	  {
		  String UserName = principal.getName();
		  System.out.println("UserName "+UserName); 
		 
		  
		  //get User using username(email)
		  User user = userRepository.getUserByUserName(UserName);
		
		  model.addAttribute("user",user);
		
		  System.out.println("USER "+user);
	  }
	
	
	  @RequestMapping("/index") 
	  public String dashboard(Model model,Principal principal) 
	  { 
		  return "normal/user_dashboard";
	  }
	  
	  @GetMapping("/add-contact")
	  public String openAddContatctForm(Model model)
	  {
		  model.addAttribute("title","Add Contact smart Contact manager");
		  model.addAttribute("contact",new Contact());
		  return "normal/add_contact_form";
	  }
	  
	  //Handler processing add contatct form
	  @PostMapping("/process-contact")
	  public String processContact(@ModelAttribute Contact contact ,
			  @RequestParam("profileImage")
			  MultipartFile file,
			  Principal principal,HttpSession session) throws IOException
	  { 
		  
		  if(file.isEmpty())
		  {
			  //if empty then print message
			  System.out.println("Image is empty");
			  contact.setImage("contact.png");
		  }
		  else
		  {
			  //process to store image in database
			  
			  //getOriginalFilename() is used to get original file name 
			  contact.setImage(file.getOriginalFilename());
			  
			  //path to where store image
			  File savefile = new ClassPathResource("static/img").getFile();
			  
			  
			  Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			  
			  
			  Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			  
			  System.out.println("Image is uploaded");
			 
		  }
		  try {
		  String name = principal.getName();
		  User user = this.userRepository.getUserByUserName(name);
		  contact.setUser(user);
		  user.getContacts().add(contact);
		  this.userRepository.save(user);
		  System.out.println("Contact Information "+contact);
		  System.out.println("into database");
		  
		  session.setAttribute("message",new Message("Contact Sucessfully Added..!!","success"));
		  }
		  catch(Exception e)
		  {
			  System.out.println("ERROR  "+e.getMessage());
			  e.printStackTrace();
			  
			  session.setAttribute("message",new Message("Something Went Wrong..!! Try Again","danger"));
		  }
		  return "normal/add_contact_form";
	  }
	  
	            //Display Contacts
	            //current page = [page]
			  @GetMapping("/show-contacts/{page}")
			  public String showContacts(@PathVariable("page") Integer page, Model model,Principal principal)
			  {
				  model.addAttribute("title","Show Contact Smart Contact Manager");
				  String UserName = principal.getName();
			
				  User user = this.userRepository.getUserByUserName(UserName);
				  
				  Pageable pageable = PageRequest.of(page,5);
				 
				  Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(),pageable);
				 
				  model.addAttribute("contacts",contacts);
				  model.addAttribute("currentpage", page);
				  model.addAttribute("Totalpages",contacts.getTotalPages());
				  
				  return "normal/show_contacts";
			   }
			  
			  
			  @RequestMapping("/{cid}/contact")
			  public String showContactDetail(@PathVariable("cid") Integer cid,Model model ,Principal principal)
			  {
				  model.addAttribute("title","User Profile Smart Contact Manager");
				  System.out.println("CID"+cid);
				  Optional<Contact> contactOptional = this.contactRepository.findById(cid);
				  Contact contact = contactOptional.get(); 
				  
				  String username = principal.getName();
				  User user = this.userRepository.getUserByUserName(username);
				  
				  if(user.getId() == contact.getUser().getId())
				   model.addAttribute("contact",contact);
				 
				   return "normal/contact_detail";
				  
			  }
			  
			  @GetMapping("delete/{cid}")
			  public String deleteContact(@PathVariable("cid") Integer cid, Model model,HttpSession session,Principal principal)
			  {
				  Contact contact = this.contactRepository.findById(cid).get();
				  
				  User user = this.userRepository.getUserByUserName(principal.getName());
				  
				  user.getContacts().remove(contact);
				  
				  this.userRepository.save(user);
				  
				  
				  contact.setUser(null);
				  
				 // this.contactRepository.delete(contact);
				  System.out.println(cid+ " Contact deleted Successfully");
				  session.setAttribute("message", new Message("Contact deleted successfully","success"));
				  
				  return "redirect:/user/show-contacts/0";
				  
			  }
			  
			  
			  //open update form handler
			  @PostMapping("/update-contact/{cid}")
			  public String updateForm(@PathVariable("cid") Integer cid , Model model)
			  {
				  Contact contact =  this.contactRepository.findById(cid).get();
				  model.addAttribute("title","Form Update Smart Conatact Manager");
				  model.addAttribute("contact",contact);
				  
				  return "normal/update_form";
			  }
			  
			 @RequestMapping(value = "/process-update" ,method = RequestMethod.POST)
			  public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,Principal principal,
					  Model model,HttpSession session)
			  {
				 try {
					 
					 Contact oldContactDetails = this.contactRepository.findById(contact.getCid()).get();	
					 
					 if(!file.isEmpty())
					 {
						 
						 //delete old Photo
						 
						 File deleteFile = new ClassPathResource("static/img").getFile();
						 
						 File file1 = new File(deleteFile , oldContactDetails.getImage());
						 
						 file1.delete();
						 
						 
						 
						 
						 //upload new Photo
						 File savefile = new ClassPathResource("static/img").getFile();
						  
						  
						  Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
						  
						  
						  Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
						  
						  contact.setImage(file.getOriginalFilename());
						  
						  System.out.println("Image is uploaded");
					 }
					 else
					 {
						 contact.setImage(file.getOriginalFilename());
						  
					 }
					 
					 User user = userRepository.getUserByUserName(principal.getName());
					 
					 contact.setUser(user);
					 
					 this.contactRepository.save(contact);
					 
					 session.setAttribute("message", new Message("Contact saved Successfully..","success"));
					 
				 } catch(Exception e)
					 {
						 e.printStackTrace();	
					 }
				 
				  System.out.println("Contact Name "+contact.getName());
				  System.out.println("Contact Id "+contact.getCid());
				  return "redirect:/user/"+contact.getCid()+"/contact";
			  }
}




