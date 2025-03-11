package dao;

import dao.jaxb.JaxbMarshaller;
import dao.jaxb.JaxbUnMarshaller;
import model.Employee;
import model.Product;
import dao.jaxb.JaxbUnMarshaller.ProductListWrapper;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DaoImplJaxb implements Dao {

    private static final String INPUT_FILE = "files/inputInventory.xml";
    private static final String OUTPUT_FILE_PREFIX = "inventory_";

    @Override
    public void connect() {
        System.out.println("Conexión establecida.");
    }

    @Override
    public void disconnect() {
        System.out.println("Conexión cerrada.");
    }

    @Override
    public ArrayList<Product> getInventory() {
        try {
            // Usar JaxbUnMarshaller para deserializar el archivo XML a objetos
            File inputFile = new File(INPUT_FILE);
            JAXBContext context = JAXBContext.newInstance(ProductListWrapper.class, Product.class);
            JaxbUnMarshaller.ProductListWrapper wrapper = (ProductListWrapper) context.createUnmarshaller().unmarshal(inputFile);

            // Convertir la lista obtenida a ArrayList y devolverla
            return new ArrayList<>(wrapper.getProducts());
        } catch (JAXBException e) {
            e.printStackTrace();
            return null; // Devolver null si ocurre un error
        }
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        try {
            // Crear el wrapper con la lista de productos
            JaxbMarshaller.ProductListWrapper wrapper = new JaxbMarshaller.ProductListWrapper();
            wrapper.setProducts(inventory);

            // Generar el nombre del archivo de salida basado en la fecha actual
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String outputFileName = OUTPUT_FILE_PREFIX + date + ".xml";

            // Usar JaxbMarshaller para serializar la lista de productos a un archivo XML
            File outputFile = new File(outputFileName);
            JAXBContext context = JAXBContext.newInstance(JaxbMarshaller.ProductListWrapper.class);
            context.createMarshaller().marshal(wrapper, outputFile);

            System.out.println("Archivo generado exitosamente: " + outputFileName);
            return true; // Operación exitosa
        } catch (JAXBException e) {
            e.printStackTrace();
            return false; // Error durante la operación
        }
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        // Crear un empleado ficticio con las credenciales estáticas
        Employee staticEmployee = new Employee(Employee.USER, "Static Employee", Employee.PASSWORD);

        // Verificar las credenciales
        if (staticEmployee.login(employeeId, password)) {
            return staticEmployee; // Devolver el empleado si las credenciales son correctas
        } else {
            System.out.println("Credenciales incorrectas.");
            return null; // Devolver null si las credenciales son incorrectas
        }
    }

	@Override
	public boolean addProduct(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateProduct(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteProduct(int productId) {
		// TODO Auto-generated method stub
		return false;
	}
}
