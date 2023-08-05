package dev.gether.getsprawdzanie.model;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Sprawdz {
    private Player admin;
    private Player playerSprawdzany;

    public Sprawdz(Player admin, Player playerSprawdzany, Location location) {
        this.admin = admin;
        this.playerSprawdzany = playerSprawdzany;
        playerSprawdzany.teleport(location);
        admin.teleport(location);
    }

    public Player getAdmin() {
        return admin;
    }

    public Player getPlayerSprawdzany() {
        return playerSprawdzany;
    }
}
