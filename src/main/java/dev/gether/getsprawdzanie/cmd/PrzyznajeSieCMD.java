package dev.gether.getsprawdzanie.cmd;

import dev.gether.getsprawdzanie.GetSprawdzanie;
import dev.gether.getsprawdzanie.manager.SprawdzManager;
import dev.gether.getsprawdzanie.model.Sprawdz;
import dev.gether.getsprawdzanie.utils.ColorFixer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PrzyznajeSieCMD implements CommandExecutor {

    private final GetSprawdzanie plugin;

    public PrzyznajeSieCMD(GetSprawdzanie plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("przyznajesie").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return false;

        Player player = (Player) sender;
        Optional<Sprawdz> first = plugin.getSprawdzManager().getAdmitUser(player);
        if(!first.isPresent())
        {
            player.sendMessage(ColorFixer.addColors(plugin.getConfig().getString("lang.no-admit")));
            return false;
        }
        Sprawdz sprawdz = first.get();
        plugin.getSprawdzManager().przyznajesie(sprawdz);
        return false;
    }
}
