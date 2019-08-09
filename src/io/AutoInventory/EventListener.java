package io.AutoInventory;

import cn.nukkit.Player;

/*import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.*;
import com.massivecraft.factions.listeners.*;*/

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

public class EventListener implements Listener {
	private PluginManager pm;
	Config serverConfig;
	Config playersConfig;

    public EventListener(AutoInventory plugin, Config server, Config players, PluginManager PM) {
    	serverConfig = server;
    	playersConfig = players;
        pm = PM;
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false) //Higher priority -> Runs later
    public void onBlockBreak(BlockBreakEvent event) {
    	Item tool = event.getItem(); //Check if player used a tool.
    	boolean canBreak = canBreak(event);

    	if(isTool(tool) && canBreak)
    	{
        	boolean canAddItem = false;
        	
        	PlayerInventory inventoryAutoAdd = event.getPlayer().getInventory();
        	Item[] itemsToAdd = event.getDrops();
        	        	
			for(int i = 0; i < itemsToAdd.length; i++) //Iterate through every possible drop
			{
				canAddItem=inventoryAutoAdd.canAddItem(itemsToAdd[i]);
				
				if(canAddItem) //If the item can be added, add it and remove the block drop.
				{
                	inventoryAutoAdd.addItem(itemsToAdd[i]);
                	Item[] dropsNull = {new Item(0)};
					event.setDrops(dropsNull);
				}else if(!dropWhenFull()) //If the item cannot be added and the item should not drop, remove the block drop
				{
					Item[] dropsNull = {new Item(0)};
					event.setDrops(dropsNull);
				}
			}
    	}
    }
    
    boolean dropWhenFull()
    {
    	return serverConfig.getBoolean("DropWhenFull", true);
    }
    
    boolean canBreak(BlockBreakEvent event)
    {
    	boolean playerChoice = playersConfig.getBoolean(event.getPlayer().getName(), true);
    	boolean allChoice = serverConfig.getBoolean("All", true);
    	boolean canChoose = serverConfig.getBoolean("CanChoose", true);
    	if(!canChoose)
    	{
    		return allChoice;
    	}else
    	{
    		return playerChoice;
    	}
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