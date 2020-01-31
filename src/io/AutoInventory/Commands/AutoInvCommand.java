package io.AutoInventory.Commands;

import cn.nukkit.command.PluginCommand;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.Plugin;
import io.AutoInventory.AutoInventory;

public class AutoInvCommand extends PluginCommand<AutoInventory>
{

	public AutoInvCommand(String name, Plugin owner) {
		super("AutoInv", AutoInventory.plugin);
        this.setPermission(Permission.DEFAULT_NOT_OP);
        this.commandParameters.clear();
        this.setUsage("/autoInv");
        this.setDescription("Allow players to toggle AutoInventory for themselves.");
	}
	
	
}