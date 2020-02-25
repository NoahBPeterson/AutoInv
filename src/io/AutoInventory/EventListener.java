package io.AutoInventory;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;

public class EventListener implements Listener {
	Config serverConfig;
	Config playersConfig;

    public EventListener(AutoInventory plugin, Config server, Config players, PluginManager PM) {
    	serverConfig = server;
    	playersConfig = players;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) //Higher priority -> Runs later
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
                	Item[] dropsNull = {Item.get(0)};
					event.setDrops(dropsNull);
				}else if(!dropWhenFull()) //If the item cannot be added and the item should not drop, remove the block drop
				{
                	Item[] dropsNull = {Item.get(0)};
					event.setDrops(dropsNull);
				}
			}
    	}
    }
    
    boolean dropWhenFull() {
    	return serverConfig.getBoolean("DropWhenFull", true);
    }
    
    boolean canBreak(BlockBreakEvent event) {
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
    	
    	if(tool.isTool()) {
    		return true;
    	}
    	return false;
    }
}