package com.projectkorra.probending;

import com.projectkorra.probending.database.DatabaseHandler;
import com.projectkorra.probending.managers.FieldCreationManager;
import com.projectkorra.probending.managers.ProbendingHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Probending extends JavaPlugin {

    private ProbendingHandler pHandler;
    private FieldCreationManager cManager;

    private Commands commands;

    public void onEnable() {
        try {
            new MetricsLite(this);
        } catch (IOException ex) {
            Logger.getLogger(Probending.class.getName()).log(Level.SEVERE, null, ex);
        }
        DatabaseHandler.init(this); //OPEN THE DATABASE! 
        
        pHandler = new ProbendingHandler(this);
        cManager = new FieldCreationManager(this, pHandler);
        this.getServer().getPluginManager().registerEvents(cManager, this);
        commands = new Commands(pHandler, cManager);
    }

    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("probending")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                commands.mainCommands(player, args);
                return true;
            }
        }
        return false;
    }
}
