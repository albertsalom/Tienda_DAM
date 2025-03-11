package dao;

import java.util.ArrayList;
import model.Employee;
import model.Product;

public interface Dao {

    // Método para establecer conexión
    public void connect();

    // Método para desconectar
    public void disconnect();

    // Obtener un empleado dado su id y contraseña
    public Employee getEmployee(int employeeId, String password);

    // Obtener el inventario de productos. No tiene parámetros de entrada y devuelve un ArrayList de productos
    public ArrayList<Product> getInventory();

    // Escribir o guardar el inventario, recibe un ArrayList de productos y devuelve true o false según si fue procesado correctamente
    public boolean writeInventory(ArrayList<Product> inventory);

    // Agregar un producto al inventario. Recibe un objeto Product y devuelve true o false según si fue procesado correctamente
    public boolean addProduct(Product product);

    // Actualizar un producto existente en el inventario. Recibe un objeto Product y devuelve true o false según si fue procesado correctamente
    public boolean updateProduct(Product product);

    // Eliminar un producto del inventario. Recibe el ID del producto y devuelve true o false según si fue procesado correctamente
    public boolean deleteProduct(int productId);
}
