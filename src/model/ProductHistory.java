package model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="historical_inventory")
public class ProductHistory {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	@Column
	private int id_product;
	@Column
    private String name;
	@Column(name = "WholesalerPrice")
    private double price;
    @Column
    private boolean available;
    @Column
    private int stock;
    @Column
    private LocalDateTime created_at;
    
    public ProductHistory() {
        // Constructor vacío requerido por Hibernate
    }
    
	public ProductHistory(int id_product, String name, double price, boolean available, int stock) {
		super();
		this.id_product = id_product;
		this.name = name;
		this.price = price;
		this.available = available;
		this.stock = stock;
		this.created_at = LocalDateTime.now();
	}
	public int getIdProduct() {
		return id_product;
	}
	public void setIdProduct(int idProduct) {
		this.id_product = idProduct;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public LocalDateTime getFecha() {
		return created_at;
	}
	public void setFecha(LocalDateTime fecha) {
		this.created_at = fecha;
	}

    
}