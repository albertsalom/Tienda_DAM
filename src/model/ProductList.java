package model;

import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="products")
public class ProductList {
	@XmlAttribute(name="total")
	private int total;
	
	private ArrayList<Product> products = new ArrayList<>();  // Initialize products	

	
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	
	public ProductList() {
		super();
	}

	public ProductList(ArrayList<Product> products) {
		this.products = products;
		this.total = products.size();

	}

	@XmlElement(name="product")
	public ArrayList<Product> getProducts() {
		return products;
	}
	
}