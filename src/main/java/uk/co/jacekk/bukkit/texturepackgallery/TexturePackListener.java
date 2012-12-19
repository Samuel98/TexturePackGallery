package uk.co.jacekk.bukkit.texturepackgallery;

import net.milkbowl.vault.economy.EconomyResponse;
import net.minecraft.server.v1_4_5.EntityItemFrame;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_4_5.CraftWorld;
import org.bukkit.craftbukkit.v1_4_5.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.map.MapView;
import org.bukkit.material.Directional;

import uk.co.jacekk.bukkit.baseplugin.v6.event.BaseListener;

public class TexturePackListener extends BaseListener<TexturePackGallery> {
	
	public TexturePackListener(TexturePackGallery plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (plugin.selectedPacks.contains(playerName)){
			String name = plugin.selectedPacks.getData(playerName);
			
			if (!plugin.packs.containsKey(name)){
				player.sendMessage(ChatColor.RED + name + " is no longer available.");
			}else{
				plugin.packs.get(name).offerToPlayer(player);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event){
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			
			if (block.getType() == Material.WALL_SIGN){
				Player player = event.getPlayer();
				String playerName = player.getName();
				Sign sign = (Sign) block.getState();
				
				String name = sign.getLine(1);
				String price = sign.getLine(2);
				
				if (plugin.packs.containsKey(name)){
					if (!Permission.SIGN_USE.has(player)){
						player.sendMessage(ChatColor.RED + "You do not have permission to use this sign");
						return;
					}
					
					if (plugin.selectedPacks.contains(playerName) && plugin.selectedPacks.getData(playerName).equals(name)){
						player.sendMessage(ChatColor.RED + "You already have this texture pack selected");
						return;
					}
					
					if (!price.isEmpty()){
						if (!Character.isDigit(price.substring(0, 1).toCharArray()[0])){
							price = price.substring(1);
						}
						
						try{
							double cost = Double.parseDouble(price);
							
							if (!plugin.econ.has(playerName, cost)){
								player.sendMessage(ChatColor.RED + "You can't afford this texture pack");
								return;
							}
							
							if (plugin.econ.withdrawPlayer(playerName, cost).type == EconomyResponse.ResponseType.FAILURE){
								player.sendMessage(ChatColor.RED + "Failed to subtract price");
								return;
							}
						}catch (NumberFormatException e){
							player.sendMessage(ChatColor.RED + "This texture pack's price is invalid");
							return;
						}
					}
					
					TexturePack pack = plugin.packs.get(name);
					
					plugin.selectedPacks.put(player.getName(), name);
					plugin.selectedPacks.save();
					
					pack.offerToPlayer(player);
					
					player.sendMessage(ChatColor.GREEN + "Text pack applied");
					
					event.setUseItemInHand(Result.DENY);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSignEdit(SignChangeEvent event){
		Block block = event.getBlock();
		
		if (block.getType() == Material.WALL_SIGN){
			Player player = event.getPlayer();
			World world = player.getWorld();
			Directional sign = (Directional) block.getState().getData();
			
			String name = event.getLine(1);
			String price = event.getLine(2);
			
			if (plugin.packs.containsKey(name)){
				if (!Permission.SIGN_CREATE.has(player)){
					player.sendMessage(ChatColor.RED + "You do not have permission to create texture pack signs.");
					
					event.setCancelled(true);
					event.getBlock().breakNaturally();
				}else{
					TexturePack pack = plugin.packs.get(name);
					MapView map;
					
					if (pack.getMapID() == -1){
						map = plugin.server.createMap(player.getWorld());
						
						pack.setMapID(map.getId());
						pack.setMapRenderer();
					}else{
						map = plugin.server.getMap(pack.getMapID());
					}
					
					CraftItemStack item = new CraftItemStack(Material.MAP, 1, map.getId());
					
					net.minecraft.server.v1_4_5.World mcWorld = ((CraftWorld)world).getHandle();
					
					BlockFace direction = sign.getFacing();
					Location frameLocation = block.getRelative(direction.getOppositeFace()).getRelative(BlockFace.UP).getLocation();
					
					int facing;
					
					switch (direction){
						case NORTH:
							facing = 1;
						break;
						case WEST:
							facing = 0;
						break;
						case SOUTH:
							facing = 3;
						break;
						default:
							facing = 2;
						break;
					}
					
					EntityItemFrame frame = new EntityItemFrame(mcWorld, frameLocation.getBlockX(), frameLocation.getBlockY(), frameLocation.getBlockZ(), facing);
					frame.a(item.getHandle());
					
					mcWorld.addEntity(frame);
					
					if (!price.isEmpty() && plugin.econ == null){
						player.sendMessage(ChatColor.RED + "No economy plugin was found.");
						event.setLine(2, "");
					}
					
					player.sendMessage(ChatColor.GREEN + "Text pack sign created");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Block block = event.getBlock();
		
		// TODO: More complete protection.
		
		if (block.getType() == Material.WALL_SIGN){
			Sign sign = (Sign) block.getState();
			Player player = event.getPlayer();
			
			if (plugin.packs.containsKey(sign.getLine(1)) && !Permission.SIGN_REMOVE.has(player)){
				player.sendMessage(ChatColor.RED + "You do not have permission to remove texture pack signs.");
				event.setCancelled(true);
			}
		}
	}
	
}
