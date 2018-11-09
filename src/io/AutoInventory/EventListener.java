package io.AutoInventory;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;

/**
 * author: MagicDroidX
 * NukkitExamplePlugin Project
 */
public class EventListener implements Listener {
    private final AutoInventory plugin;
    private boolean dropwhenfull;

    public EventListener(AutoInventory plugin, boolean bool) {
        this.plugin = plugin;
        dropwhenfull = bool;
    }

	public void setDropWhenFull(boolean setter)
	{
		dropwhenfull=setter;
	}
	
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false) //DON'T FORGET THE ANNOTATION @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
    	//Check if player used a tool.
    	Item tool = event.getItem();
  
    	
    	if(isTool(tool)) {
    		
        	PlayerInventory inventoryAutoAdd = event.getPlayer().getInventory();
        	Item[] itemsToAdd = event.getDrops();
        	inventoryAutoAdd.addItem(itemsToAdd);
    		
        	if(!dropwhenfull)
        	{
        		Item[] dropsNull = {new Item(0)};
        		event.setDrops(dropsNull);
        	}
    		//event.setCancelled(); //We cancel so the block isn't dropped.
    	}else {
    		//return; //Otherwise, we don't do anything.
    	}

    }
    
    public boolean isTool(Item tool) {
    	
    	if(tool.isAxe()) {
    		return true;
    	}else if(tool.isPickaxe()) {
    		return true;
    	}else if(tool.isShovel()) {
    		return true;
    	}else {
    	return false;
    	}
    }
}