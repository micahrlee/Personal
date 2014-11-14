import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.util.ParserException;

public class Downloader implements Runnable{
	private DownloadingWindow window;
	public static final String validURL = "(https?)\\:\\/\\/(www\\.)?(?:m\\.)?imgur\\.com/a/([a-zA-Z0-9]+)(#[0-9]+)?";
	
	public Downloader(DownloadingWindow w){
		window = w;
	}
	
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Pattern p = Pattern.compile(validURL);
		Matcher m = p.matcher(ImgurDownloadWindow.url);
		if (m.matches()) {
			ImgurAlbum i = new ImgurAlbum(ImgurDownloadWindow.url, ImgurDownloadWindow.downloadPath);
			try {
				i.getImages();
			} catch (ParserException e) {
				e.printStackTrace();
			}
		}
		window.setVisible(false);
		window.dispose();	
	}
}
