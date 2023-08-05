package dev.gether.getsprawdzanie.listener;

import dev.gether.getsprawdzanie.GetSprawdzanie;
import dev.gether.getsprawdzanie.manager.SprawdzManager;
import dev.gether.getsprawdzanie.model.Sprawdz;
import dev.gether.getsprawdzanie.utils.ColorFixer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Optional;

public class CommandPreListener implements Listener {

    private final GetSprawdzanie plugin;
    public CommandPreListener(GetSprawdzanie plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onCommandPre(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        SprawdzManager sprawdzManager = plugin.getSprawdzManager();
        Optional<Sprawdz> admitUser = sprawdzManager.getAdmitUser(player);
        if(!admitUser.isPresent())
            return;

        String message = event.getMessage();
        if(!sprawdzManager.isWhiteListCMD(message))
        {
            event.setCancelled(true);
            player.sendMessage(ColorFixer.addColors(plugin.getConfig().getString("lang.cannot-use-cmd")));
        }
    }
}
