package me.NerdsWBNerds.Empires;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by IntelliJ IDEA.
 * User: brenhein
 * Date: 4/5/12
 * Time: 9:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmpiresCommandExecutor implements CommandExecutor {
    private Empires plugin;

    public EmpiresCommandExecutor(Empires plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /**
         * READ THIS BIT: Return TRUE when this plugin handled the command
         *                Return FALSE when this plugin does NOT handle this command
         *                Rule of thumb: If the command is /empire, return TRUE no matter what!
         */
        return false;
    }
}
