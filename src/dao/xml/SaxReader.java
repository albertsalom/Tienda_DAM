package dao.xml;

import model.Product;
import model.Amount;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SaxReader extends DefaultHandler {

    private ArrayList<Product> productList;
    private String currentName;
    private Amount currentWholesalerPrice;
    private boolean currentAvailable;
    private int currentStock;
    private StringBuilder currentValue;

    public SaxReader() {
        productList = new ArrayList<>();
        currentValue = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentValue.setLength(0); // Reiniciar acumulador de texto

        if (qName.equals("product")) {
            // Inicializar variables para un nuevo producto
            currentName = attributes.getValue("name");
            currentAvailable = Boolean.parseBoolean(attributes.getValue("available"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "wholesalerPrice":
                double price = Double.parseDouble(currentValue.toString().trim());
                currentWholesalerPrice = new Amount(price);
                break;

            case "stock":
                currentStock = Integer.parseInt(currentValue.toString().trim());
                break;

            case "product":
                // Crear el producto una vez que tengamos toda la información
                Product product = new Product(
                    currentName,
                    currentWholesalerPrice,
                    currentAvailable,
                    currentStock
                );
                productList.add(product);
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentValue.append(new String(ch, start, length));
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    // Método parse para procesar el archivo XML
    public void parse(File inputFile) throws Exception {
        try {
            // Crear una instancia de SAXParserFactory
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // Establecer el manejador de eventos
            saxParser.parse(inputFile, this); // Usamos 'this' ya que estamos extendiendo DefaultHandler
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            throw new Exception("Error al parsear el archivo XML", e);
        }
    }
}

