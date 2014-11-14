import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



public class ImgurDownloadWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String title = "Imgur Album Downloader";
	public static String downloadPath;
	public static String url;
	private JButton download;
	private JButton savePath;
	private JTextField imgurURL;
	private JLabel saveDirectory;
	
	private void showFileChooser(){
		JFileChooser chooser = new JFileChooser(System.getProperty("user.home") + "\\");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Choose where to save your images");
		chooser.showOpenDialog(null);
		if((downloadPath = chooser.getSelectedFile().getAbsolutePath() + "\\") == null){
			setDefaultPath();
		}
		if(!downloadPath.endsWith("\\")){
			downloadPath += "\\";
		}
		saveDirectory.setText("Downloading to: " + downloadPath);
	}
	
	private void setDefaultPath(){
		downloadPath = System.getProperty("user.home") + "\\Downloads\\";
		if(!downloadPath.endsWith("\\")){
			downloadPath += "\\";
		}
		saveDirectory.setText("Downloading to: " + downloadPath);
	}
	
	public void buttonListeners(){
		download.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(downloadPath.equals("")){
					setDefaultPath();
				}
				url = imgurURL.getText().trim();
				new DownloadingWindow();
				imgurURL.setText("");
			}
		});
		
		savePath.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFileChooser();
			}	
		});
	}
	
	private void initialize(){
		 try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Create all the components
		imgurURL = new JTextField("", 20);
		JLabel label = new JLabel("Imgur URL");
		download = new JButton("Download");
		savePath = new JButton("Set Download Path");
		saveDirectory = new JLabel("Downloading to: " + downloadPath);
		setDefaultPath();
		//Done creating components
		
		//Set component attributes
		setLayout(new FlowLayout());
		setSize(575, 100);
		setResizable(false);
		add(label);
		add(imgurURL);
		add(download);
		add(savePath);
		add(saveDirectory);
		//Done setting component attributes
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public ImgurDownloadWindow(){
		super(title);
		initialize();
		downloadPath = "";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ImgurDownloadWindow w = new ImgurDownloadWindow();
		w.buttonListeners();
	}

}
