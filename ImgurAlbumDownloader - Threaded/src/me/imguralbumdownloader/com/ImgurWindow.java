package me.imguralbumdownloader.com;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Toolkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Window;

public class ImgurWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final String validURL = "(https?)\\:\\/\\/(www\\.)?(?:m\\.)?imgur\\.com/a/([a-zA-Z0-9]+)(#[0-9]+)?";
	private JPanel contentPane;
	private JButton setPath;
	private JButton download;
	private JButton showHidden;
	private JTextField imgurURLField;
	private JTextArea currentPath;
	private static String downloadPath;
	private static String imgurURL;
	private static JComboBox<String> fileType;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImgurWindow frame = new ImgurWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ImgurWindow() {
		try {
            // Set System L&F
	        UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // handle exception
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }

		setTitle("Imgur Album Downloader");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ImgurWindow.class.getResource("/me/imguralbumdownloader/com/icon-imgur.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 435, 129);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		imgurURLField = new JTextField();
		imgurURLField.setColumns(20);
		
		JLabel lblImgurUrl = new JLabel("Imgur URL:");
		
		fileType = new JComboBox<String>();
		fileType.setModel(new DefaultComboBoxModel(new String[] {"all", "jpg", "png", "gif", "webm/mp4"}));
		
		download = new JButton("Download");
		
		setPath = new JButton("Set Path");	
		
		showHidden = new JButton("Show Hidden Windows");
		
		currentPath = new JTextArea();
		currentPath.setBackground(SystemColor.control);
		currentPath.setEnabled(false);
		currentPath.setFont(new Font("Tahoma", Font.PLAIN, 11));
		currentPath.setEditable(false);
		setDefaultPath();
		
		setLocationRelativeTo(null);
		
		activateListeners();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(3)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblImgurUrl, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(imgurURLField, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(fileType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(download))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(setPath)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(currentPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(4, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(274, Short.MAX_VALUE)
					.addComponent(showHidden)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblImgurUrl, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(imgurURLField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(fileType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(download))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(setPath)
						.addComponent(currentPath, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(showHidden))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	private void activateListeners(){
		setPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showFileChooser();
			}
		});
		
		showHidden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Window[] windows = JFrame.getWindows();
				for(Window w : windows){
					w.setVisible(true);
				}
			}
		});
		
		download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(downloadPath.equals("")){
					setDefaultPath();
				}
				imgurURL = imgurURLField.getText().trim();
				Pattern p = Pattern.compile(validURL);
				Matcher m = p.matcher(imgurURL);
				if (m.matches()){
					fileType.setEnabled(false);
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								DownloadingWindow frame = new DownloadingWindow(imgurURL, downloadPath);
								frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				else{
					JOptionPane.showMessageDialog(null, "Invalid URL has been entered.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				imgurURLField.setText("");
				fileType.setSelectedIndex(0);
			}
		});
		
		imgurURLField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(downloadPath.equals("")){
					setDefaultPath();
				}
				imgurURL = imgurURLField.getText().trim();
				Pattern p = Pattern.compile(validURL);
				Matcher m = p.matcher(imgurURL);
				if (m.matches()){
					fileType.setEnabled(false);
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								DownloadingWindow frame = new DownloadingWindow(imgurURL, downloadPath);
								frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				else{
					JOptionPane.showMessageDialog(null, "Invalid URL has been entered.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				imgurURLField.setText("");
				fileType.setSelectedIndex(0);
			}
		});
		
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
		    	int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program? \nAny opened windows will continue downloading.", "", JOptionPane.YES_NO_OPTION);
			    if (confirmed == JOptionPane.YES_OPTION) {
			    	setVisible(false);
			    	dispose();
			    }
			}
		});
	}
	
	public static String getFileType(){
		String type = (String) fileType.getSelectedItem();
		fileType.setEnabled(true);
		return type;
	}
	
	private void showFileChooser(){
		JFileChooser chooser = new JFileChooser(System.getProperty("user.home") + "\\");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Choose where to save your images");
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){	
			downloadPath = chooser.getSelectedFile().getAbsolutePath() + "\\";
		}
		else{
			setDefaultPath();
		}
		if(!downloadPath.endsWith("\\")){
			downloadPath += "\\";
		}
		currentPath.setText(downloadPath);
	}
	
	private void setDefaultPath(){
		downloadPath = System.getProperty("user.home") + "\\Downloads\\";
		if(!downloadPath.endsWith("\\")){
			downloadPath += "\\";
		}
		currentPath.setText(downloadPath);
	}
}
