package com.smart.smartContactManager.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.smartContactManager.entities.Contact;
import com.smart.smartContactManager.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer>  {
	//A Page is a sublist of a list of objects. 
	//It allows gain information about the position of it in the containing entire list.
	@Query(value = "from Contact as c where c.user.id = :userId")
	public Page<Contact> findContactByUser (@Param("userId") int userId ,Pageable pepageable);
	
	//search functionality
	public List<Contact> findByNameContainingAndUser(String name,User user);
	
	
}
