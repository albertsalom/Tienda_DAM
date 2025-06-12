package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import dao.xml.DomWriter;
import dao.xml.SaxReader;
import model.Employee;
import model.Product;

public class DaoImplXml implements Dao{

	@Override
	public Employee getEmployee(int userNumber, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> products = new ArrayList<>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();	
			File file = new File ("xml/inputInventory.xml");
			SaxReader saxReader = new SaxReader();
			parser.parse(file, saxReader);
			products = saxReader.getInventory();
			
		} catch (ParserConfigurationException | SAXException e) {
			System.out.println("ERROR creating the parser");
		} catch (IOException e) {
			System.out.println("ERROR file not found");
		}
		return products;
	}		

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
		DomWriter domWriter = new DomWriter();
		domWriter.generateDocument(products);
		return domWriter.generateDocument(products);
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

	@Override
	public void connect() {
		// no hace nada; el XML no necesita conexión
	}

	@Override
	public void disconnect() {
		// no hace nada
	}

    
}