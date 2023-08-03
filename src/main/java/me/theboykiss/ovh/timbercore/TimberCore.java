package me.theboykiss.ovh.timbercore;

import org.bukkit.plugin.java.JavaPlugin;

public final class TimberCore extends JavaPlugin {

    @Override
    public void onEnable() {
        TreeFellerCommand treeFellerCommand = new TreeFellerCommand();
        getCommand("autofell").setExecutor(treeFellerCommand);
        getServer().getPluginManager().registerEvents(new TreeFeller(this, treeFellerCommand), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
