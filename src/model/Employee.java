package model;

import main.Logable;
import dao.*;

public class Employee extends Person implements Logable{
	private int employeeId;
	private String password;
	// connection using JDBC SQL
	private Dao dao = new DaoImplJDBC();
	
	public static int USER = 123;
	public static String PASSWORD = "test";
	
	public Employee(String name) {
		super(name);
	}
	
	public Employee(int employeeId, String name, String password) {
		super(name);
		this.USER = employeeId;
		this.PASSWORD = password;
	}
	
	public Employee() {
		super();
	}
	
	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public static int getUSER() {
		return USER;
	}

	public static void setUSER(int uSER) {
		USER = uSER;
	}

	public static String getPASSWORD() {
		return PASSWORD;
	}

	public static void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	

	/**
	 * @param user from application, password from application
	 * @return true if credentials are correct or false if not
	 */
	@Override
	public boolean login(int user, String password) {
		if (USER == user && PASSWORD.equals(password)) {
			return true;
		} 
		boolean success = false;
		
		// connect to data
		dao.connect();
		
		// get employee data
		if(dao.getEmployee(user, password) != null) {
			success =  true;
		}
		
		// disconnect data
		dao.disconnect();
		return success;
	}

}
