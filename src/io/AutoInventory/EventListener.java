package io.AutoInventory;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginManager;

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
	
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false) //Set to lowest priority to work with SpawnProtect
    public void onBlockBreak(BlockBreakEvent event) {
    	//Check if player used a tool.
    	Item tool = event.getItem();
  
    	
    	if(isTool(tool)) {
    		
    		
        	Plugin plugin = pm.getPlugin("Residence");

        	PlayerInventory inventoryAutoAdd = event.getPlayer().getInventory();
        	Item[] itemsToAdd = event.getDrops();
        	if(!event.isCancelled())
        	{
        		if(plugin!=null) //Residence plugin is used, otherwise don't call functions that require it
        		{
        			if(ResidenceCanBreak(event)) //and resident has perms
        			{
                    	inventoryAutoAdd.addItem(itemsToAdd);
        			}
        		}else {
                	inventoryAutoAdd.addItem(itemsToAdd);
        		}
        		
        		
        	}

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
    	int fishingPole = Item.FISHING_ROD;
    	if(tool.isAxe()) {
    		return true;
    	}else if(tool.isPickaxe()) {
    		return true;
    	}else if(tool.isShovel()) {
    		return true;
    	}else if(tool.getId()==fishingPole){
    		return true;
    	}else {
    	return false;
    	}
    }
}