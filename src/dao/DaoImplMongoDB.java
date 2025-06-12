package dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import static com.mongodb.client.model.Filters.eq;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;


import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplMongoDB implements Dao {

    MongoCollection<Document> collection;
    MongoClient mongoClient;
	MongoDatabase mongoDatabase;
    ObjectId id;

    @Override
	public void connect() {
		String uri = "mongodb://localhost:27017";
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		mongoClient = new MongoClient(mongoClientURI);

		mongoDatabase = mongoClient.getDatabase("JAVA-SHOP-M06");
	}

    @Override
    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexión cerrada correctamente.");
        }
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
    System.out.println("holacaracola");
    
    Employee employee = null;
    
    collection = mongoDatabase.getCollection("users");

    if (collection == null) {
        System.out.println("No se pudo conectar a la base de datos.");
    } else {
        System.out.println("Conexión exitosa.");
    }

    Document employeeDoc = collection.find(Filters.eq("employeeId", employeeId)).first();

    if (employeeDoc != null) {
        String storedPassword = employeeDoc.getString("password");

        // if (storedPassword != null && storedPassword.equals(password)) {
        //     employee = new Employee(
        //         employeeDoc.getInteger("employeeId"),
        //         employeeDoc.getString("name"),
        //         employeeDoc.getString("password")
        //     );
        // }

        if (storedPassword != null) {
            if (storedPassword.equals(password)) {
                employee = new Employee(
                    employeeDoc.getInteger("employeeId"),
                    employeeDoc.getString("name"),
                    storedPassword
                );
            } else {
                System.out.println("Contraseña incorrecta");
            }
        } else {
            System.out.println("Empleado no encontrado o contraseña no disponible.");
        }
}
    return employee;
    }

    @Override
    public ArrayList<Product> getInventory() {

        ArrayList<Product> inventory = new ArrayList<>();
        
        collection = mongoDatabase.getCollection("inventory");
        
        Iterable<Document> documents = collection.find();
        
        for (Document document : documents) {
             // Extraer unicamente el precio del wholesalerPrice

            Document priceDoc = (Document) document.get("wholesalerPrice");
            double priceValue = priceDoc != null ? priceDoc.getDouble("value") : 0.0;

            Amount wholesalerPrice = new Amount(priceValue);

            Product product = new Product (
                document.getString("name"),
                wholesalerPrice,
                document.getBoolean("available", false),
                document.getInteger("stock")
            );

            inventory.add(product);
        }

        for (Product product : inventory) {
            System.out.println(product);
        };

        return inventory;
    }

    @Override
    public boolean writeInventory(ArrayList<Product> products) {
        
        LocalDateTime fechaHora = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/mm/yyyy hh:mm:ss");
        String fechaFormateada = fechaHora.format(formato);
    
        collection = mongoDatabase.getCollection("historical_inventory");

        try {
            for (Product product: products) {

                System.out.println(product);
                Document document = new Document ("_id", new ObjectId())
                .append("name", product.getName())
                .append("wholesalerPrice", 
                    new Document("value", product.getWholesalerPrice().getValue())
                    .append("currency", "€"))
                .append("available", product.isAvailable()).append("stock", product.getStock())
                .append("id", product.getId())
                .append("created_at", fechaFormateada);

                collection.insertOne(document);

            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
    }

    public void addProduct(Product product){
        
        collection = mongoDatabase.getCollection("inventory");

        Boolean stock = true;

        if (product.getStock() <= 0) {
            stock = false;
        }

        Document document = new Document ("_id", new ObjectId())
        .append("name", product.getName())
        .append("wholesalerPrice", new Document("value", product.getWholesalerPrice().getValue()).append("currency", "€"))
        .append("available", stock).append("stock", product.getStock())
        .append("id", product.getId());

        collection.insertOne(document);
	}
	public void updateProduct(String name, int stock){        
        collection = mongoDatabase.getCollection("inventory");

        boolean isAvailable = true;

        if (stock <= 0) {
            isAvailable = false;
        }

        System.out.println("Entrando en updateProduct...");
        try {
            UpdateResult result = collection.updateOne(eq("name", name),combine(set("stock", stock), set("available", isAvailable)));
            System.out.println("se ha modificado el producto");        
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error al modificar el producto");        
        }

	}
	public void deleteProduct(String name){

        collection = mongoDatabase.getCollection("inventory");

        try {
            DeleteResult result = collection.deleteOne(eq("name", name)); 
            System.out.println("se ha eliminado el producto correctamente");        
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Da error al eliminar el producto");        
        }
	}

}
