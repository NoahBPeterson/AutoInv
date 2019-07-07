package io.AutoInventory;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

/**
 * author: MagicDroidX
 * NukkitExamplePlugin Project
 */
public class AutoInventory extends PluginBase {
	private boolean dropwhenfull;
	private EventListener autoI;
	private Config configServer;
	private Config configPlayers;

	
    @Override
    public void onLoad() {
        this.getLogger().info(TextFormat.WHITE + "AutoInventory has been loaded!");

    }

    @Override
    public void onEnable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "AutoInventory has been enabled!");

        configServer = new Config(this.getDataFolder() + "/config.yml", Config.YAML);
        
        
        if(!configServer.isBoolean("DropWhenFull"))
        {
        	configServer.set("DropWhenFull", false);
        	configServer.save();
        }

        dropwhenfull=configServer.getBoolean("DropWhenFull");
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
    			configServer.set("DropWhenFull", false);
    			dropwhenfull=configServer.getBoolean("DropWhenFull");
    			autoI.setDropWhenFull(false);
    		}else {
    			configServer.set("DropWhenFull", true);
    			dropwhenfull=configServer.getBoolean("DropWhenFull");
    			autoI.setDropWhenFull(true);
    		}
    		configServer.save();
    		return true;
    	} 
    	return false; 
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.DARK_RED + "AutoInventory has been disabled!");
        configServer.save();
        configPlayers.save();
    }



}