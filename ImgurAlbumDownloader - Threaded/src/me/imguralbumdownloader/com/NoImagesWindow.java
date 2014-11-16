package me.imguralbumdownloader.com;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author Micah Lee
 * NoImagesWindow is purely an error message
 */
public class NoImagesWindow extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Constructor
	 */
	public NoImagesWindow() {
		setTitle("Error");
		setIconImage(Toolkit.getDefaultToolkit().getImage(NoImagesWindow.class.getResource("/me/imguralbumdownloader/com/icon-imgur.png")));
		setBounds(100, 100, 299, 149);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblNoImagesWere = new JLabel("No images were found!");
		lblNoImagesWere.setFont(new Font("Tahoma", Font.PLAIN, 12));
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}	
		});
		
		setLocationRelativeTo(null);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(98)
					.addComponent(btnOk, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
					.addGap(107))
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(71)
					.addComponent(lblNoImagesWere, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(74))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addGap(22)
					.addComponent(lblNoImagesWere, GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(btnOk, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(23))
		);
		contentPanel.setLayout(gl_contentPanel);
	}
}
