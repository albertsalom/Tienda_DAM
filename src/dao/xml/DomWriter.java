package dao.xml;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.Product;
public class DomWriter {
    private Document document;	
	public DomWriter() {
	}	


    public boolean generateDocument(ArrayList<Product> inventory) {
        try {
            // Crear un nuevo documento vacio
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            
            // Numero total products
            Element root = document.createElement("products");
            root.setAttribute("total",Integer.toString(inventory.size()));
            document.appendChild(root);

            //Crear productos
            for (Product product : inventory) {
                Element productElement = document.createElement("product");
                productElement.setAttribute("name", product.getName());
                root.appendChild(productElement);

                Element priceElement = document.createElement("wholesalerPrice");
                priceElement.setAttribute("currency", "€");
                priceElement.setTextContent(String.format("%.2f", product.getWholesalerPrice().getValue()));
                productElement.appendChild(priceElement);
                
                Element stockElement = document.createElement("stock");
                stockElement.setTextContent(Integer.toString(product.getStock()));
                productElement.appendChild(stockElement);
            }


            return generateXml();
        } catch (ParserConfigurationException e) {
            System.err.println("Error al crear el parser DOM: " + e.getMessage());
            return false;
        }
    }

    private boolean generateXml() {
    try {
        //Preparamos transformer
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        // Fichero xml final
        LocalDate fecha = LocalDate.now();
        File dir = new File("xml");
        if (!dir.exists()) dir.mkdir();
        File outputFile = new File(dir, "inventory_" + fecha + ".xml");

        // PrintWriter para transformar DOM
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputFile))) {
            transformer.transform(new DOMSource(document), new StreamResult(pw));
        }

        System.out.println("XML generado en: " + outputFile.getAbsolutePath());
        return true;

    } catch (TransformerConfigurationException e) {
        System.err.println("Error configurando el Transformer: " + e.getMessage());
    } catch (TransformerException e) {
        System.err.println("Error durante la transformación XML: " + e.getMessage());
    } catch (IOException e) {
        System.err.println("Error al abrir/escribir el fichero XML: " + e.getMessage());
    }
        return false;
    }
}
