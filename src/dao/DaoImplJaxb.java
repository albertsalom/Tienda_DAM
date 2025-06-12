package dao;
import java.util.ArrayList;

import dao.jaxb.JaxbMarshaller;
import dao.jaxb.JaxbUnMarshaller;
import model.Employee;
import model.Product;

public class DaoImplJaxb implements Dao{
	JaxbUnMarshaller unMarshaller = new JaxbUnMarshaller();
	JaxbMarshaller marshaller = new JaxbMarshaller();
	
	@Override
	public void connect() {
		// no hace nada; el JAXB no necesita conexión
	}

	@Override
	public void disconnect() {
		// no hace nada
	}

	@Override
	public Employee getEmployee(int userNumber, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
		// TODO Auto-generated method stub
		ArrayList<Product> productos = unMarshaller.init().getProducts();
		return productos;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		// TODO Auto-generated method stub
		return marshaller.init(inventory);
	}

    @Override
    public void addProduct(Product product) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addProduct'");
    }

    @Override
    public void updateProduct(String name, int stock) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProduct'");
    }

    @Override
    public void deleteProduct(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProduct'");
    }

}