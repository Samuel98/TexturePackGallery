package uk.co.jacekk.bukkit.texturepackgallery;

import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.baseplugin.v9.permissions.PluginPermission;

public class Permission {
	
	public static final PluginPermission SIGN_CREATE		= new PluginPermission("texturepackgallery.sign.create",	PermissionDefault.OP,	"Allows the player to create gallery signs");
	public static final PluginPermission SIGN_REMOVE		= new PluginPermission("texturepackgallery.sign.remove",	PermissionDefault.OP,	"Allows the player to remove gallery signs");
	public static final PluginPermission SIGN_USE			= new PluginPermission("texturepackgallery.sign.use",		PermissionDefault.TRUE,	"Allows the player to use gallery signs");
	
	public static final PluginPermission PACK_SET			= new PluginPermission("texturepackgallery.pack.set",		PermissionDefault.OP, 	"Allows the player to add texture packs");
	public static final PluginPermission PACK_REMOVE		= new PluginPermission("texturepackgallery.pack.remove",	PermissionDefault.OP, 	"Allows the player to remove texture packs");
	public static final PluginPermission PACK_LIST		= new PluginPermission("texturepackgallery.pack.list",		PermissionDefault.OP,	"Allows the player to list all texture packs");
	public static final PluginPermission PACK_INFO		= new PluginPermission("texturepackgallery.pack.info",	 	PermissionDefault.OP,	"Allows the player to view info on a texture pack");
	
}
