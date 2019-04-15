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
	private Config configa;

	
    @Override
    public void onLoad() {
        this.getLogger().info(TextFormat.WHITE + "AutoInventory has been loaded!");

    }

    @Override
    public void onEnable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "AutoInventory has been enabled!");

        configa = new Config(this.getDataFolder() + "/config.yml", Config.YAML);

        if(!configa.isBoolean("DropWhenFull"))
        {
            configa.set("DropWhenFull", false);
            configa.save();
        }

        dropwhenfull=configa.getBoolean("DropWhenFull");
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
    			configa.set("DropWhenFull", false);
    			dropwhenfull=configa.getBoolean("DropWhenFull");
    			autoI.setDropWhenFull(false);
    		}else {
    			configa.set("DropWhenFull", true);
    			dropwhenfull=configa.getBoolean("DropWhenFull");
    			autoI.setDropWhenFull(true);
    		}
			configa.save();
    		return true;
    	} 
    	return false; 
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.DARK_RED + "AutoInventory has been disabled!");
        configa.save();
    }



}