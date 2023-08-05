package dev.gether.getsprawdzanie.cmd;

import dev.gether.getsprawdzanie.GetSprawdzanie;
import dev.gether.getsprawdzanie.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GetSprawdzCMD implements CommandExecutor, TabCompleter {
    private final GetSprawdzanie plugin;
    public GetSprawdzCMD(GetSprawdzanie plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("getsprawdz").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return false;

        Player player = (Player) sender;
        if(!player.hasPermission("getsprawdz.admin"))
            return false;

        if(args.length==1)
        {
            if(args[0].equalsIgnoreCase("czysty"))
            {
                plugin.getSprawdzManager().czysty(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("ban"))
            {
                plugin.getSprawdzManager().ban(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("reload"))
            {
                plugin.reloadPlugin(player);
                return true;
            }
            if(!plugin.getSprawdzManager().isSetLocation())
            {
                player.sendMessage(ColorFixer.addColors("&cMusisz ustawic /sprawdz setlocation [spawn/sprawdz]"));
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target==null)
            {
                player.sendMessage(ColorFixer.addColors("&cPodany gracz nie jest online!"));
                return false;
            }
            if(target.getName().equalsIgnoreCase(player.getName()))
            {
                player.sendMessage(ColorFixer.addColors("&cNie mozesz sprawdzic samego siebie!"));
                return false;
            }
            plugin.getSprawdzManager().sprawdz(player, target);
            return true;

        }
        if(args.length==2)
        {
            if(args[0].equalsIgnoreCase("setlocation"))
            {
                if(args[1].equalsIgnoreCase("spawn"))
                {
                    plugin.getSprawdzManager().setSpawnLocation(player.getLocation());
                    player.sendMessage(ColorFixer.addColors("&aPomyslnie ustawiono lokalizacje spawnu!"));
                    return true;
                }
                if(args[1].equalsIgnoreCase("sprawdz"))
                {
                    plugin.getSprawdzManager().setTeleportUser(player.getLocation());
                    player.sendMessage(ColorFixer.addColors("&aPomyslnie ustawiono lokalizacje sprawdzania!"));
                    return true;
                }
                return false;
            }

        }

        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length==1)
        {
            return Arrays.asList("reload", "czysty", "setlocation", "ban");
        }
        if(args.length==2)
        {
            if(args[0].equalsIgnoreCase("setlocation"))
            {
                return Arrays.asList("spawn", "sprawdz");
            }
        }
        return null;
    }
}
