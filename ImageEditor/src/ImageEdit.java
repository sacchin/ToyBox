import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class ImageEdit {
	static int w = 105;
	static int h = 99;
	public static void main(String[] args) throws Exception {
		BufferedImage img = ImageIO.read(new File("C:/Users/U-U/workspace/PockemonBattleTools/res/drawable-xxhdpi/all.png"));
		int count = 0;
		for (int i = 2; i < 21; i++) {
			for (int j = 10; j < 31; j++) {
				BufferedImage a = new BufferedImage(w, h,	BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = (Graphics2D) a.getGraphics();
				g2d.drawImage(img, 0, 0, w, h,  w * j, h * i, w * (j + 1), h * (i + 1), null);
				ImageIO.write(a, "png", new File(String.format("a%02d.png", count)));
				count++;
			}
		}
	}

}
