package dao.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.Amount;
import model.Product;

public class SaxReader extends DefaultHandler {
    ArrayList<Product> inventory;
    Product product;
    String value;
    String parsedElement;

    // devolver los Productos
    public ArrayList<Product> getInventory() {
        return inventory;
    }

    // Añadir los productos
    public void setInventory(ArrayList<Product> inventory) {
        this.inventory = inventory;
    }

    @Override
    public void startDocument() throws SAXException {
        this.inventory = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // Cada vez que empieza un elemento, reiniciamos el buffer
        value = "";
        parsedElement = qName;
        
        if ("product".equals(qName)) {
            // creas tu Product con atributos, etc.
            product = new Product(
                attributes.getValue("name"),
                new Amount(0),
                true,
                0
            );
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // Acumulamos todo el texto (sin procesarlo aún)
        value += new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String text = value.trim();       // eliminamos espacios y saltos
        if (!text.isEmpty()) {
            switch (qName) {
                case "wholesalePrice":    // coincidir con tu XML real
                    double precio = Double.parseDouble(text);
                    // si alguna vez amplías Amount para guardar currency, lo recogerás aquí
                    product.setWholesalerPrice(new Amount(precio));
                    break;
                case "stock":
                    int stock = Integer.parseInt(text);
                    product.setStock(stock);
                    break;
                // otros campos si tuvieras más...
            }
        }

        if ("product".equals(qName)) {
            // cuando se cierra <product> añadimos al inventario
            inventory.add(product);
        }

        // limpiamos para el siguiente elemento
        parsedElement = "";
        value = "";
    }

    @Override
    public void endDocument() throws SAXException {
        printDocument();
    }

    private void printDocument() {
        for (Product p : inventory) {
            System.out.println(p.toString());
        }
    }

}
