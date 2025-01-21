package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Employee;
import model.Product;

public interface Dao {
	
	public void connect() throws SQLException;
	
	public void disconnect() throws SQLException;
	
	public Employee getEmployee(int employeeId, String password) throws SQLException;
	
	public ArrayList<Product> getInventory();
	
	public boolean writeInventory(ArrayList<Product> inventory);
}
