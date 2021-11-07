package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.Product;

public class ClientApp extends JFrame {

	private static final long serialVersionUID = 7090060221530791948L;
	private JTextField txtProductName;
	private JButton btnSearch;
	private JTextArea ta;
	private DataOutputStream out;
	private ObjectInputStream in;
	private JTextField txtProductID;
	private JButton btnNotSold;
	private JButton btnTotal;

	public ClientApp() {
		setTitle("Student Manager");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		Container cp = getContentPane();

//		Box b;
//		cp.add(b = Box.createHorizontalBox(), BorderLayout.NORTH);
//		b.add(txtProductName = new JTextField("Frameset"));
//		b.add(btnSearch = new JButton("Search Product"));
		cp.add(new JScrollPane(ta = new JTextArea()));

		Box mainBox = Box.createVerticalBox();
		cp.add(mainBox, BorderLayout.NORTH);

		Box b1 = Box.createHorizontalBox();
		b1.add(txtProductName = new JTextField("Frameset"));
		b1.add(btnSearch = new JButton("Search Product"));
		mainBox.add(b1);

		Box b2 = Box.createHorizontalBox();
		b2.add(btnNotSold = new JButton("Product not sold"));
		mainBox.add(b2);
		
		Box b3 = Box.createHorizontalBox();
		b3.add(btnTotal = new JButton("Get total Product"));
		mainBox.add(b3);

		new Thread(() -> {
			try {
				Socket socket = new Socket("192.168.1.2", 9080);

				out = new DataOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

		btnSearch.addActionListener((e) -> {
			System.out.println("dang tim");
			try {
				// gửi kết quả
				String productTitle = txtProductName.getText();

				out.writeUTF("1>" + productTitle);

				// Nhận kết quả
				@SuppressWarnings("unchecked")
				List<Product> products = (List<Product>) in.readObject();

				if (products == null) {
					ta.append("null");
				} else {

					ta.setText("");
					for (Product product : products) {
						SwingUtilities.invokeLater(() -> {
							ta.append("\n" + product.toString());
						});
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		btnNotSold.addActionListener((e) -> {
			System.out.println("dang tim2");
			try {
				// gửi kết quả
//				String productTitle = txtProductID.getText();
				out.writeUTF("2>" + "null");

				// Nhận kết quả
				List<Product> products =  (List<Product>) in.readObject();

				if (products == null) {
					ta.append("null");
				} else {

					ta.setText("San pham chua ban dc");
					for (Product product : products) {
						SwingUtilities.invokeLater(() -> {
							ta.append("\n" + product.toString());
						});
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		btnTotal.addActionListener((e) -> {
			System.out.println("dang tim3");
			try {
				// gửi kết quả
//				String productTitle = txtProductID.getText();
				out.writeUTF("3>" + "null");
				
				// Nhận kết quả
				int total = (int) in.readObject();
				
				if (total == 0) {
					ta.append("null");
				} else {
					
					ta.setText("Tong so san pham: " + total);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new ClientApp().setVisible(true);
		});
	}
}
