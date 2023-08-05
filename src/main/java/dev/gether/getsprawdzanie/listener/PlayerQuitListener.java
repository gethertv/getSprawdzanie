package dev.gether.getsprawdzanie.listener;

import dev.gether.getsprawdzanie.GetSprawdzanie;
import dev.gether.getsprawdzanie.model.Sprawdz;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayerQuitListener implements Listener {
    private final GetSprawdzanie plugin;

    public PlayerQuitListener(GetSprawdzanie plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuitPlayer(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        Optional<Sprawdz> admitUser = plugin.getSprawdzManager().getAdmitUser(player);
        if(admitUser.isPresent())
        {
            plugin.getSprawdzManager().banLeave(admitUser.get());
        }
    }
}
