package dao;

import dao.xml.DomWriter;
import dao.xml.SaxReader;
import model.Product;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class DaoImplXml implements Dao {

    private static final String INVENTORY_XML_FILE = "files/inputInventory.xml"; // Ruta del archivo de inventario XML

    @Override
    public ArrayList<Product> getInventory() {
        ArrayList<Product> products = null;

        // Usar SAX para leer el documento XML de inventario
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            File inputFile = new File(INVENTORY_XML_FILE);

            // Verificar si el archivo existe
            if (!inputFile.exists()) {
                System.out.println("ERROR: Archivo 'inputInventory.xml' no encontrado en la ruta especificada.");
                return null;
            }

            // Usar SaxReader para parsear el XML
            SaxReader saxReader = new SaxReader();
            parser.parse(inputFile, saxReader);
            products = saxReader.getProductList();
            
        } catch (ParserConfigurationException | SAXException e) {
            System.out.println("ERROR creando el parser");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR al leer el archivo 'inputInventory.xml'.");
            e.printStackTrace();
        }

        return products; // Devolver la lista de productos leída
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {


        // Usar DomWriter para escribir el inventario en un nuevo archivo XML
        DomWriter domWriter = new DomWriter();
        return domWriter.generateDocument(inventory); // Devuelve true si se genera el documento correctamente
    }

    // Métodos de la interfaz Dao sin implementación
    @Override
    public void connect() {
        
    }

    @Override
    public void disconnect() {
        
    }

    @Override
    public model.Employee getEmployee(int employeeId, String password) {
        return null; 
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
