package me.Jonnyfant.BukkitCoralBoneMeal;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitCoralBoneMeal extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BlockBoneMealListener(), this);
    }

}
