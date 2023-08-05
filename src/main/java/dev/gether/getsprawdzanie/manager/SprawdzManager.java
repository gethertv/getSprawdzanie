package dev.gether.getsprawdzanie.manager;

import dev.gether.getsprawdzanie.GetSprawdzanie;
import dev.gether.getsprawdzanie.model.Sprawdz;
import dev.gether.getsprawdzanie.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class SprawdzManager {
    private final GetSprawdzanie plugin;

    private List<Sprawdz> listaSprawdzanych = new ArrayList<>();
    private Location teleportUser;
    private Location spawnLocation;

    private List<String> whiteListCMD = new ArrayList<>();
    private List<String> przyznajesieCmd = new ArrayList<>();
    private List<String> leaveCMD = new ArrayList<>();
    private List<String> banCMD = new ArrayList<>();
    public SprawdzManager(GetSprawdzanie plugin)
    {
        this.plugin = plugin;
        this.przyznajesieCmd.addAll(plugin.getConfig().getStringList("przyznajesie-cmd"));
        this.leaveCMD.addAll(plugin.getConfig().getStringList("leave-cmd"));
        this.banCMD.addAll(plugin.getConfig().getStringList("ban-cmd"));
        this.teleportUser = plugin.getConfig().getLocation("admit.teleport-loc");
        this.spawnLocation = plugin.getConfig().getLocation("admit.spawn");
        this.whiteListCMD.addAll(plugin.getConfig().getStringList("whitelist-cmd"));

    }

    public void sprawdz(Player admin, Player target)
    {
        listaSprawdzanych.add(new Sprawdz(admin, target, teleportUser));


        /*
            BROADCAST MESSAGE
         */
        List<String> broadcastList = plugin.getConfig().getStringList("lang.admit.broadcast");
        String broadcast = listToString(broadcastList);
        broadcast = broadcast.replace("{admin}", admin.getName())
                .replace("{target}", target.getName());
        Bukkit.broadcastMessage(ColorFixer.addColors(broadcast));
        /*
            TARGER MESSAGE INFO
         */
        List<String> stringList = plugin.getConfig().getStringList("lang.admit.target");
        String message = listToString(stringList);
        message = message.replace("{admin}", admin.getName());
        target.sendMessage(ColorFixer.addColors(message));
    }

    public void przyznajesie(Sprawdz sprawdz)
    {
        for (String cmd : przyznajesieCmd) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", sprawdz.getPlayerSprawdzany().getName()));
        }
        /*
            BROADCAST MESSAGE
         */
        List<String> stringList = plugin.getConfig().getStringList("lang.admit.accept");
        String message = listToString(stringList);
        message = message.replace("{target}", sprawdz.getPlayerSprawdzany().getName());
        Bukkit.broadcastMessage(ColorFixer.addColors(message));

        listaSprawdzanych.remove(sprawdz);
    }

    public void czysty(Player admin)
    {
        Optional<Sprawdz> adminCheck = getAdminCheck(admin);
        if(!adminCheck.isPresent())
        {
            admin.sendMessage(ColorFixer.addColors("&cAktualnie nikogo nie sprawdzasz!"));
            return;
        }
        Sprawdz sprawdz = adminCheck.get();
        /*
            BROADCAST MESSAGE
         */
        List<String> stringList = plugin.getConfig().getStringList("lang.admit.purge");
        String message = listToString(stringList);
        message = message.replace("{target}", sprawdz.getPlayerSprawdzany().getName());
        Bukkit.broadcastMessage(ColorFixer.addColors(message));

        sprawdz.getPlayerSprawdzany().teleport(spawnLocation);
        sprawdz.getAdmin().teleport(spawnLocation);
        listaSprawdzanych.remove(sprawdz);
    }

    public void banLeave(Sprawdz sprawdz) {
        for (String cmd : leaveCMD) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", sprawdz.getPlayerSprawdzany().getName()));
        }
        /*
            BROADCAST MESSAGE
         */
        List<String> stringList = plugin.getConfig().getStringList("lang.admit.leave");
        String message = listToString(stringList);
        message = message.replace("{target}", sprawdz.getPlayerSprawdzany().getName())
                .replace("{admin}", sprawdz.getAdmin().getName());

        Bukkit.broadcastMessage(ColorFixer.addColors(message));

        sprawdz.getAdmin().teleport(spawnLocation);
        listaSprawdzanych.remove(sprawdz);
    }

    public void ban(Player admin) {
        Optional<Sprawdz> adminCheck = getAdminCheck(admin);
        if(!adminCheck.isPresent())
        {
            admin.sendMessage(ColorFixer.addColors("&cAktualnie nikogo nie sprawdzasz!"));
            return;
        }
        Sprawdz sprawdz = adminCheck.get();
        for (String cmd : banCMD) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", sprawdz.getPlayerSprawdzany().getName()));
        }
        /*
            BROADCAST MESSAGE
         */
        List<String> stringList = plugin.getConfig().getStringList("lang.admit.ban");
        String message = listToString(stringList);
        message = message.replace("{target}", sprawdz.getPlayerSprawdzany().getName())
                .replace("{admin}", sprawdz.getAdmin().getName());

        Bukkit.broadcastMessage(ColorFixer.addColors(message));

        sprawdz.getAdmin().teleport(spawnLocation);
        listaSprawdzanych.remove(sprawdz);
    }
    public Optional<Sprawdz> getAdminCheck(Player admin)
    {
        return listaSprawdzanych.stream().filter(sprawdz -> sprawdz.getAdmin().getUniqueId().equals(admin.getUniqueId())).findFirst();
    }
    public Optional<Sprawdz> getAdmitUser(Player player)
    {
       return listaSprawdzanych.stream().filter(sprawdz -> sprawdz.getPlayerSprawdzany().getUniqueId().equals(player.getUniqueId())).findFirst();
    }

    private String listToString(List<String> broadcast)
    {

        StringBuilder sb = new StringBuilder();
        for (String s : broadcast) {
            sb.append(s);
            sb.append("\n");
        }

        return sb.toString();

    }

    public boolean isWhiteListCMD(String message) {
        for (String cmd : whiteListCMD) {
            String[] words = message.split(" ");
            if (words.length > 0) {
                String word = words[0];
                if(cmd.equalsIgnoreCase(word))
                    return true;
            }
        }
        return false;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
        plugin.getConfig().set("admit.spawn", spawnLocation);
        plugin.saveConfig();
    }

    public void setTeleportUser(Location teleportUser) {
        this.teleportUser = teleportUser;
        plugin.getConfig().set("admit.teleport-loc", teleportUser);
        plugin.saveConfig();
    }

    public boolean isSetLocation()
    {
        return teleportUser!=null && spawnLocation!=null;
    }


}
