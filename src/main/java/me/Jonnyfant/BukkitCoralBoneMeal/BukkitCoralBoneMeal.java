package me.Jonnyfant.BukkitCoralBoneMeal;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitCoralBoneMeal extends JavaPlugin {
    public final String CFG_NOT_FAN_CHANCE_KEY = "Chance for a normal coral (Not Fan)";
    private final double CFG_NOT_FAN_CHANCE_DEFAULT = 0.2;
    @Override
    public void onEnable() {
        initConfig();
        getServer().getPluginManager().registerEvents(new BlockBoneMealListener(this), this);
    }

    public void initConfig()
    {
        getConfig().addDefault(CFG_NOT_FAN_CHANCE_KEY, CFG_NOT_FAN_CHANCE_DEFAULT);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
