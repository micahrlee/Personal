package me.imguralbumdownloader.com;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.swing.JOptionPane;

import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

public class ImgurAlbumDownloader implements Runnable {
	public static final String gifs = "//i\\.imgur\\.com/.*\\.gif";
	public static final String pngs = "//i\\.imgur\\.com/.*\\.png";
	public static final String jpgs = "//i\\.imgur\\.com/.*\\.jpg";
	public static final String webm = "//i\\.imgur\\.com/.*\\.(webm|mp4)";
	public static final String all = "//i\\.imgur\\.com/.*\\..*";
	public boolean continueDownload = true;
	private final String albumURL;
	private final String path;
	private final String matchingPattern;
	private final DownloadingWindow window;
	private Parser parser;
	private final ArrayList<String> imgSrcs;
	private String albumTitle;
	
	
	public ImgurAlbumDownloader(String url, final String pth, final String pattern, final DownloadingWindow w) {
		albumURL = url;
		imgSrcs = new ArrayList<String>();
		NodeList title = null;
		parser = new Parser();
		try {
			parser.setURL(albumURL);
			title = parser.parse(new TagNameFilter("title"));
		} catch (ParserException e1) {
			JOptionPane.showMessageDialog(null, "Invalid connection. Check the URL, or your internet connection.", "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		parser.reset();
		albumTitle = title.remove(0).toPlainTextString().trim();
		this.path = pth + albumTitle + "\\";
		File testFile = new File(path.substring(0, path.length() -1));
		if(!testFile.exists()){
			testFile.mkdir();
		}
		matchingPattern = pattern;
		window = w;
	}
	

	private void downloadImages(){
		if(imgSrcs.isEmpty()){
			JOptionPane.showMessageDialog(null, "No images were found." , "", JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			window.setFileCount(imgSrcs.size());
			for (int i = 0; i < imgSrcs.size(); i++) {
				window.incrementProgress(i);
				if(continueDownload){
					URL img = null;
					try {
						img = new URL("http://" + imgSrcs.get(i));
					} catch (MalformedURLException e1) {
						JOptionPane.showMessageDialog(null, "Invalid connection. Check the URL, or your internet connection.", "Error", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
						return;
					}
					String fileName = imgSrcs.get(i).split("i\\.imgur\\.com/")[1];
					window.setFile(fileName);
					byte[] b = new byte[4096];
					int length;
					try {
						InputStream is = img.openStream();
						FileOutputStream os = new FileOutputStream(path + fileName);
						while ((length = is.read(b)) != -1) {
							os.write(b, 0, length);
						}
						is.close();
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					break;
				}
			}
		}
	}
	
	private void getImages(){
		if(!matchingPattern.equals(webm)){
			NodeList imgs = null;
			try {
				parser.setURL(albumURL);
				imgs = parser.parse(new TagNameFilter("a"));
			} catch (ParserException e1) {
				JOptionPane.showMessageDialog(null, "Invalid connection. Check the URL, or your internet connection.", "Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				return;
			}
			parser.reset();
			for (SimpleNodeIterator i = imgs.elements(); i.hasMoreNodes();) {
				Tag tag = (Tag) i.nextNode();
				if (tag.getAttribute("href") != null) {
					String src = tag.getAttribute("href").trim();
					Pattern p = Pattern.compile(matchingPattern);
					Matcher m = p.matcher(src);
					if (m.matches()) {
						imgSrcs.add(src.split("//")[1].trim());
					}
				}
			}
		}
		
		if(matchingPattern.equals(all) || matchingPattern.equals(webm)){
			NodeList webm = null;
			try {
				parser.setURL(albumURL);
				webm = parser.parse(new TagNameFilter("source"));
			} catch (ParserException e1) {
				JOptionPane.showMessageDialog(null, "Invalid connection. Check the URL, or your internet connection.", "Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				return;
			}
			parser.reset();
			for(SimpleNodeIterator i = webm.elements(); i.hasMoreNodes();){
				Tag tag = (Tag) i.nextNode();
				if(tag.getAttribute("src") != null && (tag.getAttribute("type").equals("video/webm") || tag.getAttribute("type").equals("video/mp4"))){
					String src = tag.getAttribute("src").trim();
					Pattern p = Pattern.compile(matchingPattern);
					Matcher m = p.matcher(src);
					if(m.matches()){
						imgSrcs.add(src.split("//")[1].trim());
					}
				}
			}
		}
	}

	public void run() {
		getImages();
		downloadImages();
		window.setVisible(false);
		window.dispose();
	}

//	public static void main(String[] args) {
//		while(true){	
//			Scanner in = new Scanner(System.in);
//			System.out.print("Please enter your imgur URL or quit to exit: ");
//			String url = in.nextLine().trim();
//			if(url.toLowerCase().equals("quit")){
//				System.out.println("Thanks for using ImgurAlbum Downloader!");
//				break;
//			}
//			System.out.print("Do you want to make a new directory? (y | n): ");
//			String decision;
//			String path;
//			while (true) {
//				decision = in.nextLine();
//				if (decision.equals("y")) {
//					System.out.println("Enter the path to where you want your directory:");
//					String pt = in.nextLine().trim();
//					System.out.print("Enter your directory name: ");
//					String dirName;
//					while (true) {
//						dirName = in.nextLine();
//						Pattern p = Pattern.compile("/|\\?|<|>|\\|\\||\"|:|\\*");
//						Matcher m = p.matcher(dirName);
//						if (!m.matches()) {
//							break;
//						}
//					}
//					if(!pt.endsWith("\\")){
//						pt += "\\";
//					}
//					new File(pt + dirName).mkdirs();
//					path = pt + dirName;
//					break;
//				}
//				else if(decision.equals("n")){
//					System.out.println("Please enter the path where you want to save your images:");
//					path = in.nextLine().trim();
//					break;
//				}
//			}
//			if(!path.endsWith("\\")){
//				path += "\\";
//			}
//			ImgurAlbum a = new ImgurAlbum(url, path);
//			try {
//				a.getImages();
//			} catch (ParserException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
