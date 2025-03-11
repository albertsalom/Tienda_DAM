package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.Product;
import model.Amount;
import utils.Constants;
import dao.DaoImplHibernate;
import dao.DaoImplJDBC;  
import main.Shop;

public class ShopView extends JFrame {
    private DaoImplHibernate dao; 
    private Shop shop;  // Instancia de Shop para acceder a la caja

    // Constructor
    public ShopView() {
        this.dao = new DaoImplHibernate();  // Inicializa DaoImplHibernate
        this.shop = new Shop();  // Inicializa la tienda

        // Configuración de la ventana
        setTitle("Gestión de la Tienda");
        setSize(400, 400);  // Ajustar tamaño para 9 botones
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));  // Disposición para 9 botones

        // Crear botones
        JButton exportInventoryButton = new JButton("0. Exportar inventario");
        JButton countCashButton = new JButton("1. Contar caja");
        JButton addProductButton = new JButton("2. Añadir producto");
        JButton addStockButton = new JButton("3. Añadir stock"); 
        JButton removeProductButton = new JButton("9. Eliminar producto");
        JButton exitButton = new JButton("10. Salir");

        // Añadir listeners a los botones
        exportInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportInventory();
            }
        });

        countCashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countCashRegister();  
            }
        });

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        addStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	addStock();
            }
        });

        removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeProduct(); 
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Añadir los botones al frame
        add(exportInventoryButton);
        add(countCashButton);
        add(addProductButton);
        add(addStockButton);
        add(removeProductButton);  
        add(exitButton);
    }

    // Método para mostrar el dinero en la caja, accediendo a la variable cash en Shop
    public void countCashRegister() {
        Amount cash = shop.getCash();  // Obtener el valor de cash desde Shop
        JOptionPane.showMessageDialog(this, "Dinero en caja: €" + cash);
    }

    // Método para exportar el inventario
    public void exportInventory() {
        boolean isExported = shop.writeInventory();

        if (isExported) {
        	JOptionPane.showMessageDialog(this, "El inventario ha sido exportado correctamente.");
        } else {
        	JOptionPane.showMessageDialog(this, "Hubo un problema al exportar el inventario.", "Error de Exportación", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para añadir un producto (placeholder)
    public void addProduct() {
        // Redirigir a ProductView para añadir un nuevo producto
        ProductView productView = new ProductView(shop, Constants.OPTION_ADD_PRODUCT);
        productView.setVisible(true); // Mostrar el diálogo de ProductView
    }

    // Método para eliminar un producto (placeholder)
    public void removeProduct() {
        // Redirigir a ProductView para eliminar un producto
        ProductView productView = new ProductView(shop, Constants.OPTION_REMOVE_PRODUCT);
        productView.setVisible(true); // Mostrar el diálogo de ProductView
    }
    
    public void addStock() {
        // Redirigir a ProductView para añadir stock
        ProductView productView = new ProductView(shop, Constants.OPTION_ADD_STOCK);
        productView.setVisible(true); // Mostrar el diálogo de ProductView
    }

    // Método principal para mostrar la ventana
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ShopView view = new ShopView();
                view.setVisible(true);
            }
        });
    }
}
