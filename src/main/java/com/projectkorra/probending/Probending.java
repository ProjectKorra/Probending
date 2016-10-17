package com.projectkorra.probending;


import com.projectkorra.projectkorra.storage.DBConnection;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class Probending extends JavaPlugin {

    public void onEnable() {
        try {
            new MetricsLite(this);
        } catch (IOException ex) {
            Logger.getLogger(Probending.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onDisable() {
        if (DBConnection.isOpen) {
            DBConnection.sql.close();
        }
    }
}
