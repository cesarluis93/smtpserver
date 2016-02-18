package client;

import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.Connector;
import data.DBManager;
import data.Tools;
import data.User;
import dns.DNSlookup;

import javax.swing.JTabbedPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SMTPClientGUIMain extends JFrame {

	private JPanel contentPane;
	private User loggedUser;
	private JSONArray correos;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Create the frame.
	 */
	public SMTPClientGUIMain(User user) {
		loggedUser = user;//usuario loggeado
		setTitle("SMTP Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 584, 261);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Bandeja de Entrada", null, panel, null);
		panel.setLayout(null);
		
		JTextArea textArea = new JTextArea();
		
		JList list = new JList();
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				//cargar la data del correo que se selecciona
				
//				ListModel mod = list.getModel();
//				Object o = mod.getElementAt(list.getSelectedIndex());
//				String contenidoCorreo = Tools.convertToContentJsonView(o.toString());
				int indice = list.getSelectedIndex();			
				if(indice != -1)
				{
					//setear data
					try {
						textArea.setText("Fecha: "+correos.getJSONObject(indice).get("date")
										+"\n"
										+"De: "+correos.getJSONObject(indice).get("from")
										+"\n\n"
										+correos.getJSONObject(indice).get("message"));
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
			}
		});
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//cargar correos del ususario loggeado
				Connector.connect();				
				DBManager dbm = new DBManager();
			
				correos = dbm.retrieveJsonMails(loggedUser.getUsername());
				if (correos != null)
				{
					//System.out.println("Correos de "+loggedUser.getUsername()+"\n" +Tools.convertToContentJsonView(correos.toString()));
					retrieveMails(correos, list);
				}					
				
				Connector.close();			
				
			}
		});
		btnRefresh.setBounds(10, 11, 89, 23);
		panel.add(btnRefresh);		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 40, 287, 182);
		panel.add(scrollPane);		
		
		scrollPane.setViewportView(list);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(310, 40, 259, 182);
		panel.add(scrollPane_1);
		
		
		scrollPane_1.setViewportView(textArea);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Nuevo Correo", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblFrom = new JLabel("From:");
		lblFrom.setBounds(10, 11, 46, 14);
		panel_1.add(lblFrom);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBounds(60, 14, 146, 20);
		textArea_1.setText(loggedUser.getUsername()+"@LabSMTP");
		panel_1.add(textArea_1);
		
		JLabel lblTo = new JLabel("To:");
		lblTo.setBounds(10, 45, 46, 14);
		panel_1.add(lblTo);
		
		//to
		textField = new JTextField();
		textField.setBounds(60, 42, 146, 20);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JLabel lblSubject = new JLabel("Subject:");
		lblSubject.setBounds(10, 70, 46, 14);
		panel_1.add(lblSubject);
		
		//subjet
		textField_1 = new JTextField();
		textField_1.setBounds(60, 73, 146, 20);
		panel_1.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblMessage = new JLabel("Message:");
		lblMessage.setBounds(300, 11, 72, 14);
		panel_1.add(lblMessage);
		
		//message
		JTextArea textArea_2 = new JTextArea();
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//enciar el mal a los usuarios correspondientes
				//realizar validaciones primero
				//validar todos los campos tengan data
				String mailTO = textField.getText();
				String mailSUBJECT = textField_1.getText();
				String mailMESSAGE = textArea_2.getText();
				
				if(!mailTO.isEmpty() && !mailSUBJECT.isEmpty() && !mailMESSAGE.isEmpty())
				{
					//validar To: sea separable y entendible
					
					String[] recipients = mailTO.split(",");
					for(String r : recipients)
					{
						//verificar mail valido asdf@asdf
						String[] temporal = r.split("@");
						if(temporal.length == 2)
						{
							String recipientUser = temporal[0];
							String recipientDomain = temporal[1];
							
							System.out.println("Validando DNS del usuario " + recipientDomain +"....");
							
							//armar mail
							//verificar si es local para almacenamiento
							if(recipientDomain.equals("LabSMTP"))
							{
								//local
								//armar mail
								
								
								
								
								//almacenar los locales
							}
							else
							{
								/*---------------------verify DNS antes de enviar-----------------------------*/
								//enviar a los remitentes, verificando existan 
								
								String[] test = {recipientDomain};
								String [] validar;
								
								DNSlookup dns = new DNSlookup();
								dns.getNames(test);
								
								
								// si el mxRecord no contiene DNS's válidos
								
								if(!dns.getStatus()){
									// el DNS es válido enviar email
								}
								else{
									// el DNS es NO válido 
									System.out.println(" DNS Inválido: Correo no puede enviarse!! ");
								}
								
								
								
								
								
								//armar mail en un string para enviar via TCP a los remitentes
								//el string debe llevar (usando enters siempre)
								//HELO LabSMTP \n 
								//MAIL FROM: elchavo@LabSMTP
								//RCPT TO: A quien corersponda
								//DATA
								//To:
								//From:
								//Subject:
								//Date:
								//
								//los datos
								//.
								//QUIT
								
								
								//enviar ese string mediante una conexion al puerto 25 de dicho dominio
								
							}							
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Correo invalido: "+r+"!");
						}
					}					
				}	
				else
				{
					JOptionPane.showMessageDialog(null, "No ingreso todos los datos!");
				}
			}
		});
		btnEnviar.setBounds(10, 199, 89, 23);
		panel_1.add(btnEnviar);
		
		
		
		JButton btnDescartar = new JButton("Descartar");
		btnDescartar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//limpiar to, subjet y mensaje
				textField.setText("");
				textField_1.setText("");
				textArea_2.setText("");
			}
		});
		btnDescartar.setBounds(109, 199, 89, 23);
		panel_1.add(btnDescartar);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(300, 32, 269, 190);
		panel_1.add(scrollPane_2);
		
		
		scrollPane_2.setViewportView(textArea_2);
	}
	
	public void retrieveMails(JSONArray correos, JList list)
	{
		//cargar mails en la bandeja de entrada (la lista)
		DefaultListModel listModel = new DefaultListModel();
		
		for(int i = 0 ; i < correos.length() ; i++)
		{
			try {
				listModel.addElement(correos.getJSONObject(i).get("date")+" -- "+correos.getJSONObject(i).get("from"));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		list.setModel(listModel);				
	}
}
