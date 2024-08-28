package com.pro.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Product_list")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int pid;
	private String img;
	private String pname;
	private String description;
	private String price;
	private String category;
	
	
	@ManyToOne
	private User user;
	
		public Product() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		public Product(int pid, String image, String pname, String description, String price, String category) {
			super();
			this.pid = pid;
			this.img = image;
			this.pname = pname;
			this.description = description;
			this.price = price;
			this.category = category;
		}
		

		public int getPid() {
			return pid;
		}

		public void setPid(int pid) {
			this.pid = pid;
		}

		public String getImage() {
			return img;
		}

		public void setImage(String image) {
			this.img = image;
		}

		public String getPname() {
			return pname;
		}

		public void setPname(String pname) {
			this.pname = pname;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}
		

		@Override
		public String toString() {
			return "Product [pid=" + pid + ", img=" + img + ", pname=" + pname + ", description=" + description
					+ ", price=" + price + ", category=" + category + "]";
		}

		public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
