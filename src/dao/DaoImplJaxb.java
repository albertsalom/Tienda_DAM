package dao;

import model.Employee;
import model.Product;
import model.ProductList;

import java.io.File;
import java.util.ArrayList;
import java.util.List; 
import javax.xml.bind.JAXBException;
import dao.jaxb.JaxbUnMarshaller;
import dao.jaxb.JaxbMarshaller;

public class DaoImplJaxb implements Dao {

    private final String inventoryFilePath = "files/inputInventory.xml"; // Ruta del archivo XML de inventario

    @Override
    public void connect() {
    }

    @Override
    public void disconnect() {
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        return null;
    }

    @Override
    public ArrayList<Product> getInventory() {
        JaxbUnMarshaller unMarshaller = new JaxbUnMarshaller();
        
        ProductList productList = unMarshaller.unmarshal(inventoryFilePath);
        
        if (productList == null) {
            return new ArrayList<>();
        }
        
        return new ArrayList<>(productList.getProducts());
    }


    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        JaxbMarshaller marshaller = new JaxbMarshaller();

        String fileName = "files/inventory_" + java.time.LocalDate.now() + ".xml";

        return marshaller.marshal(inventory, fileName);
    }

}
