package dao.jaxb;

import model.Product;
import model.ProductList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class JaxbUnMarshaller {
	
	// Método para deserializar (unmarshal) un archivo XML y convertirlo en un objeto Java de tipo ProductList
    public ProductList unmarshal(String xmlFilePath) {
        try {
        	// Crear el contexto JAXB para la clase ProductList
            JAXBContext context = JAXBContext.newInstance(ProductList.class);

            // Crear un objeto Unmarshaller para convertir XML a objetos Java
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Crear un objeto File a partir de la ruta del archivo XML proporcionada
            File file = new File(xmlFilePath);
            // Convertir el archivo XML en un objeto Java de tipo ProductList
            ProductList productList = (ProductList) unmarshaller.unmarshal(file);  // Devuelve un objeto ProductList

            return productList;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}
