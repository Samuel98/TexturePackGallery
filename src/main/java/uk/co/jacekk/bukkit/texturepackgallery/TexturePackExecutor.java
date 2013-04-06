package uk.co.jacekk.bukkit.texturepackgallery;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.baseplugin.v9_1.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v9_1.command.CommandHandler;

public class TexturePackExecutor extends BaseCommandExecutor<TexturePackGallery> {
	
	public TexturePackExecutor(TexturePackGallery plugin){
		super(plugin);
	}
	
	@CommandHandler(names = {"texturepackgallery", "texture", "tpg"}, description = "Add, remove or list texture packs", usage = "[options] [args]")
	public void texturePackGallery(CommandSender sender, String label, String[] args){
		if (args.length < 1 || args.length > 3){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " [option] <args>");
			sender.sendMessage(ChatColor.RED + "Options:");
			sender.sendMessage(ChatColor.RED + "  list - List all texture packs");
			sender.sendMessage(ChatColor.RED + "  set - Sets the link for a texture pack");
			sender.sendMessage(ChatColor.RED + "  remove - Remove a texture pack");
			sender.sendMessage(ChatColor.RED + "  info - Show info on a texture pack");
			
			return;
		}
		
		String option = args[0].toLowerCase();
		
		if (option.equals("list")){
			if (!Permission.PACK_LIST.has(sender)){
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return;
			}
			
			sender.sendMessage(ChatColor.GREEN + "Available texture packs:");
			
			for (String name : plugin.packs.keySet()){
				sender.sendMessage(ChatColor.GREEN + "  " + name);
			}
		}else if (option.equals("set")){
			if (!Permission.PACK_SET.has(sender)){
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return;
			}
			
			if (args.length != 3){
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + " set [name] [url]");
				return;
			}
			
			TexturePack pack = new TexturePack(args[1], args[2]);
			
			pack.fetchImage();
			
			plugin.packs.put(args[1], pack);
			
			if (plugin.packs.containsKey(args[1])){
				sender.sendMessage(ChatColor.GREEN + "Texture pack updated \\o/");
			}else{
				sender.sendMessage(ChatColor.GREEN + "Texture pack added \\o/");
			}
		}else if (option.equals("remove")){
			if (!Permission.PACK_REMOVE.has(sender)){
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return;
			}
			
			if (args.length != 2){
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + " remove [name]");
				return;
			}
			
			if (!plugin.packs.containsKey(args[1])){
				sender.sendMessage(ChatColor.RED + "A texture pack with that name does not exist");
				return;
			}
			
			TexturePack pack = plugin.packs.get(args[1]);
			
			pack.getImageFile().delete();
			
			plugin.packs.remove(args[1]);
			
			sender.sendMessage(ChatColor.GREEN + "Texture pack remove \\o/");
		}else if (option.equals("info")){
			if (!Permission.PACK_INFO.has(sender)){
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return;
			}
			
			if (args.length != 2){
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + " info [name]");
				return;
			}
			
			if (!plugin.packs.containsKey(args[1])){
				sender.sendMessage(ChatColor.RED + "A texture pack with that name does not exist");
				return;
			}
			
			TexturePack pack = plugin.packs.get(args[1]);
			
			sender.sendMessage(ChatColor.GREEN + "Link: " + pack.getLink());
		}
	}
	
}
