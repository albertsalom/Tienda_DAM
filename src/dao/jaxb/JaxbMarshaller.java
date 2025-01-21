package dao.jaxb;

import model.Product;
import model.ProductList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;

public class JaxbMarshaller {

	// Método para serializar (marshal) un objeto Java de tipo ArrayList<Product> a un archivo XML
    public boolean marshal(ArrayList<Product> inventory, String filePath) {
        try {
        	// Crear un objeto ProductList que envuelva la lista de productos
            ProductList productList = new ProductList();
            productList.setProducts(inventory);// Asignar la lista de productos al objeto ProductList

            // Crear el contexto JAXB para la clase ProductList
            JAXBContext context = JAXBContext.newInstance(ProductList.class);

            // Crear un objeto Marshaller para convertir objetos Java a XML
            Marshaller marshaller = context.createMarshaller();

            // Configurar el Marshaller para que formatee la salida XML con saltos de línea y sangría
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Convertir el objeto ProductList a XML y guardarlo en el archivo especificado
            marshaller.marshal(productList, new File(filePath));
            
            return true;
        } catch (JAXBException e) {
            e.printStackTrace();
            return false;
        }
    }
}
