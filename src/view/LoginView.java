package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import exception.LimitLoginException;
import model.Employee;
import dao.DaoImplMongoDB;
import utils.Constants;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class LoginView extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldEmployeeId;
    private JTextField textFieldPassword;
    private JButton btnLogin;
    private int counterErrorLogin = 0;  
    private DaoImplMongoDB dao; 

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginView frame = new LoginView();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public LoginView() {
        dao = new DaoImplMongoDB(); 

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblEmployeeId = new JLabel("Número de empleado");
        lblEmployeeId.setBounds(55, 50, 130, 14);
        contentPane.add(lblEmployeeId);

        textFieldEmployeeId = new JTextField();
        textFieldEmployeeId.setBounds(65, 75, 176, 20);
        contentPane.add(textFieldEmployeeId);
        textFieldEmployeeId.setColumns(10);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(55, 115, 130, 14);
        contentPane.add(lblPassword);

        textFieldPassword = new JTextField();
        textFieldPassword.setBounds(65, 140, 176, 20);
        contentPane.add(textFieldPassword);
        textFieldPassword.setColumns(10);

        btnLogin = new JButton("Acceder");
        btnLogin.setBounds(308, 208, 89, 23);
        contentPane.add(btnLogin);
        // Escuchar botón 
        btnLogin.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            if (counterErrorLogin >= Constants.MAX_LOGIN_TIMES) {
                JOptionPane.showMessageDialog(null, "Demasiados intentos fallidos. Intente más tarde.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String employeeIdStr = textFieldEmployeeId.getText();
            String password = textFieldPassword.getText();

            if (employeeIdStr.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Usuario y contraseña son obligatorios", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int employeeId = Integer.parseInt(employeeIdStr);
                Employee employee = dao.getEmployee(employeeId, password);

                if (employee != null) {
                    // Login exitoso, abrir ShopView
                    JOptionPane.showMessageDialog(null, "Bienvenido " + employee.getName(), "Login Exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                    ShopView shop = new ShopView();
                    shop.setExtendedState(NORMAL);
                    shop.setVisible(true);
                    dispose(); // Cierra la ventana de login
                } else {
                    counterErrorLogin++;
                    JOptionPane.showMessageDialog(null, "Usuario o password incorrectos ", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    
                    // Limpiar campos
                    textFieldEmployeeId.setText("");
                    textFieldPassword.setText("");

                    if (counterErrorLogin >= Constants.MAX_LOGIN_TIMES) {
                        throw new LimitLoginException("Se ha superado el número de intentos permitidos", counterErrorLogin);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El ID de empleado debe ser un número", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (LimitLoginException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        }
    }
}
