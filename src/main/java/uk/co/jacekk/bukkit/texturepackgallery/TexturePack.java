package uk.co.jacekk.bukkit.texturepackgallery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class TexturePack implements Serializable {
	
	private static final long serialVersionUID = 3178558289128834462L;
	
	private String name;
	private String link;
	
	private short mapID;
	
	private File imageFile;
	private long lastImageCache;
	
	public TexturePack(String name, String link){
		this.name = name;
		this.link = link;
		
		this.mapID = -1;
		
		this.imageFile = new File(TexturePackGallery.getInstance().packDir.getAbsolutePath() + File.separator + name + ".png");
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getLink(){
		return this.link;
	}
	
	public short getMapID(){
		return this.mapID;
	}
	
	public void setMapID(short mapID){
		this.mapID = mapID;
	}
	
	public File getImageFile(){
		return this.imageFile;
	}
	
	public TexturePackRenderer getMapRenderer(){
		return new TexturePackRenderer(this);
	}
	
	public void fetchImage(){
		try{
			URLConnection connection = (new URL(this.link)).openConnection();
			
			connection.setReadTimeout(8000);
			connection.setConnectTimeout(4000);
			
			String type = connection.getContentType();
			long lastMod = connection.getLastModified();
			
			if (lastMod != this.lastImageCache){
				if (type != null && !type.equalsIgnoreCase("application/zip")){
					TexturePackGallery.getInstance().log.warn("The URL for " + this.name + " is not to a .zip file, will try anyway :s");
				}
				
				ZipInputStream input = new ZipInputStream(connection.getInputStream());
				
				ZipEntry entry = input.getNextEntry();
				
				while (entry != null && !entry.getName().equals("pack.png")){
					entry = input.getNextEntry();
				}
				
				if (entry == null){
					TexturePackGallery.getInstance().log.warn("A pack.png image for " + this.name + " could not be found");
				}else{
					TexturePackGallery.getInstance().log.warn("Downloading pack.png for " + this.name);
					
					FileOutputStream output = new FileOutputStream(this.imageFile);
					
					byte[] data = new byte[4096];
					int read;
					
					while ((read = input.read(data, 0, 4096)) != -1){
						output.write(data, 0, read);
					}
					
					output.flush();
					output.close();
					
					this.lastImageCache = lastMod;
				}
				
				input.close();
			}
		}catch (MalformedURLException e){
			TexturePackGallery.getInstance().log.warn("The URL for " + this.name + " is not valid");
		}catch (IOException e){
			TexturePackGallery.getInstance().log.warn("Unable to download the pack.png image for " + this.name);
			e.printStackTrace();
		}
	}
	
	public void setMapRenderer(){
		if (this.mapID != -1){
			MapView map = Bukkit.getMap(this.mapID);
			
			for (MapRenderer renderer : map.getRenderers()){
				map.removeRenderer(renderer);
			}
			
			map.addRenderer(this.getMapRenderer());
		}
	}
	
	public void offerToPlayer(Player player){
		player.setTexturePack(this.link);
	}
	
}
