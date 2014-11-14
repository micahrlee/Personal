package me.imguralbumdownloader.com;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

public class DownloadingWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JProgressBar progressBar;
	private JLabel nowDownloading;
	private JLabel fileName;
	private JButton cancelDownload;
	private JButton background;
	private ImgurAlbumDownloader iad;
	
	public DownloadingWindow(String url, String downloadPath) {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(DownloadingWindow.class.getResource("/me/imguralbumdownloader/com/icon-imgur.png")));
		setTitle("Downloading...");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 439, 131);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		nowDownloading = new JLabel("Downloading from " + url + " to " + downloadPath);
		
		progressBar = new JProgressBar();
		
		cancelDownload = new JButton("Cancel Download");
		
		background = new JButton("Download in Background");
		
		fileName = new JLabel("File Name");
		
		switch(ImgurWindow.getFileType()){
			case "all":
				iad = new ImgurAlbumDownloader(url, downloadPath, ImgurAlbumDownloader.all, this);
				break;
			case "jpg":
				iad = new ImgurAlbumDownloader(url, downloadPath, ImgurAlbumDownloader.jpgs, this);
				break;
			case "gif":
				iad = new ImgurAlbumDownloader(url, downloadPath, ImgurAlbumDownloader.gifs, this);
				break;
			case "png":
				iad = new ImgurAlbumDownloader(url, downloadPath, ImgurAlbumDownloader.pngs, this);
				break;
			case "webm/mp4":
				iad = new ImgurAlbumDownloader(url, downloadPath, ImgurAlbumDownloader.webm, this);
				break;
		}
		setLocationRelativeTo(null);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(75)
							.addComponent(background)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cancelDownload))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(nowDownloading, GroupLayout.PREFERRED_SIZE, 335, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(fileName))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(nowDownloading, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(fileName)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(background)
						.addComponent(cancelDownload))
					.addContainerGap(37, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		activateListeners();
		new Thread(iad).start();
	}
	
	private void activateListeners(){
		background.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}	
		});
		cancelDownload.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?\nAny unfinished downloads will cancel.", "", JOptionPane.YES_NO_OPTION);
				if(confirmed == JOptionPane.YES_OPTION){
					iad.continueDownload = false;
				}
			}	
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?\nAny unfinished downloads will cancel.", "", JOptionPane.YES_NO_OPTION);
			    if (confirmed == JOptionPane.YES_OPTION) {
			    	iad.continueDownload = false;
			    }
			}
		});
	}
	
	public void setFile(String s){
		fileName.setText(s);
	}
	
	public void setFileCount(int i){
		progressBar.setMinimum(0);
		progressBar.setMaximum(i);
	}
	
	public void incrementProgress(int i){
		progressBar.setValue(i);
	}
}
