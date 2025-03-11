package dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Amount;
import model.Employee;
import model.Product;
import model.ProductHistory;
import model.Sale;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DaoImplHibernate implements Dao {

    private SessionFactory sessionFactory;
    private Session session;

    // Método para establecer conexión
    @Override
    public void connect() {
        try {
            // Configuración de Hibernate
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            session = sessionFactory.openSession();
            System.out.println("Conexión con Hibernate establecida.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar con Hibernate");
        }
    }

    // Método para desconectar
    @Override
    public void disconnect() {
        try {
            if (session != null) session.close();
            if (sessionFactory != null) sessionFactory.close();
            System.out.println("Conexión con Hibernate cerrada.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cerrar la conexión con Hibernate");
        }
    }

    // Implementación del método getInventory
    @Override
    public ArrayList<Product> getInventory() {
        try {
            // Establecemos conexión
            connect();
            
            // Usamos HQL para obtener todos los productos desde la base de datos
            List<Product> products = session.createQuery("FROM Product", Product.class).list();
            
            // Convertimos la lista a ArrayList y la retornamos
            return new ArrayList<>(products);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // Cerramos la conexión
            disconnect();
        }
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        
        return null;
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        try {
            connect();
            session.beginTransaction(); // Iniciamos la transacción

            for (Product product : inventory) {
                ProductHistory history = new ProductHistory();

                // Establecer los valores de history con los datos de product
                history.setId(product.getId());
                history.setName(product.getName());

                // Verificar si wholesalerPrice es null y, si lo es, asignar un valor por defecto
                Amount wholesalerPrice = product.getWholesalerPrice();
                if (wholesalerPrice == null) {
                    wholesalerPrice = new Amount(0.0);  // Asignar valor por defecto
                }

                // Extraer el valor de wholesalerPrice (de tipo Amount) y asignarlo como double
                history.setWholesalerPrice(wholesalerPrice.getValue());  // Aquí usamos getValue() para obtener el double

                history.setAvailable(product.isAvailable());
                history.setStock(product.getStock());

                // Establecer la fecha actual de creación
                history.setCreatedAt(new Timestamp(System.currentTimeMillis()));

                // Guardamos en la base de datos
                session.save(history);
            }

            session.getTransaction().commit(); // Confirmamos la transacción
            System.out.println("Inventario guardado correctamente en historical_inventory.");
            return true;

        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback(); // Rollback en caso de error
            }
            e.printStackTrace();
            return false;
        } finally {
            disconnect();
        }
    }




    @Override
    public boolean addProduct(Product product) {
        try {
            connect();
            session.beginTransaction(); // Iniciamos la transacción

            // Guardamos el producto en la base de datos
            session.save(product);

            // Crear y guardar el producto en la tabla ProductHistory
            ProductHistory productHistory = new ProductHistory(
                    product.getId(),
                    product.getName(),
                    product.getWholesalerPrice().getValue(),
                    product.isAvailable(),
                    product.getStock(),
                    new Timestamp(System.currentTimeMillis()) 
            );
            
            session.save(productHistory); // Guardamos la entrada en ProductHistory

            session.getTransaction().commit(); // Confirmamos la transacción
            System.out.println("Producto agregado correctamente: " + product.getName());
            return true;

        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback(); // Rollback en caso de error
            }
            e.printStackTrace();
            return false;
        } finally {
            disconnect();
        }
    }



    @Override
    public boolean updateProduct(Product product) {
        try {
            // Establecemos la conexión
            connect();
            
            // Iniciamos una transacción
            session.beginTransaction();
            
            // Buscamos el producto en la base de datos por su ID
            Product existingProduct = session.get(Product.class, product.getId());  // Usamos getId() en lugar de getIdProduct()
            
            // Verificamos si el producto existe
            if (existingProduct != null) {
                // Actualizamos los campos del producto existente con los nuevos valores
                existingProduct.setName(product.getName());
                existingProduct.setWholesalerPrice(product.getWholesalerPrice());
                existingProduct.setAvailable(product.isAvailable());
                existingProduct.setStock(product.getStock());

                // Guardamos el producto actualizado en la base de datos
                session.update(existingProduct);

                // También podemos crear un historial de actualización, similar a cuando agregamos el producto
                ProductHistory history = new ProductHistory(
                    product.getId(),  // Usamos getId() en lugar de getIdProduct()
                    product.getName(),
                    product.getWholesalerPrice().getValue(),
                    product.isAvailable(),
                    product.getStock(),
                    new Timestamp(System.currentTimeMillis()) // Fecha de la actualización
                );
                
                // Guardamos el historial de actualización
                session.save(history);

                // Confirmamos la transacción
                session.getTransaction().commit();

                System.out.println("Producto actualizado correctamente: " + product.getName());
                return true;
            } else {
                System.out.println("Producto no encontrado con ID: " + product.getId());
                return false;
            }
            
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback(); // Rollback en caso de error
            }
            e.printStackTrace();
            return false;
        } finally {
            // Cerramos la conexión
            disconnect();
        }
    }



    @Override
    public boolean deleteProduct(int productId) {
        try {
            // Establecemos la conexión
            connect();

            // Iniciamos una transacción
            session.beginTransaction();

            // Obtenemos el producto desde la base de datos usando su ID
            Product product = session.get(Product.class, productId);

            // Verificamos si el producto existe
            if (product != null) {
                // Eliminamos el producto
                session.delete(product);
                System.out.println("Producto con ID " + productId + " ha sido eliminado.");

                // Confirmamos la transacción
                session.getTransaction().commit();
                return true;
            } else {
                System.out.println("Producto no encontrado con ID: " + productId);
                return false;
            }
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback(); // Rollback en caso de error
            }
            e.printStackTrace();
            return false;
        } finally {
            // Cerramos la conexión
            disconnect();
        }
    }


	public void addSale(Sale sale) {
		// TODO Auto-generated method stub
		
	}


}

