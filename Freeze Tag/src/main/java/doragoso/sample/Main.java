package doragoso.sample;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener{

public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        registerEvents(this, new ItemInfo(), new ItemEffect(), new CommandList(), new StageGimmick(), new GameSystem(), new MenuInfo());
        getCommand("sethp").setExecutor(new CommandList());
        getCommand("push").setExecutor(new CommandList());
        getCommand("speed").setExecutor(new CommandList());
        getCommand("saturation").setExecutor(new CommandList());
        getCommand("ft").setExecutor(new CommandList());
        System.out.println("プラグインが有効になったニャン");
    }

    @Override
    public void onDisable() {
        System.out.println("プラグインが無効になったニャン");
        plugin = null;
    }

    public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
