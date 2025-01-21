package dao.xml;

import model.Product;
import model.Amount;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class DomWriter {

    public void writeInventoryFile(List<Product> productList) {
        try {
            // Crear una nueva instancia de DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Crear un nuevo documento
            Document document = builder.newDocument();

            // Crear el elemento raíz <products>
            Element rootElement = document.createElement("products");
            document.appendChild(rootElement);

            // Agregar cada producto al documento
            for (Product product : productList) {
                // Crear el elemento <product>
                Element productElement = document.createElement("product");
                productElement.setAttribute("name", product.getName());
                rootElement.appendChild(productElement);

                // Crear el elemento <wholesalerPrice>
                Element priceElement = document.createElement("wholesalerPrice");
                priceElement.setAttribute("currency", "Euro");
                priceElement.setTextContent(String.valueOf(product.getWholesalerPrice().getValue()));
                productElement.appendChild(priceElement);

                // Crear el elemento <stock>
                Element stockElement = document.createElement("stock");
                stockElement.setTextContent(String.valueOf(product.getStock()));
                productElement.appendChild(stockElement);
            }

            // Crear el archivo XML con fecha actual en el nombre
            LocalDate today = LocalDate.now();
            String fileName = "files/inventory_" + today + ".xml";
            File outputFile = new File(fileName);

            // Configurar el transformador para escribir el contenido
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            // Escribir el contenido en el archivo
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(outputFile);
            transformer.transform(source, result);

            System.out.println("Archivo generado: " + outputFile.getAbsolutePath());

        } catch (ParserConfigurationException | javax.xml.transform.TransformerException e) {
            e.printStackTrace();
        }
    }
}
