package io.AutoInventory.Commands;

import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.Plugin;
import io.AutoInventory.AutoInventory;

public class DropWhenFullCommand extends PluginCommand<AutoInventory>
{

	public DropWhenFullCommand(String name, Plugin owner) {
		super("DropWhenFull", AutoInventory.plugin);
        this.setPermission(Permission.DEFAULT_OP);
        this.commandParameters.clear();
        this.commandParameters.put("dropwhenfull", new CommandParameter[]
        		{
                new CommandParameter("DropWhenFull", CommandParamType.COMMAND, false)

        });
        this.setUsage("/DropWhenFull  [total/blockName] [broken/placed]");
        this.setDescription("When a player's inventory is full, do mined blocks drop or destroy? Default is to drop.");
	}
}