package dao;

import model.Product;
import dao.xml.*;
import model.Amount;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import main.Shop;  
import org.xml.sax.SAXException;

public class DaoImplFile implements Dao {
    
    private Shop shop; 

    private static final String INVENTORY_FILE = "files/inputInventory.txt";  
    private static final String INVENTORY_XML_FILE = "files/inputInventory.xml";

    @Override
    public ArrayList<Product> getInventory() {
        ArrayList<Product> inventory = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Procesar cada línea y extraer datos del producto
                String[] productDetails = line.split(";");

                // Crear variables auxiliares
                String productName = null;
                double wholesalePrice = 0;
                int stock = 0;

                // Iterar por los detalles del producto
                for (String detail : productDetails) {
                    String[] keyValue = detail.split(":");
                    if (keyValue.length != 2) {
                        continue;
                    }

                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    if (key.equals("Product")) {
                        productName = value;
                    } else if (key.equals("Wholesaler Price")) {
                        try {
                            wholesalePrice = Double.parseDouble(value);
                        } catch (NumberFormatException e) {
                            System.out.println("Error al convertir el precio a número: " + value);
                        }
                    } else if (key.equals("Stock")) {
                        try {
                            stock = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            System.out.println("Error al convertir el stock a número: " + value);
                        }
                    }
                }

                // Verificar que se haya leído correctamente el producto antes de agregarlo al inventario
                if (productName != null && wholesalePrice > 0 && stock >= 0) {
                    // Crear el objeto Amount para el precio mayorista
                    Amount wholesalerPriceAmount = new Amount(wholesalePrice);
                    // Instanciar el producto con el constructor adecuado
                    Product product = new Product(productName, wholesalerPriceAmount, true, stock);
                    inventory.add(product);
                } else {
                    System.out.println("Datos incompletos o incorrectos para el producto: " + line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer el inventario desde el archivo: " + e.getMessage());
        }

        return inventory;
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        // Define el nombre del archivo en función de la fecha actual
        LocalDate currentDate = LocalDate.now();
        String fileName = "inventory_" + currentDate.toString() + ".txt";

        // Localiza la ruta del archivo y el nombre
        File file = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + fileName);

        try {
            // Envolver en clases adecuadas para la escritura
            FileWriter fw = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fw);

            // Contador de productos
            int counterProduct = 1;

            // Escribir línea por línea el inventario
            for (Product product : inventory) {
                // Primera línea: ContadorProducto;Nombre=Manzana;Wholesaler Price=10.00€;Stock=20;
                StringBuilder productLine = new StringBuilder(counterProduct + ";");
                productLine.append("Product=" + product.getName() + ";");
                productLine.append("Wholesaler Price=" + product.getWholesalerPrice() + ";");
                productLine.append("Stock=" + product.getStock() + ";");

                // Escribir la línea del producto
                pw.write(productLine.toString());
                pw.write("\n");

                // Incrementar el contador de productos
                counterProduct++;
            }

            // Cerrar archivos
            pw.close();
            fw.close();

            return true; // Indica que el inventario fue escrito correctamente
        } catch (IOException e) {
            System.out.println("Error al escribir el inventario: " + e.getMessage());
            return false; // Indica que hubo un error al escribir el inventario
        }
    }
    
    public boolean writeInventoryXML(ArrayList<Product> inventory) {
        ArrayList<Product> products = null;

        // Leer el documento XML existente
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;
        try {
            parser = factory.newSAXParser();
            File inputFile = new File("files/inputInventory.xml"); // Ruta corregida

            // Verificar si el archivo existe
            if (!inputFile.exists()) {
                System.out.println("ERROR: Archivo 'inputInventory.xml' no encontrado en la ruta especificada.");
                return false;
            }

            SaxReader saxReader = new SaxReader();
            parser.parse(inputFile, saxReader);
            products = saxReader.getProductList();
            
        } catch (ParserConfigurationException | SAXException e) {
            System.out.println("ERROR creating the parser");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR: Archivo 'inputInventory.xml' no se pudo leer.");
            e.printStackTrace();
        }

        // Crear un nuevo documento XML ordenado por stock en orden descendente
        if (products != null) {
            products.sort(Comparator.comparingInt(Product::getStock).reversed());
            DomWriter domWriter = new DomWriter();

            // Crear el directorio de salida si no existe
            File outputDir = new File("files");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            domWriter.generateDocument(products);
            return true; // Indicar que el XML se generó correctamente
        } else {
            System.out.println("ERROR: La lista de productos está vacía o hubo un problema al procesar el XML de entrada.");
            return false; // Indicar que hubo un problema al leer o procesar el XML
        }
    }


    @Override
    public void connect() {      
    }

    @Override
    public void disconnect() {       
    }

    @Override
    public model.Employee getEmployee(int employeeId, String password) {
        return null;
    }

	@Override
	public boolean addProduct(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateProduct(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteProduct(int productId) {
		// TODO Auto-generated method stub
		return false;
	}
}

