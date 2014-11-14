import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class DownloadingWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DownloadingWindow(){
		super("Downloading...");
		initialize();
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
		JLabel label = new JLabel("Downloading from " + ImgurDownloadWindow.url + " to " + ImgurDownloadWindow.downloadPath);
		//Set component attributes
		setLayout(new FlowLayout());
		setSize(550, 65);
		setResizable(false);
		add(label);
		//Done setting component attributes
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//JFrame.setDefaultLookAndFeelDecorated(true);
		setVisible(true);
		new Thread(new Downloader(this)).start();
	}

}
