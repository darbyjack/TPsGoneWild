package me.glaremasters.tpsgonewild;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.concurrent.ThreadLocalRandom;

public class LocationUtil {
    public Location getRandomLocationInsideWorldBorder(World world) {
        final double randomX = ThreadLocalRandom.current().nextDouble(0, world.getWorldBorder().getSize() / 2);
        final double randomZ = ThreadLocalRandom.current().nextDouble(0, world.getWorldBorder().getSize() / 2);
        return world.getWorldBorder().getCenter().add(randomX, 0, randomZ);
    }
}
