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
import javax.swing.JDialog;
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

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

/**
 * 
 * @author Micah Lee
 * ImgurWindow is the main window of the application. This will be the first window the user sees, and will be responsible for creating
 * download windows, and setting the download path.
 *
 */
public class ImgurWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	//Regex to match to determine if the URL is a legal imgur format
	public static final String validURL = "(https?)\\:\\/\\/(www\\.)?(?:m\\.)?imgur\\.com/a/([a-zA-Z0-9]+)(#[0-9]+)?";
	private JPanel contentPane;
	private JButton setPath;
	private JButton download;
	private JButton showHidden;
	private JTextField imgurURLField;
	private JTextArea currentPath;
	private JMenuBar menuBar;
	private JMenu mnMenu;
	private JMenuItem help;
	private JMenuItem about;
	private static JComboBox<String> fileType;
	//The path where the images will be downloaded
	private static String downloadPath = System.getProperty("user.home") + "\\";
	//The url to download from
	private static String imgurURL;
	
	/**
	 * 
	 * @param args
	 * Execute the program by creating a new thread to be invoked later by the swing event dispatcher
	 */
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
	
	/**
	 * Constructor
	 */
	public ImgurWindow() {
		//Set the look and feel of the window
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
		
		//Set window attributes
		setTitle("Imgur Album Downloader");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ImgurWindow.class.getResource("/me/imguralbumdownloader/com/icon-imgur.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 435, 156);
		
		//Set up and create the content for the pane
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		help = new JMenuItem("Help");
		mnMenu.add(help);
		about = new JMenuItem("About");
		mnMenu.add(about);
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
		//Set the default path to the user's downloads by default
		setDefaultPath();
		
		//Set up the Group Layout (Auto generated by Eclipse Window Builder)
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
	
	/**
	 * activateListeners will set up all action listeners for JFrame content
	 */
	private void activateListeners(){
		//If user clicked Set Path, then open the file chooser so the user can choose their own path
		setPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showFileChooser();
			}
		});
		
		//If the user clicked Show Hidden Windows this window (main window) will find all child windows and show them
		showHidden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Window[] windows = JFrame.getWindows();
				for(Window w : windows){
					if(w.isEnabled() && w instanceof DownloadingWindow){
						w.setVisible(true);
					}
				}
			}
		});
		
		//If the user clicked the Download button then the download process will start
		download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//If downloadPath is blank, then set the default path
				if(downloadPath.equals("")){
					setDefaultPath();
				}
				
				//Obtain the text from the imgurURL text field
				imgurURL = imgurURLField.getText().trim();
				
				//Match the imgurURL to regex
				Pattern p = Pattern.compile(validURL);
				Matcher m = p.matcher(imgurURL);
				
				//If valid URL
				if (m.matches()){
					//Disable fileType while download process sets up so user cannot interfere
					fileType.setEnabled(false);
					//Use the swing event dispatcher to create a new Downloading window which will
					//execute any download processes
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
				//If invalid URL, show warning message and return to main window
				else{
					JOptionPane.showMessageDialog(null, "Invalid URL has been entered.", "Error", JOptionPane.ERROR_MESSAGE);
					fileType.setSelectedItem(0);
				}
				//Reset the 
				imgurURLField.setText("");
			}
		});
		
		//If user presses "Enter" Key on the keyboard, then activate download button
		imgurURLField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				download.doClick();
			}
		});
		
		//If help button was pressed from the menu, then display the help window
		help.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				try {
					HelpWindow dialog = new HelpWindow();
					dialog.setModal(true);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
			
		});
		
		//If about button was pressed from the menu, then display the about window
		about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				try {
					AboutWindow dialog = new AboutWindow();
					dialog.setModal(true);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		//If user tries to exit, first confirm to prevent any unwanted activity
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
		    	int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit? \nAny opened windows will continue downloading.", "", JOptionPane.YES_NO_OPTION);
			    if (confirmed == JOptionPane.YES_OPTION) {
			    	setVisible(false);
			    	dispose();
			    }
			}
		});
	}
	
	/**
	 * getFileType will return the type of file the user selected.
	 * This method will also re-enable the fileType so the user can continue with another download
	 * @return type - String - the type of files to download
	 */
	public static String getFileType(){
		//Get the selected file type
		String type = (String) fileType.getSelectedItem();
		//Re-enable the combobox, since download has already started
		fileType.setEnabled(true);
		//Reset the default file type
		fileType.setSelectedIndex(0);
		return type;
	}
	
	/**
	 * showFileChooser will display a file chooser so that the user can select
	 * the path where their images are downloaded
	 */
	private void showFileChooser(){
		//Create the file chooser with a default directory of the last downloadPath (default will be user.home\Downloads\)
		JFileChooser chooser = new JFileChooser(downloadPath);
		//Allow the file chooser to only select directories
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//Set the title
		chooser.setDialogTitle("Choose where to save your images");
		//If the user chose a directory, then set the new path
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){	
			downloadPath = chooser.getSelectedFile().getAbsolutePath() + "\\";
		}
		//If the user did not choose a directory, then reset to default path
		else{
			setDefaultPath();
		}
		//Check to see if the path is completed by a \
		if(!downloadPath.endsWith("\\")){
			downloadPath += "\\";
		}
		//Update to let the user see where their files are downloaded
		currentPath.setText(downloadPath);
	}
	
	/**
	 * setDefault path will set downloadPath to a default directory of user home\Downloads\
	 */
	private void setDefaultPath(){
		downloadPath = System.getProperty("user.home") + "\\Downloads\\";
		if(!downloadPath.endsWith("\\")){
			downloadPath += "\\";
		}
		currentPath.setText(downloadPath);
	}
}
