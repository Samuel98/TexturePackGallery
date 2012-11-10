package uk.co.jacekk.bukkit.texturepackgallery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

import uk.co.jacekk.bukkit.baseplugin.v5.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.v5.storage.DataStore;

public class TexturePackGallery extends BasePlugin {
	
	public static TexturePackGallery instance;
	
	public File packDir;
	public File packsFile;
	
	public DataStore selectedPacks;
	public HashMap<String, TexturePack> packs;
	
	public Economy econ;
	
	@SuppressWarnings("unchecked")
	public void onEnable(){
		super.onEnable(true);
		
		instance = this;
		
		this.packDir = new File(this.baseDirPath + File.separator + "pack_cache");
		
		if (!this.packDir.exists()){
			this.packDir.mkdirs();
		}
		
		this.packsFile = new File(this.baseDirPath + File.separator + "texture_packs.bin");
		
		if (!this.packsFile.exists()){
			this.packs = new HashMap<String, TexturePack>();
		}else{
			try{
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(packsFile));
				
				this.packs = (HashMap<String, TexturePack>) input.readObject();
				
				input.close();
			}catch (Exception e){
				this.log.warn("Failed to load texture packs from file");
				e.printStackTrace();
			}
		}
		
		this.selectedPacks = new DataStore(new File(this.baseDirPath + File.separator + "selected-packs.txt"), true);
		this.selectedPacks.load();
		
		for (TexturePack pack : this.packs.values()){
			pack.fetchImage();
			pack.setMapRenderer();
		}
		
		if (this.pluginManager.isPluginEnabled("Vault")){
			RegisteredServiceProvider<Economy> economyProvider = this.server.getServicesManager().getRegistration(Economy.class);
			
			if (economyProvider != null){
				this.econ = economyProvider.getProvider();
			}
		}
		
		this.permissionManager.registerPermissions(Permission.class);
		this.commandManager.registerCommandExecutor(new TexturePackExecutor(this));
		this.pluginManager.registerEvents(new TexturePackListener(this), this);
	}
	
	public void onDisable(){
		this.selectedPacks.save();
		
		try{
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(this.packsFile));
			
			output.writeObject(this.packs);
			output.flush();
			
			output.close();
		}catch (Exception e){
			this.log.warn("Failed to save texture packs to file");
			e.printStackTrace();
		}
		
		instance = null;
	}
	
}
