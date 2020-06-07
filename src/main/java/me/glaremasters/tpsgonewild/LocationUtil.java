package me.glaremasters.tpsgonewild;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class LocationUtil {
    public final Location getRandLocation(@NotNull final World world) {
        final double borderSize = world.getWorldBorder().getSize();
        final double randomX = ThreadLocalRandom.current().nextDouble(0, borderSize / 2);
        final double randomZ = ThreadLocalRandom.current().nextDouble(0, borderSize / 2);
        return world.getWorldBorder().getCenter().add(randomX, 0, randomZ);
    }
}
