package model;

import javax.persistence.*; 
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "product")
@Entity 
@Table(name = "inventory")
public class Product {

    @Id
    private int id;

    @Column(nullable = false) 
    private String name;

    @Transient 
    private Amount publicPrice;

    @Embedded
    private Amount wholesalerPrice;

    @Column(nullable = false) 
    private boolean available;

    @Column(nullable = false)
    private int stock;

    @Transient 
    private static int totalProducts;

    private String badge;
    private String color;

    @Transient 
    public static final double EXPIRATION_RATE = 0.60;

    // Constructores
    public Product() {
        this.id = ++totalProducts;
        this.available = true;
    }

    public Product(int id, String name, Amount wholesalerPrice, boolean available, int stock) {
        super();
        this.id = id;
        this.name = name;
        this.wholesalerPrice = wholesalerPrice;
        this.available = available;
        this.stock = stock;
        this.publicPrice = new Amount(wholesalerPrice.getValue() * 2);
        ++totalProducts;
    }

    public Product(String name, Amount wholesalerPrice, boolean available, int stock) {
        this();
        this.name = name;
        this.wholesalerPrice = wholesalerPrice;
        this.publicPrice = new Amount(wholesalerPrice.getValue() * 2);
        this.stock = stock;
    }

    // Getters y Setters con anotaciones JPA
    @XmlElement
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public Amount getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(Amount publicPrice) {
        this.publicPrice = publicPrice;
    }

    @XmlElement
    public Amount getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(Amount wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
    }

    @XmlElement
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @XmlElement
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public static int getTotalProducts() {
        return totalProducts;
    }

    public static void setTotalProducts(int totalProducts) {
        Product.totalProducts = totalProducts;
    }

    @XmlElement
    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    @XmlElement
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void expire() {
        if (this.publicPrice != null) {
            double newValue = this.publicPrice.getValue() * EXPIRATION_RATE;
            this.publicPrice.setValue(newValue);
        }
    }

    @Override
    public String toString() {
        return "Product [name=" + name + ", publicPrice=" + publicPrice + ", wholesalerPrice=" + wholesalerPrice
                + ", available=" + available + ", stock=" + stock + "]";
    }
}
