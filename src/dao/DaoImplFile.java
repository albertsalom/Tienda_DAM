package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplFile implements Dao {

    @Override
    public void connect() {
        // TODO Auto-generated method stub
    }

    @Override
    public void disconnect() {
        // TODO Auto-generated method stub
    }
    
    @Override
    public Employee getEmployee(int employeeId, String password){
        return null;
    }

    @Override
	public ArrayList<Product> getInventory(){

        ArrayList<Product> inventory = new ArrayList<Product>();

        File f = new File(System.getProperty("user.dir") + File.separator + "files/inputInventory.txt");
		
		try {			
			// wrap in proper classes
			FileReader fr;
			fr = new FileReader(f);				
			BufferedReader br = new BufferedReader(fr);
			
			// read first line
			String line = br.readLine();
			
			// process and read next line until end of file
			while (line != null) {
				// split in sections
				String[] sections = line.split(";");
				
				String name = "";
				double wholesalerPrice=0.0;
				int stock = 0;
				
				// read each sections
				for (int i = 0; i < sections.length; i++) {
					// split data in key(0) and value(1) 
					String[] data = sections[i].split(":");
					
					switch (i) {
					case 0:
						// format product name
						name = data[1];
						break;
						
					case 1:
						// format price
						wholesalerPrice = Double.parseDouble(data[1]);
						break;
						
					case 2:
						// format stock
						stock = Integer.parseInt(data[1]);
						break;
						
					default:
						break;
					}
				}
				// add product to inventory
				inventory.add(new Product(name, new Amount(wholesalerPrice), true, stock));
				
				// read next line
				line = br.readLine();
			}
			fr.close();
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return inventory;
    }

    @Override
	public boolean writeInventory(ArrayList<Product> inventory){
        LocalDate fecha = LocalDate.now();
		String rutaCarpeta = System.getProperty("user.dir") + File.separator + "files";
		String rutaArchivo = rutaCarpeta + File.separator + "inventory_" + fecha.toString() + ".txt";
		LocalDateTime fechaHora = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/mm/yyyy hh:mm:ss");
        String fechaFormateada = fechaHora.format(formato);
		System.out.println(rutaArchivo);
		File file = new File(rutaArchivo);
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			int numeroTotalProductos = 0;
			for (Product product : inventory) {
				if (product != null) {
					pw.write(product.getId() + ";Product=" + product.getName() + ";Stock=" + product.getStock() + "\n");
					numeroTotalProductos++;
				}
			}
			pw.write("Numero total de productos: " + numeroTotalProductos);
            pw.write("Informe generado a dia: " + fechaFormateada);
			pw.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public void addProduct(Product product){
		// TODO Auto-generated method stub
	}
	public void updateProduct(String name, int stock){
		// TODO Auto-generated method stub
	}
	public void deleteProduct(String name){
		// TODO Auto-generated method stub
	}
}