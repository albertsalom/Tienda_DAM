package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplJDBC implements Dao {
	Connection connection;

	@Override
	public void connect() {
		// Define connection parameters
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		String query = "select * from employee where employeeId= ? and password = ? ";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) { 
			ps.setInt(1,employeeId);
			ps.setString(2,password);
    	  	//System.out.println(ps.toString());
            try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
				}
            }
        } catch (SQLException e) {
			// in case error in SQL
			e.printStackTrace();
		}
		return employee;
	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> products = new ArrayList<>();
		String query = "select * from inventory";
		if (this.connection == null) {
            throw new IllegalStateException("Database connection is not established. Call connect() first.");
        }
		try (PreparedStatement ps = connection.prepareStatement(query)) { 
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String  name      = rs.getString("name");
					double  dblPrice  = rs.getDouble("wholesalerPrice");
					Amount  price     = new Amount(dblPrice);
					boolean available = rs.getBoolean("available");
					int     stock     = rs.getInt("stock");

					products.add(new Product(name, price, available, stock));
				}
			}
		}  catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error al cargar inventario", e);
		}
		
		return products; 
	}
	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		String query = "insert into historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) values"
				+ "(?, ?, ?, ?, ?, ?);";
		if (this.connection == null) {
            throw new IllegalStateException("Database connection is not established. Call connect() first.");
        }
		try (PreparedStatement ps = connection.prepareStatement(query)) { 	
			for (Product product : inventory) {
				ps.setInt(1, product.getId());
				ps.setString(2, product.getName());
				ps.setDouble(3, product.getWholesalerPrice().getValue());
				ps.setBoolean(4, product.isAvailable());
                ps.setInt(5, product.getStock());
                ps.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
                int results = ps.executeUpdate(); 
			}

            return true;
		}  catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void addProduct(Product product) {
		String query = "insert into inventory (id, name, wholesalerPrice, available, stock) values"
				+ "(?, ?, ?, ?, ?);";
		if (this.connection == null) {
            throw new IllegalStateException("Database connection is not established. Call connect() first.");
        }
		try (PreparedStatement ps = connection.prepareStatement(query)) { 	
			ps.setInt(1, product.getId());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getWholesalerPrice().getValue());
            ps.setBoolean(4, product.isAvailable());
            ps.setInt(5, product.getStock());
            int results = ps.executeUpdate(); 
		}  catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void updateProduct(String name, int stock){
		String query = "update inventory set stock = stock + ? where name = ?";
		if (this.connection == null) {
            throw new IllegalStateException("Database connection is not established. Call connect() first.");
        }
		try (PreparedStatement ps = connection.prepareStatement(query)) { 	
			ps.setInt(1, stock);
			ps.setString(2, name);
			int results = ps.executeUpdate(); 
		}  catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Override
	public void deleteProduct(String name){
		String query = "delete from inventory where name = ?";
		if (this.connection == null) {
            throw new IllegalStateException("Database connection is not established. Call connect() first.");
        }
		try (PreparedStatement ps = connection.prepareStatement(query)) { 	
			ps.setString(1, name);
			int results = ps.executeUpdate(); 
		}  catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}