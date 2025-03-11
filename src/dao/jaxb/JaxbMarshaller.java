package dao.jaxb;

import model.Product;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JaxbMarshaller {

    public static void main(String[] args) {
        try {
            // Leer los productos desde inputInventory.xml (Unmarshalling)
            File inputFile = new File("files/inputInventory.xml");
            JAXBContext context = JAXBContext.newInstance(ProductListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ProductListWrapper wrapper = (ProductListWrapper) unmarshaller.unmarshal(inputFile);

            // Obtener la lista de productos del wrapper
            List<Product> products = wrapper.getProducts();

            // Generar el nombre del archivo de salida basado en la fecha actual
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String outputFileName = "inventory_" + date + ".xml";
            File outputFile = new File(outputFileName);

            // Escribir los productos en el nuevo archivo XML (Marshalling)
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(wrapper, outputFile);

            System.out.println("El archivo XML ha sido generado: " + outputFileName);

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
