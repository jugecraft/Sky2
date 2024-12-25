package com.sky.arenas;

import org.bukkit.Location;
import java.util.ArrayList;
import java.util.List;

public class Arena {
    private String name;
    private Location spawn1;
    private Location spawn2;
    private List<Location> bedrockLocations;

    public Arena(String name, Location spawn1, Location spawn2) {
        this.name = name;
        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
        this.bedrockLocations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public Location getSpawn2() {
        return spawn2;
    }

    public List<Location> getBedrockLocations() {
        return bedrockLocations;
    }

    public void addBedrockLocation(Location location) {
        bedrockLocations.add(location);
    }

    public void addChestLocation(Location location) {
    }
}