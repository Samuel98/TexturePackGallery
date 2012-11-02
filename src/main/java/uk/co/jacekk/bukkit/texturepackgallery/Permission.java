package uk.co.jacekk.bukkit.texturepackgallery;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.baseplugin.v4.permissions.PluginPermission;

public enum Permission implements PluginPermission {
	
	SIGN_CREATE(	"texturepackgallery.sign.create",	PermissionDefault.OP,		"Allows the player to create gallery signs"),
	SIGN_REMOVE(	"texturepackgallery.sign.remove",	PermissionDefault.OP,		"Allows the player to remove gallery signs"),
	SIGN_USE(		"texturepackgallery.sign.use",		PermissionDefault.TRUE,		"Allows the player to use gallery signs"),
	
	PACK_SET(		"texturepackgallery.pack.set",		PermissionDefault.OP, 		"Allows the player to add texture packs"),
	PACK_REMOVE(	"texturepackgallery.pack.remove",	PermissionDefault.OP, 		"Allows the player to remove texture packs"),
	PACK_LIST(		"texturepackgallery.pack.list",		PermissionDefault.OP,		"Allows the player to list all texture packs"),
	PACK_INFO(		"texturepackgallery.pack.info", 	PermissionDefault.OP,		"Allows the player to view info on a texture pack");
	
	protected String node;
	protected PermissionDefault defaultValue;
	protected String description;
	
	private Permission(String node, PermissionDefault defaultValue, String description){
		this.node = node;
		this.defaultValue = defaultValue;
		this.description = description;
	}
	
	public boolean has(CommandSender sender){
		return sender.hasPermission(this.node);
	}
	
	public List<Player> getPlayersWith(){
		ArrayList<Player> players = new ArrayList<Player>();
		
		for (Player player : Bukkit.getServer().getOnlinePlayers()){
			if (this.hasPermission(player)){
				players.add(player);
			}
		}
		
		return players;
	}
	
	public Boolean hasPermission(CommandSender sender){
		return sender.hasPermission(this.node);
	}
	
	public String getNode(){
		return this.node;
	}
	
	public PermissionDefault getDefault(){
		return this.defaultValue;
	}
	
	public String getDescription(){
		return this.description;
	}
	
}
