package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;

import model.Amount;
import model.Employee;
import model.Product;
import model.Sale;

public class DaoImplMongoDB {
    private static final String URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "shop";
    private static final String COLLECTION_NAME = "inventory";
    
    private MongoClient mongoClient;
    private MongoDatabase database;

    public DaoImplMongoDB() {
        connect();
    }

    private void connect() {
        try {
            mongoClient = new MongoClient(new MongoClientURI(URI));
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Conectado a la base de datos MongoDB: " + DATABASE_NAME);
        } catch (Exception e) {
            System.err.println("Error al conectar a MongoDB: " + e.getMessage());
        }
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexión cerrada.");
        }
    }
    
    public Employee getEmployee(int employeeId, String password) {
        try {
            MongoCollection<Document> collection = database.getCollection("users");

            // Buscar el empleado con el ID y la contraseña en la base de datos
            Document doc = collection.find(Filters.and(
                    Filters.eq("employeeId", employeeId),
                    Filters.eq("password", password)
            )).first();

            // Si se encuentra el empleado, se devuelve como objeto Employee
            if (doc != null) {
                return new Employee(
                    doc.getInteger("employeeId"),
                    doc.getString("name"),
                    doc.getString("password") 
                );
            }

        } catch (Exception e) {
            System.err.println("Error al obtener el empleado: " + e.getMessage());
            e.printStackTrace();
        }

        return null; 
    }

    public ArrayList<Product> getInventory() {
        ArrayList<Product> inventory = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("inventory");

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            System.out.println("Cargando inventario...");

            while (cursor.hasNext()) {
                Document doc = cursor.next();

                // Acceder correctamente al campo wholesalerPrice.value
                Document priceDoc = (Document) doc.get("wholesalerPrice");
                double priceValue = priceDoc.getDouble("value");

                Product product = new Product(
                    doc.getInteger("id"),
                    doc.getString("name"),
                    new Amount(priceValue), 
                    doc.getBoolean("available"),
                    doc.getInteger("stock")
                );

                inventory.add(product);

                // Imprimir información del producto en la consola
                System.out.println("Producto cargado: ID=" + product.getId() +
                        ", Nombre=" + product.getName() +
                        ", Precio Mayorista=" + product.getWholesalerPrice().getValue() +
                        ", Disponible=" + product.isAvailable() +
                        ", Stock=" + product.getStock());
            }

            if (inventory.isEmpty()) {
                System.out.println("No hay productos en el inventario.");
            }

        } catch (Exception e) {
            System.err.println("Error al obtener el inventario: " + e.getMessage());
            e.printStackTrace();
        }

        return inventory;
    }
    
    public boolean writeInventory(ArrayList<Product> inventory) {
        try {
            MongoCollection<Document> collection = database.getCollection("historical_inventory");

            // Crear una lista de documentos para la inserción en batch
            List<Document> documents = new ArrayList<>();

            for (Product product : inventory) {
                Document productDoc = new Document("id_product", product.getId())
                        .append("name", product.getName())
                        .append("wholesalerPrice", new Document("value", product.getWholesalerPrice().getValue())
                                .append("currency", "€")) // Mantener el formato de currency
                        .append("available", product.isAvailable())
                        .append("stock", product.getStock())
                        .append("created_at", new Date()); // Guardar la fecha actual

                documents.add(productDoc);
            }

            // Insertar todos los productos en batch
            if (!documents.isEmpty()) {
                collection.insertMany(documents);
                System.out.println("Inventario histórico guardado correctamente.");
                return true;
            } else {
                System.out.println("No hay productos para guardar en el inventario histórico.");
                return false;
            }

        } catch (Exception e) {
            System.err.println("Error al escribir el inventario histórico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean addProduct(Product product) {
        try {
            MongoCollection<Document> collection = database.getCollection("inventory");

            // Crear el documento con la estructura esperada
            Document productDoc = new Document("id", product.getId())
                    .append("name", product.getName())
                    .append("wholesalerPrice", new Document("value", product.getWholesalerPrice().getValue())
                            .append("currency", "€"))
                    .append("available", product.isAvailable())
                    .append("stock", product.getStock());

            // Insertar el producto en la colección
            collection.insertOne(productDoc);
            System.out.println("Producto insertado correctamente: " + product.getName());

            return true; // Retorna true si la inserción fue exitosa

        } catch (Exception e) {
            System.err.println("Error al insertar el producto: " + e.getMessage());
            e.printStackTrace();
            return false; // Retorna false en caso de error
        }
    }

    public boolean deleteProduct(int productId) {
        try {
            MongoCollection<Document> collection = database.getCollection("inventory");

            // Eliminar el producto con el ID especificado
            DeleteResult result = collection.deleteOne(Filters.eq("id", productId));

            // Si se eliminó al menos un documento, la operación fue exitosa
            return result.getDeletedCount() > 0;

        } catch (Exception e) {
            System.err.println("Error al eliminar el producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        try {
            MongoCollection<Document> collection = database.getCollection("inventory");

            // Crear la actualización con los nuevos valores
            UpdateResult result = collection.updateOne(
                Filters.eq("id", product.getId()),  // Filtrar por el ID del producto
                Updates.combine(
                    Updates.set("name", product.getName()),
                    Updates.set("wholesalerPrice.value", product.getWholesalerPrice().getValue()),
                    Updates.set("wholesalerPrice.currency", "€"), // Asegurar que la moneda se mantenga
                    Updates.set("available", product.isAvailable()),
                    Updates.set("stock", product.getStock())
                )
            );

            // Verificar si al menos un documento fue modificado
            return result.getModifiedCount() > 0;

        } catch (Exception e) {
            System.err.println("Error al actualizar el producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void addSale(Sale sale) {
        // TODO Auto-generated method stub
    }
}
