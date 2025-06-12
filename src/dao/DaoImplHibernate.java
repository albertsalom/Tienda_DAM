package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import model.Amount;
import model.Employee;
import model.Product;
import model.ProductHistory;

public class DaoImplHibernate implements Dao{

    private Session session;
	private Transaction tx;

    @Override
	public void connect() {
        Configuration configuration = new Configuration().configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
    }

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		try{
            session.close();
        } catch (HibernateException e) {
        }
	}

	@Override
	public Employee getEmployee(int userNumber, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public ArrayList<Product> getInventory() {
        if (session == null || !session.isOpen()) connect();

        ArrayList<Product> inventory = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            List<Product> productList = session
                .createQuery("from Product", Product.class)
                .list();
            tx.commit();

            for (Product product : productList) {
                double cost = product.getPrice();
                product.setWholesalerPrice(new Amount(cost));
                product.setPublicPrice    (new Amount(cost * 2));
                inventory.add(product);
            }

            System.out.println("Get Inventory Successfully.");

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return inventory;
    }

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		try {
			tx = session.beginTransaction();

			//we generate and save all new Mark elements
			for (Product product : inventory) {
                ProductHistory inventoryHistory = new ProductHistory(product.getId(), product.getName(), product.getPrice(), product.isAvailable(), product.getStock());
				session.save(inventoryHistory);			
			}
			// we update this information in the Database
			tx.commit();
			System.out.println("Write Inventory Successfully.");
			return true;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback(); // Roll back if any exception occurs.
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void addProduct(Product product) {
		// TODO Auto-generated method stub
		try {

			tx = session.beginTransaction();

			session.save(product);

			tx.commit();
			System.out.println("Product inserted Successfully.");
			
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback(); // Roll back if any exception occurs.
			e.printStackTrace();
		}
	
	}

	@Override
	public void updateProduct(String name, int stock) {
		// TODO Auto-generated method stub
		try {
			for (Product product : this.getInventory()) {
				if (product.getName().equals(name)) {
					product.setStock(product.getStock() + stock);
					tx = session.beginTransaction();
					session.save(product);
					tx.commit();
					System.out.println("Stock added Successfully.");					
				}				
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback(); // Roll back if any exception occurs.
			e.printStackTrace();
		}
	}

	@Override
	public void deleteProduct(String name) {
		// TODO Auto-generated method stub		
		try {
			for (Product product : this.getInventory()) {
				if (product.getName().equals(name)) {
					tx = session.beginTransaction();
					session.remove(product);
					tx.commit();
					System.out.println("Deleted Successfully.");					
				}				
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback(); // Roll back if any exception occurs.
			e.printStackTrace();
		}
	}
    
}
