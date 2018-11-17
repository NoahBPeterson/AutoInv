package io.AutoInventory;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.TextFormat;

/**
 * author: MagicDroidX
 * NukkitExamplePlugin Project
 */
public class AutoInventory extends PluginBase {
	private boolean dropwhenfull = false;
	private EventListener autoI;

	
    @Override
    public void onLoad() {
        this.getLogger().info(TextFormat.WHITE + "AutoInventory has been loaded!");
    }

    @Override
    public void onEnable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "AutoInventory has been enabled!");


        //Register the EventListener
    	PluginManager pm = this.getServer().getPluginManager();
    	
        autoI = new EventListener(this, dropwhenfull, pm);
        this.getServer().getPluginManager().registerEvents(autoI, this);
    }
    

    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    	if (cmd.getName().equalsIgnoreCase("dropwhenfull")) {
    		if(dropwhenfull)
    		{
    			 autoI.setDropWhenFull(false);
    		}else {
   			 autoI.setDropWhenFull(true);
    		}
    		return true;
    	} 
    	return false; 
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.DARK_RED + "AutoInventory has been disabled!");
    }



}