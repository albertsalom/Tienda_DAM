package dao;

import model.Employee;
import model.Product;
import java.util.ArrayList;

public interface Dao {
	
	public void connect();
	public void disconnect();

	public Employee getEmployee(int employeeId, String password);

	public ArrayList<Product> getInventory();
	public boolean writeInventory(ArrayList<Product> products);
	
	public void addProduct(Product product);
	public void updateProduct(String name, int stock);
	public void deleteProduct(String name);
}
