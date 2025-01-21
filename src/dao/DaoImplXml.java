package dao;

import dao.xml.DomWriter;
import dao.xml.SaxReader;
import model.Employee;
import model.Product;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoImplXml implements Dao {

    private static final String INPUT_FILE = "files/inputInventory.xml";

    @Override
    public void connect() throws SQLException {
        // Conexión no aplicable en XML, no hace nada
        System.out.println("Conexión no requerida para archivos XML.");
    }

    @Override
    public void disconnect() throws SQLException {
        // Desconexión no aplicable en XML, no hace nada
        System.out.println("Desconexión no requerida para archivos XML.");
    }

    @Override
    public Employee getEmployee(int employeeId, String password) throws SQLException {
        // No es aplicable en la gestión de inventarios XML
        throw new UnsupportedOperationException("Operación no soportada para XML.");
    }

    @Override
    public ArrayList<Product> getInventory() {
        try {
            // Usar SaxReader para parsear el archivo XML
            SaxReader saxReader = new SaxReader();
            File inputFile = new File(INPUT_FILE);

            if (!inputFile.exists()) {
                System.err.println("Archivo no encontrado: " + INPUT_FILE);
                return new ArrayList<>(); // Devuelve lista vacía si el archivo no existe
            }

            saxReader.parse(inputFile); // Método parse implementado en SaxReader
            return new ArrayList<>(saxReader.getProductList()); // Convertir a ArrayList
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Devuelve lista vacía en caso de error
        }
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        try {
            // Usar DomWriter para generar el archivo XML
            DomWriter domWriter = new DomWriter();
            domWriter.writeInventoryFile(inventory);
            return true; // Retorna true si la escritura fue exitosa
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Retorna false en caso de error
        }
    }
}
