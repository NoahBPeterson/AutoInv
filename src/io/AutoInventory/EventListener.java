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
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.TextFormat;

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
        	boolean isFull = false;
        	Item[] itemsToAdd = event.getDrops();
        	
        	if(dropwhenfull)
        	{
                MainLogger.getLogger().error("dropwhenfull=true");
        	}else
        	{
                MainLogger.getLogger().error("dropwhenfull=false");

        	}
        	
        	Item[] itemsToDrop = new Item[itemsToAdd.length];
        	
        	boolean ResidenceCanBreak = true;

        	if(!event.isCancelled())
        	{
        		if(plugin!=null) //Residence plugin is used, otherwise don't call functions that require it
        		{
        			if(!ResidenceCanBreak(event)) //and resident has perms
        			{
        				ResidenceCanBreak  =  false;
        			}
        		}
        		if(ResidenceCanBreak)
        		{
    				int length = itemsToAdd.length;
    				Item cursor;
    				int itemDropCount = 0;
    				for(int i = 0; i < length; i++) //Iterate through every possible drop
    				{
        				isFull=!inventoryAutoAdd.canAddItem(itemsToAdd[i]);

        				if(isFull)
        				{
            				MainLogger.getLogger().error("isFull");

        				}else
        				{
            				MainLogger.getLogger().error("!isFull");
        				}
        				
        				if(!isFull) //If the item can be added, add it and remove the block drop.
        				{
                        	inventoryAutoAdd.addItem(itemsToAdd[i]);
                        	Item[] dropsNull = {new Item(0)};
        					event.setDrops(dropsNull);
        				}else if(isFull&&!dropwhenfull) //If the item cannot be added and the item should not drop, remove the block drop
        				{
        					Item[] dropsNull = {new Item(0)};
        					event.setDrops(dropsNull);
        				}
    				}
    			}	
        	}
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