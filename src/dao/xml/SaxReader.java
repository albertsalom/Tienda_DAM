package dao.xml;

import model.Product;
import model.Amount;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class SaxReader extends DefaultHandler {

    private ArrayList<Product> productList;
    private Product currentProduct;
    private StringBuilder currentValue;
    private String parsedElement;

    public SaxReader() {
        productList = new ArrayList<>();
        currentValue = new StringBuilder();
    }

    // Se llama al inicio de cada etiqueta
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "product":
                // Creamos un nuevo producto; si 'name' no está como atributo, usamos el nombre por defecto "empty"
                this.currentProduct = new Product();
                String nameAttribute = attributes.getValue("name");
                if (nameAttribute != null) {
                    this.currentProduct.setName(nameAttribute);
                }
                break;
            case "wholesalerPrice":
                // Aquí usamos un atributo 'badge' si existe
                String badgeAttribute = attributes.getValue("badge");
                if (badgeAttribute != null) {
                    this.currentProduct.setBadge(badgeAttribute);
                }
                break;
            case "stock":
                // Establecemos 'color' y 'stock' si están como atributos 
                String colorAttribute = attributes.getValue("color");
                if (colorAttribute != null) {
                    this.currentProduct.setColor(colorAttribute);
                }
                String stockAttribute = attributes.getValue("storage"); // Cambiar a stock si es necesario
                if (stockAttribute != null) {
                    this.currentProduct.setStock(Integer.parseInt(stockAttribute)); // Usar setStock en lugar de setStorage
                }
                break;
        }
        // Asignamos el elemento actual y reiniciamos currentValue para acumular el contenido
        this.parsedElement = qName;
        currentValue.setLength(0); // Reiniciamos el acumulador de valores de texto
    }

    // Se llama al final de cada etiqueta
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("name")) {
            currentProduct.setName(currentValue.toString().trim());
        } else if (qName.equalsIgnoreCase("wholesalerPrice")) {
            double price = Double.parseDouble(currentValue.toString().trim());
            currentProduct.setWholesalerPrice(new Amount(price));
            currentProduct.setPublicPrice(new Amount(price * 2));  // Precio público es el doble del precio mayorista
        } else if (qName.equalsIgnoreCase("stock")) {
            int stock = Integer.parseInt(currentValue.toString().trim());
            currentProduct.setStock(stock); // Usar setStock directamente
        } else if (qName.equalsIgnoreCase("product")) {
            productList.add(currentProduct);  // Al terminar el producto, lo añadimos a la lista
        }
    }

    // Para capturar los valores dentro de las etiquetas
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentValue.append(new String(ch, start, length));
    }

    // Método que devuelve la lista de productos una vez terminado el parseo
    public ArrayList<Product> getProductList() {
        return productList;
    }
}

