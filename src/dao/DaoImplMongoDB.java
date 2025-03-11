package dao;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import model.Product;
import model.Amount;
import model.Employee;

public class DaoImplMongoDB implements Dao {

    private static final String CONNECTION_STRING = "mongodb://localhost:27017"; // URL de conexión
    private static final String DATABASE_NAME = "shop"; // Nombre de la base de datos
    private static final String COLLECTION_NAME = "inventory"; // Nombre de la colección

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> productCollection;

    @Override
    public void connect() {
        mongoClient = MongoClients.create(CONNECTION_STRING);
        database = mongoClient.getDatabase(DATABASE_NAME);
        productCollection = database.getCollection(COLLECTION_NAME);
    }

    @Override
    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    public ArrayList<Product> getInventory() {
        ArrayList<Product> inventory = new ArrayList<>();

        // Obtener todos los documentos de la colección "inventory"
        FindIterable<Document> documents = productCollection.find();

        // Iterar sobre los documentos y convertirlos en objetos Product
        for (Document doc : documents) {
            // Crear un nuevo objeto Product a partir del documento
            Product product = new Product();
            product.setId(doc.getInteger("id"));
            product.setName(doc.getString("name"));

            // Convertir el subdocumento "wholesalerPrice" en un objeto Amount
            Document priceDoc = (Document) doc.get("wholesalerPrice");
            if (priceDoc != null) {
                double value = priceDoc.getDouble("value");
                String currency = priceDoc.getString("currency");
                Amount amount = new Amount(value);
                amount.setCurrency(currency);
                product.setWholesalerPrice(amount);
            }

            // Asignar los demás atributos del producto
            product.setAvailable(doc.getBoolean("available"));
            product.setStock(doc.getInteger("stock"));

            // Agregar el producto a la lista
            inventory.add(product);
        }

        return inventory;
    }


    @Override
    public Employee getEmployee(int employeeId, String password) {
        return null;
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        return false;
    }

    @Override
    public boolean addProduct(Product product) {
        return false;
    }

    @Override
    public boolean updateProduct(Product product) {
        return false;
    }

    @Override
    public boolean deleteProduct(int productId) {
        return false;
    }
}


