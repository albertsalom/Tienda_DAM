package dao.jaxb;

import java.io.File;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import model.ProductList;

public class JaxbUnMarshaller {
	public ProductList init() {
		// read from xml to java object
		ProductList products = null;
		try {
			JAXBContext context = JAXBContext.newInstance(ProductList.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			System.out.println("unmarshalling...");
			products = (ProductList) unmarshaller.unmarshal(new File("jaxb/inputInventory.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		// print products
		if (products == null) {
			System.out.println("Error unmarshalling");
			return products;
		}else {
			return products;
		}
	}
}