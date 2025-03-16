package main;

import model.Product;
import model.Sale;
import model.Amount;
import model.Client;
import model.Employee;
import dao.DaoImplHibernate;
import dao.DaoImplJDBC; 
import dao.DaoImplMongoDB;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Shop {
    private Amount cash = new Amount(100.00);
    private ArrayList<Product> inventory;
    private int numberProducts;
    private ArrayList<Sale> sales;
    private int numberSales;
    
    private DaoImplMongoDB dao = new DaoImplMongoDB();

    final static double TAX_RATE = 1.04;

    public Shop() {
        this.dao = new DaoImplMongoDB(); 
        inventory = new ArrayList<Product>();
        sales = new ArrayList<Sale>();
        loadInventory();  
    }

    public Amount getCash() {
        return cash;
    }

    public void setCash(Amount cash) {
        this.cash = cash;
    }

    public ArrayList<Product> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Product> inventory) {
        this.inventory = inventory;
    }

    public int getNumberProducts() {
        return numberProducts;
    }

    public void setNumberProducts(int numberProducts) {
        this.numberProducts = numberProducts;
    }

    public ArrayList<Sale> getSales() {
        return sales;
    }

    public void setSales(ArrayList<Sale> sales) {
        this.sales = sales;
    }

    public int getNumberSales() {
        return numberSales;
    }

    public void setNumberSales(int numberSales) {
        this.numberSales = numberSales;
    }

    public static void main(String[] args) {
        Shop shop = new Shop();

        // load inventory from the database (not from file anymore)
        shop.loadInventory();

        // init session as employee
        shop.initSession();

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;
        boolean exit = false;

        do {
            System.out.println("\n");
            System.out.println("===========================");
            System.out.println("Menu principal miTienda.com");
            System.out.println("===========================");
            System.out.println("1) Contar caja");
            System.out.println("2) Añadir producto");
            System.out.println("3) Añadir stock");
            System.out.println("4) Marcar producto proxima caducidad");
            System.out.println("5) Ver inventario");
            System.out.println("6) Venta");
            System.out.println("7) Ver ventas");
            System.out.println("8) Ver venta total");
            System.out.println("9) Eliminar producto");
            System.out.println("10) Salir programa");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    shop.showCash();
                    break;

                case 2:
                    shop.addProduct();
                    break;

                case 3:
                    shop.addStock();
                    break;

                case 4:
                    shop.setExpired();
                    break;

                case 5:
                    shop.showInventory();
                    break;

                case 6:
                    shop.sale();
                    break;

                case 7:
                    shop.showSales();
                    break;

                case 8:
                    shop.showSalesAmount();
                    break;

                case 9:
                    shop.removeProduct();
                    break;

                case 10:
                    System.out.println("Cerrando programa ...");
                    exit = true;
                    break;
            }

        } while (!exit);
    }

    private void initSession() {
        Employee employee = new Employee("test");
        boolean logged = false;

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Introduzca numero de empleado: ");
            int employeeId = scanner.nextInt();

            System.out.println("Introduzca contraseña: ");
            String password = scanner.next();

            logged = employee.login(employeeId, password);
            if (logged) {
                System.out.println("Login correcto ");
            } else {
                System.out.println("Usuario o password incorrectos ");
            }
        } while (!logged);
    }

    public void loadInventory() {
        // Cargar inventario desde la base de datos
        this.inventory = dao.getInventory();
        if (inventory != null && !inventory.isEmpty()) {
            this.numberProducts = inventory.size();
            System.out.println("Inventario cargado con éxito.");
        } else {
            System.out.println("Error al cargar el inventario o inventario vacío.");
        }
    }

    public boolean writeInventory() {
        // Cambiar el método a trabajar con base de datos
        return dao.writeInventory(this.inventory);
    }

    private void showCash() {
        System.out.println("Dinero actual: " + cash);
    }

    public void addProduct() {
        if (isInventoryFull()) {
            System.out.println("No se pueden añadir más productos");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre: ");
        String name = scanner.nextLine();
        System.out.print("Precio mayorista: ");
        double wholesalerPrice = scanner.nextDouble();
        System.out.print("Stock: ");
        int stock = scanner.nextInt();

        Product newProduct = new Product(name, new Amount(wholesalerPrice), true, stock);
        dao.addProduct(newProduct); 
        inventory.add(newProduct);
        numberProducts++;
    }

    public void removeProduct() {
        if (inventory.size() == 0) {
            System.out.println("No se pueden eliminar productos, inventario vacio");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();
        Product product = findProduct(name);

        if (product != null) {
            // Eliminar el producto usando DaoImplJDBC
            if (dao.deleteProduct(product.getId())) {
                inventory.remove(product);
                numberProducts--;
                System.out.println("El producto " + name + " ha sido eliminado");
            } else {
                System.out.println("No se ha podido eliminar el producto.");
            }
        } else {
            System.out.println("No se ha encontrado el producto con nombre " + name);
        }
    }

    public void addStock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();
        Product product = findProduct(name);

        if (product != null) {
            System.out.print("Seleccione la cantidad a añadir: ");
            int stock = scanner.nextInt();
            product.setStock(product.getStock() + stock);
            dao.updateProduct(product);  // Actualizar el producto en la base de datos
            System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());
        } else {
            System.out.println("No se ha encontrado el producto con nombre " + name);
        }
    }

    private void setExpired() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();
        Product product = findProduct(name);

        if (product != null) {
            product.expire();
            dao.updateProduct(product);  // Actualizar el producto en la base de datos
            System.out.println("El precio del producto " + name + " ha sido actualizado a " + product.getPublicPrice());
        }
    }

    public void showInventory() {
        System.out.println("Contenido actual de la tienda:");
        for (Product product : inventory) {
            if (product != null) {
                System.out.println(product);
            }
        }
    }

    public void sale() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Realizar venta, escribir nombre cliente");
        String nameClient = sc.nextLine();
        Client client = new Client(nameClient);

        ArrayList<Product> shoppingCart = new ArrayList<>();
        Amount totalAmount = new Amount(0.0);
        String name = "";

        while (!name.equals("0")) {
            System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
            name = sc.nextLine();

            if (name.equals("0")) {
                break;
            }

            Product product = findProduct(name);
            if (product != null && product.isAvailable() && product.getStock() > 0) {
                totalAmount.setValue(totalAmount.getValue() + product.getPublicPrice().getValue());
                product.setStock(product.getStock() - 1);
                dao.updateProduct(product);
                shoppingCart.add(product);

                if (product.getStock() == 0) {
                    product.setAvailable(false);
                    dao.updateProduct(product);
                }
                System.out.println("Producto añadido con éxito.");
            } else {
                System.out.println("Producto no encontrado o sin stock.");
            }
        }

        totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
        System.out.println("Venta realizada con éxito, total: " + totalAmount);

        if (!client.pay(totalAmount)) {
            System.out.println("Cliente debe: " + client.getBalance());
        }

        Sale sale = new Sale(client, shoppingCart, totalAmount);
        dao.addSale(sale);
        sales.add(sale);
        numberSales++;
    }


    private void showSales() {
        for (Sale sale : sales) {
            System.out.println(sale);
        }
    }

    private void showSalesAmount() {
        double totalAmount = 0;
        for (Sale sale : sales) {
            totalAmount += sale.getAmount().getValue();
        }
        System.out.println("Total de ventas: " + totalAmount);
    }

    public Product findProduct(String name) {
        for (Product product : inventory) {
            if (product != null && product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }

    private boolean isInventoryFull() {
        return inventory.size() >= 100;
    }
    
    public void addProduct(Product product) {
        inventory.add(product);
    }

}