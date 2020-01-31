package io.AutoInventory;


import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import io.AutoInventory.Commands.AutoInvCommand;
import io.AutoInventory.Commands.AutoInventoryCommand;
import io.AutoInventory.Commands.DropWhenFullCommand;

public class AutoInventory extends PluginBase {
	private EventListener autoI;
	public Config configServer;
	public Config configPlayers;
	
	public static AutoInventory plugin;

	
    @Override
    public void onLoad() {
        this.getLogger().info(TextFormat.WHITE + "AutoInventory has been loaded!");

    }

    @Override
    public void onEnable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "AutoInventory has been enabled!");
        plugin = this;
        this.getServer().getCommandMap().register("AutoInv", (Command)new AutoInvCommand("autoInv", this));
        this.getServer().getCommandMap().register("AutoInventory", (Command)new AutoInventoryCommand("autoInventory", this));
        this.getServer().getCommandMap().register("DropWhenFull", (Command)new DropWhenFullCommand("dropWhenFull", this));

        configServer = new Config(this.getDataFolder() + "/config.yml", Config.YAML);
        configPlayers = new Config(this.getDataFolder() + "/playerPreference.yml", Config.YAML);
        
        if(!configServer.isBoolean("DropWhenFull"))
        {
        	configServer.set("DropWhenFull", true);
        	configServer.set("CanChoose", true);
        	configServer.set("All", true);
        	configServer.save();
        }

        //Register the EventListener
    	PluginManager pm = this.getServer().getPluginManager();
    	
        autoI = new EventListener(this, configServer, configPlayers, pm);
        this.getServer().getPluginManager().registerEvents(autoI, this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
    {
    	if (cmd.getName().equalsIgnoreCase("dropwhenfull")) {
			boolean dropWhenInventoryFull = configServer.getBoolean("DropWhenFull", true);
			configServer.set("DropWhenFull", !dropWhenInventoryFull);
			dropWhenInventoryFull = configServer.getBoolean("DropWhenFull");

			String OnOff = dropWhenInventoryFull ? "dropped" : "destroyed";
			sender.sendMessage("Items are "+OnOff+" when the player's inventory is full and AutoInv is on.");
    		configServer.save();
    		configServer.reload();
    		return true;
    	} else if(cmd.getName().equalsIgnoreCase("autoinventory")&& sender.hasPermission(Permission.DEFAULT_OP))
    	{
    		if(args.length==0) return false;
    		if(args[0].toLowerCase().contains("canchoose"))
    		{
    			boolean canChoose = configServer.getBoolean("CanChoose", true);
    			configServer.set("CanChoose", !canChoose);
    			canChoose = configServer.getBoolean("CanChoose");

    			String negation = canChoose ? "" : "not ";
    			sender.sendMessage("Players can "+negation+"choose to toggle now.");
    		}else if(args[0].toLowerCase().equals("all"))
			{
    			boolean all = configServer.getBoolean("All", true);
    			configServer.set("All", !all);
    			all = configServer.getBoolean("All");
    			String OnOff = all ? "on" : "off";
    			sender.sendMessage("AutoInventory is now "+OnOff+" for everyone.");
			}else {
				sender.sendMessage("Usage: /AutoInventory CanChoose");
				sender.sendMessage("Usage: /AutoInventory All");
				return false;
			}
    		configServer.save();
    		configServer.reload();
    		return true;
    	} else if(cmd.getName().equalsIgnoreCase("autoinv"))
    	{
			boolean autoInvPlayer = configPlayers.getBoolean(sender.getName(), true);
			configPlayers.set(sender.getName(), !autoInvPlayer);
			autoInvPlayer = configPlayers.getBoolean(sender.getName());
			String OnOff = autoInvPlayer ? "on" : "off";
			sender.sendMessage("You have turned AutoInventory "+OnOff+".");
			configPlayers.save();
    		configPlayers.reload();
			return true;
    	}
    	return false; 
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.DARK_RED + "AutoInventory has been disabled!");
        configServer.save();
        configPlayers.save();
        plugin = null;
        this.getServer().getCommandMap().getCommands().remove("AutoInv");
        this.getServer().getCommandMap().getCommands().remove("AutoInventory");
        this.getServer().getCommandMap().getCommands().remove("DropWhenFull");

    }



}
