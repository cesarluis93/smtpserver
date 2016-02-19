package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import data.Connector;
import data.DBManager;
import data.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SMTPClientGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SMTPClientGUI frame = new SMTPClientGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SMTPClientGUI() {
		setTitle("LabSMTP Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUser = new JLabel("User:");
		lblUser.setBounds(22, 28, 46, 14);
		contentPane.add(lblUser);
		
		textField = new JTextField();
		textField.setBounds(20, 44, 91, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(22, 75, 67, 14);
		contentPane.add(lblPassword);
		
		textField_1 = new JTextField();
		textField_1.setBounds(20, 99, 91, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		// LOGIN de usuario
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				String userLogin = "", passwordLogin = "";
				
				userLogin = textField.getText();
				passwordLogin = textField_1.getText();
				
				if(!userLogin.isEmpty() && !passwordLogin.isEmpty())
				{
					//validar el usuario
					Connector.connect();		
					DBManager dbm = new DBManager();
					User usuario = dbm.login(userLogin, passwordLogin); //FALTA UNA QUE REVISE PASSWORD
					
					if(usuario != null)
					{
						Connector.close();
						
						//abrir un nuevo JFrame ocn el main y el usuario loggeado
						SMTPClientGUIMain mainFrame = new SMTPClientGUIMain(usuario);
						mainFrame.setVisible(true);
						
					}
					else
					{
						Connector.close();
						JOptionPane.showMessageDialog(null, "Usuario Invalido!");						
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "No ingreso todos los datos!");
				}								
			}
		});
		btnLogin.setBounds(22, 144, 89, 23);
		contentPane.add(btnLogin);
		
		JLabel lblNewUser = new JLabel("New User:");
		lblNewUser.setBounds(242, 28, 67, 14);
		contentPane.add(lblNewUser);
		
		textField_2 = new JTextField();
		textField_2.setBounds(242, 44, 99, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblNewpassword = new JLabel("NewPassword");
		lblNewpassword.setBounds(242, 75, 99, 14);
		contentPane.add(lblNewpassword);
		
		textField_3 = new JTextField();
		textField_3.setBounds(242, 99, 99, 20);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		// CREAR usuario
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//crear un nuevo usuario en la BD
				String newUser = "", newPassword="";
				
				newUser = textField_2.getText();
				newPassword = textField_3.getText();
				
				if(!newUser.isEmpty() && !newPassword.isEmpty())
				{
					//instanciar le usuario
					User usuario = new User(newUser);
					usuario.setPassword(newPassword);
					usuario.setBirthdate("01-01-2000");
					
					//guardar
					Connector.connect();
					
					usuario.save();
					
					Connector.close();
					//limpiar campos
					textField_2.setText("");
					textField_3.setText("");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Ingrese todos los datos!");
				}				
			}
		});
		btnCreate.setBounds(242, 144, 89, 23);
		contentPane.add(btnCreate);
	}
}
