package main;

import model.Product;
import model.Sale;
import model.Amount;
import model.Client;
import model.Employee;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

//import dao.DaoImplFile;
//import dao.DaoImplXml;
import dao.DaoImplJaxb;


public class Shop {
    private Amount cash = new Amount(100.00);
    private ArrayList<Product> inventory;
    private int numberProducts;
    private ArrayList<Sale> sales;
    private int numberSales;
    //private DaoImplFile dao;
    //private DaoImplXml dao;
    private DaoImplJaxb dao;


    final static double TAX_RATE = 1.04;

    public Shop() {
        inventory = new ArrayList<Product>();
        sales = new ArrayList<Sale>();
        //dao = new DaoImplFile();
        //dao = new DaoImplXml();
        dao = new DaoImplJaxb();

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

        // load inventory from external data
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
            System.out.println("11) Guardar inventario en archivo");
            System.out.println("12) Exportar inventario");
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
                    
                case 11: // Nueva opción en el menú para guardar el inventario
                    if (shop.writeInventory()) { // Llamada al método a través de la instancia "shop"
                        System.out.println("Inventario guardado correctamente.");
                    } else {
                        System.out.println("Error al guardar el inventario.");
                    }
                    break;
                case 12:
                    shop.saveInventory();
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
        this.dao.getInventory();
        System.out.println(this.inventory);

    }
    

    public void saveInventory() {
    	String fileName = "files/inventory_" + LocalDate.now() + ".txt"; // Generar el nombre del archivo
        File file = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            int counter = 1;
            for (Product product : inventory) { // Recorrer los productos del inventario
                writer.write(counter + ";Product:" + product.getName() + ";Stock:" + product.getStock() + ";\n");
                counter++;
            }
            writer.write("Numero total de productos:" + inventory.size() + ";"); // Agregar el número total de productos
            System.out.println("Inventario exportado exitosamente a: " + fileName);
        } catch (IOException e) {
            System.err.println("Error al exportar el inventario: " + e.getMessage());
        }
    }

    private void readInventory() {
        ArrayList<Product> products = dao.getInventory();
        if (products != null && !products.isEmpty()) {
            this.inventory = products;
            System.out.println(this.inventory);
            System.out.println("Inventario cargado correctamente.");
        } else {
            System.out.println("Error al cargar el inventario.");
        }
    }

    
    public boolean writeInventory() {
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

        addProduct(new Product(name, new Amount(wholesalerPrice), true, stock));
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
            if (inventory.remove(product)) {
                System.out.println("El producto " + name + " ha sido eliminado");
            } else {
                System.out.println("No se ha encontrado el producto con nombre " + name);
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

        ArrayList<Product> shoppingCart = new ArrayList<Product>();
        int numberShopping = 0;

        Amount totalAmount = new Amount(0.0);
        String name = "";
        while (!name.equals("0")) {
            System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
            name = sc.nextLine();

            if (name.equals("0")) {
                break;
            }
            Product product = findProduct(name);
            boolean productAvailable = false;

            if (product != null && product.isAvailable()) {
                productAvailable = true;
                totalAmount.setValue(totalAmount.getValue() + product.getPublicPrice().getValue());
                product.setStock(product.getStock() - 1);
                shoppingCart.add(product);
                numberShopping++;
                if (product.getStock() == 0) {
                    product.setAvailable(false);
                }
                System.out.println("Producto añadido con éxito");
            }

            if (!productAvailable) {
                System.out.println("Producto no encontrado o sin stock");
            }
        }

        totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
        System.out.println("Venta realizada con éxito, total: " + totalAmount);

        if (!client.pay(totalAmount)) {
            System.out.println("Cliente debe: " + client.getBalance());
        }

        Sale sale = new Sale(client, shoppingCart, totalAmount);
        sales.add(sale);
        cash.setValue(cash.getValue() + totalAmount.getValue());
        numberSales++;
    }

    public void showSales() {
        System.out.println("Lista de ventas:");
        for (Sale sale : sales) {
            if (sale != null) {
                System.out.println(sale);
            }
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Exportar fichero ventas? S / N");
        String option = sc.nextLine();
        if ("S".equalsIgnoreCase(option)) {
            this.writeSales();
        }
    }

    private void showSalesAmount() {
        double totalSales = 0.0;

        for (Sale sale : sales) {
            totalSales += sale.getAmount().getValue();
        }
        System.out.println("Venta total: " + totalSales);
    }

    private boolean isInventoryFull() {
        return inventory.size() >= 10;
    }

    public Product findProduct(String name) {
        for (Product product : this.inventory) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }


    private void writeSales() {
        File file = new File("sales.txt");

        try (FileWriter writer = new FileWriter(file);
                BufferedWriter buffer = new BufferedWriter(writer)) {

            for (Sale sale : sales) {
                buffer.write(sale.toString());
                buffer.newLine();
            }
            System.out.println("Fichero sales.txt generado");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(Product product) {
        if (inventory.size() < 10) {
            inventory.add(product);
            System.out.println("Producto añadido");
        } else {
            System.out.println("Inventario completo, no se pueden añadir más productos");
        }
    }

    
    
}