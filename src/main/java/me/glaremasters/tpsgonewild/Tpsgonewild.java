package me.glaremasters.tpsgonewild;

import co.aikar.commands.PaperCommandManager;
import me.glaremasters.tpsgonewild.commands.CommandTP;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tpsgonewild extends JavaPlugin {

    private PaperCommandManager manager;
    private final LocationUtil locationUtil = new LocationUtil();

    @Override
    public void onEnable() {
        manager = new PaperCommandManager(this);
        manager.registerCommand(new CommandTP());
    }

    @Override
    public void onDisable() {}

    public PaperCommandManager getManager() {
        return manager;
    }

    public LocationUtil getLocationUtil() {
        return locationUtil;
    }
}
