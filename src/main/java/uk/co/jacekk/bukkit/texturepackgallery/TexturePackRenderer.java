package uk.co.jacekk.bukkit.texturepackgallery;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class TexturePackRenderer extends MapRenderer {
	
	private TexturePack pack;
	
	public TexturePackRenderer(TexturePack pack){
		this.pack = pack;
	}
	
	@Override
	public void render(MapView map, MapCanvas canvas, Player player){
		try{
			BufferedImage srcImage = ImageIO.read(this.pack.getImageFile());
			BufferedImage destImage = new BufferedImage(128, 128, 2);
			
			Graphics2D graphics = destImage.createGraphics();
			
			graphics.drawImage(srcImage, 0, 0, 128, 128, null);
			graphics.finalize();
			graphics.dispose();
			
			destImage.flush();
			
			canvas.drawImage(0, 0, destImage);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
}
