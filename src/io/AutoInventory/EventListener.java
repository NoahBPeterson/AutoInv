package io.AutoInventory;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.*;
import com.massivecraft.factions.listeners.*;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;

/**
 * author: MagicDroidX
 * NukkitExamplePlugin Project
 */
public class EventListener implements Listener {
    private boolean dropwhenfull;
	private PluginManager pm;

    public EventListener(AutoInventory plugin, boolean bool, PluginManager PM) {
        dropwhenfull = bool;
        pm = PM;
    }

	public void setDropWhenFull(boolean setter)
	{
		dropwhenfull=setter;
	}	
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false) //Set to lowest priority to work with SpawnProtect
    public void onBlockBreak(BlockBreakEvent event) {
    	//Check if player used a tool.
    	Item tool = event.getItem();
  
    	if(isTool(tool))
    	{
        	PlayerInventory inventoryAutoAdd = event.getPlayer().getInventory();
        	boolean isFull = false;
        	Item[] itemsToAdd = event.getDrops();
        	        	
        	if(!event.isCancelled() && canBreak(event))
        	{
				int length = itemsToAdd.length;
				for(int i = 0; i < length; i++) //Iterate through every possible drop
				{
    				isFull=!inventoryAutoAdd.canAddItem(itemsToAdd[i]);
    				
    				if(!isFull) //If the item can be added, add it and remove the block drop.
    				{
                    	inventoryAutoAdd.addItem(itemsToAdd[i]);
                    	//event.setCancelled();
                    	Item[] dropsNull = {new Item(0)};
    					event.setDrops(dropsNull);
    				}else if(isFull&&!dropwhenfull) //If the item cannot be added and the item should not drop, remove the block drop
    				{
    					//event.setCancelled();
    					Item[] dropsNull = {new Item(0)};
    					event.setDrops(dropsNull);
    				}
				}
        	}
    	}
    }
    
    boolean canBreak(BlockBreakEvent event)
    {
    	Plugin pluginResidence = pm.getPlugin("Residence");
    	Plugin pluginProtectedWorlds = pm.getPlugin("ProtectedWorlds");
    	Plugin pluginFactions = pm.getPlugin("Factions");
    	
    	boolean residenceCanBreak = true;
    	boolean protectedCanBreak = true;
    	boolean factionsCanBreak = true; 
    	
		if(pluginResidence!=null) //Residence plugin is used, otherwise don't call functions that require it
		{
			if(!ResidenceCanBreak(event)) //and resident has perms
			{
				residenceCanBreak = false;
			}
		}
		if(pluginProtectedWorlds!=null)
		{
			if(!ProtectedWorldsCanBreak(event))
			{
				protectedCanBreak = false;
			}
		}
		if(pluginFactions!=null)
		{
			if(!FactionsCanBreak(event))
			{
				factionsCanBreak=false;
			}
		}
		
		return (residenceCanBreak && protectedCanBreak && factionsCanBreak);
    }
    
    private boolean ResidenceCanBreak(BlockBreakEvent event)
    {
    	Location loc = event.getBlock().getLocation();
    	ClaimedResidence res = Residence.getResidenceManager().getByLoc(loc);
    	if(res==null)
    	{
    		return true;
    	}
    	ResidencePermissions perms = res.getPermissions();
    	String playerName = event.getPlayer().getName();
    	
    	boolean hasPermission = perms.playerHas(playerName, "build", true);


    	if(hasPermission)
    	{
    		return true;
    	}
    	return false;
    }
    
    private boolean ProtectedWorldsCanBreak(BlockBreakEvent event)
    {
    	Config config;
    	config = pm.getPlugin("ProtectedWorlds").getConfig();
    			
    	
    	if (config.getStringList("worlds").contains(event.getBlock().getLevel().getName()) //This code snippet taken directly from PeterIM's plugin ProtectedWorlds
    			&& !event.getPlayer().hasPermission("protectedworlds.bypass." + event.getPlayer().getLevel().getName().toLowerCase()) 
    			&& !event.getPlayer().hasPermission("protectedworlds.bypassall") 
    			&& config.getBoolean("noBlockBreaking"))
    	{
    		return false;
    	}
    	
    	return true;
    }
    
    private boolean FactionsCanBreak(BlockBreakEvent event)
    {    	
    	Location location = new Location(event.getBlock().getFloorX(), event.getBlock().getFloorY(), event.getBlock().getFloorZ(), 0, 0, event.getBlock().getLevel());    	
    	boolean canBreak =  FactionsBlockListener.playerCanBuildDestroyBlock(event.getPlayer(), location, "destroy", true);
    	return canBreak;
    }
    

    public boolean isTool(Item tool) {
    	if(tool.isAxe()) {
    		return true;
    	}else if(tool.isPickaxe()) {
    		return true;
    	}else if(tool.isShovel()) {
    		return true;
    	}else if(tool.getId()==Item.FISHING_ROD){
    		return true;
    	}else {
    	return false;
    	}
    }
}