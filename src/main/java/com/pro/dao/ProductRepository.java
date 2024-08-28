package com.pro.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pro.entites.Product;
import com.pro.entites.User;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	 @Query("SELECT p FROM Product p")
	 List<Product> getAllProducts();

	 @Query("SELECT u FROM Product u WHERE u.category = 'hair'")
	 public List<Product> findCategoryByhair(@Param("category")String category);
	 
	 @Query("SELECT u FROM Product u WHERE u.category = 'body'")
	 public List<Product> findCategoryBybody(@Param("category")String category);
	 
	 @Query("SELECT u FROM Product u WHERE u.category = 'face'")
	 public List<Product> findCategoryByface(@Param("category")String category);
	 
	 @Query("SELECT u FROM Product u WHERE u.category = 'lips'")
	 public List<Product> findCategoryBylips(@Param("category")String category);
	 
	 @Query("SELECT u FROM Product u WHERE u.category = 'other'")
	 public List<Product> findCategoryByother(@Param("category")String category);
	 
	 
}
