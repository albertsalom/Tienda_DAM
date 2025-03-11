package model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "product_history")
public class ProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "product_id")
    private int productId; 

    @Column(name = "name")
    private String name;

    @Column(name = "wholesaler_price")
    private double wholesalerPrice;

    @Column(name = "available")
    private boolean available;

    @Column(name = "stock")
    private int stock;

    @Column(name = "created_at")
    private Timestamp createdAt;

    // Constructor sin parámetros (requerido por Hibernate)
    public ProductHistory() {}

    // Constructor para writeInventory (este puede aceptar Amount como parámetro)
    public ProductHistory(int productId, String name, Amount wholesalerPrice, boolean available, int stock, Timestamp createdAt) {
        this.productId = productId; // Asignar manualmente el ID del producto
        this.name = name;
        this.wholesalerPrice = wholesalerPrice.getValue();
        this.available = available;
        this.stock = stock;
        this.createdAt = createdAt;
    }

    // Constructor para addProduct (este acepta el valor de wholesalerPrice directamente como un double)
    public ProductHistory(int productId, String name, double wholesalerPrice, boolean available, int stock, Timestamp createdAt) {
        this.productId = productId; // Asignar manualmente el ID del producto
        this.name = name;
        this.wholesalerPrice = wholesalerPrice;
        this.available = available;
        this.stock = stock;
        this.createdAt = createdAt;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(double wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}