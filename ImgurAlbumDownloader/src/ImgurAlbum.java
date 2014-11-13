import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

public class ImgurAlbum {
	private String albumURL;
	private String path;
	public static final String validURL = "(https?)\\:\\/\\/(www\\.)?(?:m\\.)?imgur\\.com/a/([a-zA-Z0-9]+)(#[0-9]+)?";

	public ImgurAlbum(String url, String path) {
		albumURL = url;
		this.path = path;
	}

	private void downloadImages(String s) throws MalformedURLException {
		URL img = new URL("http://" + s);
		String fileName = s.split("i\\.imgur\\.com/")[1];
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

	public void getImages() throws ParserException {
		Parser parser = new Parser(albumURL);
		NodeList imgs = parser.parse(new TagNameFilter("img"));
		NodeList gifs = parser.parse(new TagNameFilter("source"));
		ArrayList<String> imgSrcs = new ArrayList<String>();
		
		for (SimpleNodeIterator i = imgs.elements(); i.hasMoreNodes();) {
			Tag tag = (Tag) i.nextNode();
			if (tag.getAttribute("src") != null) {
				String src = tag.getAttribute("src").trim();
				Pattern p = Pattern.compile("//i.imgur.com/.*\\.jpg");
				Matcher m = p.matcher(src);
				if (m.matches()) {
					imgSrcs.add(src.split("//")[1].trim());
				}
			}
		}
		
		for(SimpleNodeIterator i = gifs.elements(); i.hasMoreNodes();){
			Tag tag = (Tag) i.nextNode();
			if(tag.getAttribute("type").equals("video/mp4")){
				String src = tag.getAttribute("src").trim();
				Pattern p = Pattern.compile("//i.imgur.com/.*\\.mp4");
				Matcher m = p.matcher(src);
				if(m.matches()){
					imgSrcs.add(src.split("//")[1].trim());
				}
			}
		}

		for (String s : imgSrcs) {
			try {
				downloadImages(s);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
}
