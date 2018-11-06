package io.AutoInventory;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

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
        autoI = new EventListener(this, dropwhenfull);
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
        this.getLogger().info(TextFormat.DARK_RED + "I've been disabled!");
    }



}