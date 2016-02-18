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
import javax.swing.JTabbedPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class SMTPClientGUIMain extends JFrame {

	private JPanel contentPane;
	private User loggedUser;
	private JSONArray correos;

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
					System.out.println("Correos de "+loggedUser.getUsername()+"\n" +Tools.convertToContentJsonView(correos.toString()));
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
