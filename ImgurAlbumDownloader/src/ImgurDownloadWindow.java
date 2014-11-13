import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.htmlparser.util.ParserException;


public class ImgurDownloadWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String title = "Imgur Album Downloader";
	private String downloadPath;
	private JButton download;
	private JButton savePath;
	private JTextField imgurURL;
	
	private void showFileChooser(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Choose where to save your images.");
		chooser.showOpenDialog(null);
		if((downloadPath = chooser.getSelectedFile().getAbsolutePath() + "\\") == null){
			setDefaultPath();
		}
	}
	
	private void setDefaultPath(){
		downloadPath = System.getProperty("user.home") + "\\Downloads\\";
	}
	
	public void buttonListeners(){
		download.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String url = imgurURL.getText().trim();
				Pattern p = Pattern.compile(ImgurAlbum.validURL);
				Matcher m = p.matcher(url);
				if (m.matches()) {
					if(downloadPath.equals("")){
						setDefaultPath();
					}
					if(!downloadPath.endsWith("\\")){
						downloadPath += "\\";
					}
					ImgurAlbum i = new ImgurAlbum(url, downloadPath);
					try {
						i.getImages();
					} catch (ParserException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		savePath.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFileChooser();
			}	
		});
	}
	
	private void initialize(){
		//Create all the components
		imgurURL = new JTextField("", 20);
		JLabel label = new JLabel("Imgur URL");
		download = new JButton("Download");
		savePath = new JButton("Set Download Path");
		//Done creating components
		
		//Set component attributes
		setLayout(new FlowLayout());
		setSize(575, 65);
		setResizable(false);
		add(label);
		add(imgurURL);
		add(download);
		add(savePath);
		//Done setting component attributes
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
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
