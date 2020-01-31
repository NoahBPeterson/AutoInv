package io.AutoInventory.Commands;

import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.Plugin;
import io.AutoInventory.AutoInventory;

public class AutoInventoryCommand extends PluginCommand<AutoInventory>
{
	

	public AutoInventoryCommand(String name, Plugin owner) {
		super("AutoInventory", AutoInventory.plugin);
        this.setPermission(Permission.DEFAULT_OP);
        this.commandParameters.clear();
        this.commandParameters.put("canchoose", new CommandParameter[]
        		{
                new CommandParameter("CanChoose", CommandParamType.COMMAND, false),

        });
        this.commandParameters.put("all", new CommandParameter[]
        		{
                new CommandParameter("all", CommandParamType.COMMAND, false),
        });
        this.setUsage("/autoInventory  [CanChoose/All]");
        this.setDescription("Choose if players can toggle AutoInv [CanChoose], and toggle it for the server [All].");
	}
	
}