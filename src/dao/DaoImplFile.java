package dao;

import main.Shop;
import model.Employee;
import model.Product;
import model.Amount;

import java.io.*;
import java.util.ArrayList;

public class DaoImplFile implements Dao {

    private Shop shop;

    public DaoImplFile(Shop shop) {
        this.shop = shop;
    }

    /**
     * Lee el inventario desde un archivo y lo carga en el objeto Shop.
     *
     * @return true si la operación fue exitosa, false en caso de error.
     */
    
    @Override
    public Employee getEmployee(int employeeId, String password) {
        throw new UnsupportedOperationException("getEmployee no está soportado en DaoImplFile");
    }

    @Override
    public ArrayList<Product> getInventory() {
        File file = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + "inputInventory.txt");
        ArrayList<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] sections = line.split(";");
                String name = sections[0].split(":")[1];
                double price = Double.parseDouble(sections[1].split(":")[1]);
                int stock = Integer.parseInt(sections[2].split(":")[1]);

                products.add(new Product(name, new Amount(price), true, stock));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return products;
    }



    /**
     * Escribe el inventario actual de Shop en un archivo.
     *
     * @return true si la operación fue exitosa, false en caso de error.
     */
    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        File file = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + "outputInventory.txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (Product product : inventory) { // Usamos el parámetro inventory recibido
                String line = String.format("Name:%s;Price:%.2f;Stock:%d",
                        product.getName(),
                        product.getWholesalerPrice().getValue(),
                        product.getStock());
                pw.println(line);
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    @Override
    public void disconnect() {
        
    }
    
    @Override
    public void connect() {
     
    }


}
