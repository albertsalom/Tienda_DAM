package dao.jaxb;

import model.Product;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.List;

public class JaxbUnMarshaller {

    public static void main(String[] args) {
        try {
            // Ruta al archivo XML
            File file = new File("files/inputInventory.xml");

            // Crear el contexto JAXB para la clase Wrapper
            JAXBContext context = JAXBContext.newInstance(ProductListWrapper.class);

            // Crear el unmarshaller
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Realizar el unmarshalling
            ProductListWrapper wrapper = (ProductListWrapper) unmarshaller.unmarshal(file);

            // Convertir wholesalerPrice a Amount después del unmarshalling
            List<Product> products = wrapper.getProducts();
            for (Product product : products) {
                product.setWholesalerPrice(product.getWholesalerPrice()); 
                System.out.println(product); 
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // Clase interna para envolver la lista de productos
    @XmlRootElement(name = "inventory")
    public static class ProductListWrapper {
        private List<Product> products;

        @XmlElement(name = "product")
        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }
}