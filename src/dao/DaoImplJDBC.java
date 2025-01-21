package dao;

import java.sql.*;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplJDBC implements Dao {
    Connection connection;

    @Override
    public void connect() {
        String url = "jdbc:mysql://localhost:3306/shop";
        String user = "root";
        String pass = "";
        try {
            this.connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        Employee employee = null;
        String query = "SELECT * FROM employee WHERE employeeId = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, employeeId);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }
    
    @Override
    public ArrayList<Product> getInventory() {
        ArrayList<Product> inventory = new ArrayList<>();
        String query = "SELECT id, name, public_price, wholesaler_price, stock FROM product";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double publicPriceValue = rs.getDouble("public_price");
                double wholesalerPriceValue = rs.getDouble("wholesaler_price");
                int stock = rs.getInt("stock");

                // Crear Amount para precios
                Amount publicPrice = new Amount(publicPriceValue);
                Amount wholesalerPrice = new Amount(wholesalerPriceValue);

                // Crear el objeto Product
                Product product = new Product(name, wholesalerPrice, true, stock);
                product.setId(id); // Si es necesario asignar manualmente el ID
                product.setPublicPrice(publicPrice);

                inventory.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventory;
    }


    @Override
    public boolean writeInventory(ArrayList<Product> products) {
        String insertQuery = "INSERT INTO product (id, name, public_price, wholesaler_price, stock) VALUES (?, ?, ?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE name = VALUES(name), public_price = VALUES(public_price), wholesaler_price = VALUES(wholesaler_price), stock = VALUES(stock)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            for (Product product : products) {
                ps.setInt(1, product.getId());
                ps.setString(2, product.getName());
                ps.setDouble(3, product.getPublicPrice().getValue()); // Extraer valor de publicPrice
                ps.setDouble(4, product.getWholesalerPrice().getValue()); // Extraer valor de wholesalerPrice
                ps.setInt(5, product.getStock());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
