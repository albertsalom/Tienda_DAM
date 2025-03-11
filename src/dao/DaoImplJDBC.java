package dao;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;
import model.Sale;
 
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
            System.out.println("Error al conectarse a la base de datos: " + e.getMessage());
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
	    ArrayList<Product> inventory = new ArrayList<>();
	    if (this.connection == null) {
	        connect();
	    }
	    String query = "SELECT * FROM inventory";
	    try (Statement stmt = connection.createStatement(); 
	         ResultSet rs = stmt.executeQuery(query)) {
	        while (rs.next()) {
	            Product product = new Product(
	                rs.getInt("id"),
	                rs.getString("name"),
	                new Amount(rs.getDouble("wholesalerPrice")),
	                rs.getBoolean("available"),
	                rs.getInt("stock")
	            );
	            inventory.add(product);
	        }
	    } catch (SQLException e) {
	        System.out.println("Error al obtener el inventario: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return inventory;
	}
 
	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
	    String query = "INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) VALUES (?, ?, ?, ?, ?, ?)";
	    
	    try (PreparedStatement ps = connection.prepareStatement(query)) {
	        for (Product product : inventory) {
	            ps.setInt(1, product.getId());
	            ps.setString(2, product.getName());
	            double wholesalerPrice = product.getWholesalerPrice().getValue();
	            ps.setDouble(3, wholesalerPrice);	            
	            ps.setBoolean(4, product.isAvailable());
	            ps.setInt(5, product.getStock());
	            ps.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
	            ps.addBatch();
	        }
	        
	        ps.executeBatch();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	@Override
	public boolean addProduct(Product product) {
	    if (this.connection == null) {
	        connect(); // Verifica que la conexión esté activa
	    }

	    String sql = "INSERT INTO inventory (name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?)";

	    try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

	        // Establecer los parámetros del PreparedStatement
	        pstmt.setString(1, product.getName()); // Nombre del producto
	        pstmt.setDouble(2, product.getWholesalerPrice().getValue()); // Precio mayorista (double)
	        pstmt.setBoolean(3, product.isAvailable()); // Disponibilidad
	        pstmt.setInt(4, product.getStock()); // Stock disponible

	        // Ejecutar la inserción
	        int rowsAffected = pstmt.executeUpdate();

	        // Verifica si la inserción fue exitosa
	        return rowsAffected > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Retorna false si hubo un error
	    }
	}




	@Override
	public boolean updateProduct(Product product) {
	    // Verificar que la conexión esté abierta
	    if (this.connection == null) {
	        connect();  // Si la conexión es null, la inicializamos
	    }

	    // La consulta SQL para actualizar el producto en la base de datos
	    String sql = "UPDATE inventory SET name = ?, wholesalerPrice = ?, available = ?, stock = ? WHERE id = ?";

	    try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
	        // Establecer los parámetros del PreparedStatement
	        pstmt.setString(1, product.getName());  // Establecer el nombre del producto
	        pstmt.setDouble(2, product.getWholesalerPrice().getValue());  // Establecer el precio (mayorista o público, según tu lógica)
	        pstmt.setBoolean(3, product.isAvailable());  // Establecer la disponibilidad
	        pstmt.setInt(4, product.getStock());  // Establecer el stock
	        pstmt.setInt(5, product.getId());  // Establecer el ID del producto para actualizar

	        // Ejecutar la actualización
	        int rowsAffected = pstmt.executeUpdate();

	        // Si la actualización afecta una o más filas, la operación fue exitosa
	        return rowsAffected > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;  // En caso de error, retornar false
	    }
	}


	@Override
	public boolean deleteProduct(int productId) {
	    // Verificar que la conexión esté abierta
	    if (this.connection == null) {
	        connect();  // Si la conexión no está inicializada, la inicializamos
	    }

	    // La consulta SQL para eliminar el producto
	    String sql = "DELETE FROM inventory WHERE id = ?";

	    try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
	        // Establecer el parámetro del PreparedStatement (el ID del producto)
	        pstmt.setInt(1, productId);

	        // Ejecutar la eliminación
	        int rowsAffected = pstmt.executeUpdate();

	        // Si se eliminó una o más filas, la operación fue exitosa
	        return rowsAffected > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;  // En caso de error, retornar false
	    }
	}

	public void addSale(Sale sale) {
		// TODO Auto-generated method stub
		
	}

 
}