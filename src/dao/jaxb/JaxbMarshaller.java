package dao.jaxb;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import model.Product;
import model.ProductList;



public class JaxbMarshaller {
	
	public boolean init (ArrayList<Product>productsList) {
		try {
			JAXBContext context = JAXBContext.newInstance(ProductList.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			ProductList products = createXml(productsList);
			System.out.println("marshalling... ");
			LocalDate fecha = LocalDate.now();
			String rutaCarpeta = System.getProperty("user.dir") + File.separator + "jaxb";
			String rutaArchivo = rutaCarpeta + File.separator + "inventory_" + fecha.toString() + ".xml";
			marshaller.marshal(products, new File(rutaArchivo));
			if (products != null) {
				return true;
			} else {
				return false;
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			return false;
		}
	}

	private ProductList createXml(ArrayList<Product>productsList) {
	
		// print products
		for (Product p : productsList) {
			System.out.println(p);
		}
		return new ProductList(productsList);
	}
}
