package dev.gether.getsprawdzanie;

import dev.gether.getsprawdzanie.cmd.GetSprawdzCMD;
import dev.gether.getsprawdzanie.cmd.PrzyznajeSieCMD;
import dev.gether.getsprawdzanie.listener.CommandPreListener;
import dev.gether.getsprawdzanie.listener.PlayerQuitListener;
import dev.gether.getsprawdzanie.manager.SprawdzManager;
import dev.gether.getsprawdzanie.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class GetSprawdzanie extends JavaPlugin {


    private SprawdzManager sprawdzManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        /*
            MANAGER
         */
        sprawdzManager = new SprawdzManager(this);

        /*
            REGISTER CMD
         */
        new GetSprawdzCMD(this);
        new PrzyznajeSieCMD(this);

        /*
            REGISTER EVENTS
         */
        new CommandPreListener(this);
        new PlayerQuitListener(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
    }

    public void reloadPlugin(Player player)
    {
        reloadConfig();
        sprawdzManager = new SprawdzManager(this);
        player.sendMessage(ColorFixer.addColors("&aPomyslnie przeladowano config!"));
    }

    public SprawdzManager getSprawdzManager() {
        return sprawdzManager;
    }
}
