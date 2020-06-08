package me.glaremasters.tpsgonewild.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Modified from https://github.com/EssentialsX/Essentials/blob/2.x/Essentials/src/com/earth2me/essentials/utils/LocationUtil.java
 */
public class LocationUtil {
    // Water types used for TRANSPARENT_MATERIALS and is-water-safe config option
    private static final Set<Material> WATER_TYPES = EnumUtil.getAllMatching(Material.class, "WATER", "FLOWING_WATER");

    // The player can stand inside these materials
    private static final Set<Material> HOLLOW_MATERIALS = new HashSet<>();
    private static final Set<Material> TRANSPARENT_MATERIALS = new HashSet<>();

    static {
        // Materials from Material.isTransparent()
        for (Material mat : Material.values()) {
            if (mat.isTransparent()) {
                HOLLOW_MATERIALS.add(mat);
            }
        }

        TRANSPARENT_MATERIALS.addAll(HOLLOW_MATERIALS);
        TRANSPARENT_MATERIALS.addAll(WATER_TYPES);
    }

    public static void setIsWaterSafe(boolean isWaterSafe) {
        if (isWaterSafe) {
            HOLLOW_MATERIALS.addAll(WATER_TYPES);
        } else {
            HOLLOW_MATERIALS.removeAll(WATER_TYPES);
        }
    }

    public static final int RADIUS = 3;
    public static final Vector3D[] VOLUME;

    public static ItemStack convertBlockToItem(final Block block) {
        return new ItemStack(block.getType(), 1);
    }


    public static class Vector3D {
        public int x;
        public int y;
        public int z;

        Vector3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    static {
        List<Vector3D> pos = new ArrayList<>();
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    pos.add(new Vector3D(x, y, z));
                }
            }
        }
        pos.sort(Comparator.comparingInt(a -> (a.x * a.x + a.y * a.y + a.z * a.z)));
        VOLUME = pos.toArray(new Vector3D[0]);
    }

    public static Location getTarget(final LivingEntity entity) throws Exception {
        Block block = null;
        try {
            block = entity.getTargetBlock(TRANSPARENT_MATERIALS, 300);
        } catch (NoSuchMethodError ignored) {
        } // failing now :(
        if (block == null) {
            throw new Exception("Not targeting a block");
        }
        return block.getLocation();
    }

    public static boolean isBlockAboveAir(final World world, final int x, final int y, final int z) {
        return y > world.getMaxHeight() || HOLLOW_MATERIALS.contains(world.getBlockAt(x, y - 1, z).getType());
    }

    public static boolean isBlockUnsafeForUser(final Player player, final World world, final int x, final int y, final int z) {
        if (player.isOnline() && world.equals(player.getWorld())) {
            return false;
        }

        if (isBlockDamaging(world, x, y, z)) {
            return true;
        }
        return isBlockAboveAir(world, x, y, z);
    }

    public static boolean isBlockUnsafe(final World world, final int x, final int y, final int z) {
        return isBlockDamaging(world, x, y, z) || isBlockAboveAir(world, x, y, z);
    }

    public static boolean isBlockDamaging(final World world, final int x, final int y, final int z) {
        final Block below = world.getBlockAt(x, y - 1, z);

        switch (below.getType()) {
            case LAVA:
            case FIRE:
                return true;
        }

        if (MaterialUtil.isBed(below.getType())) {
            return true;
        }

        try {
            if (below.getType() == Material.valueOf("FLOWING_LAVA")) {
                return true;
            }
        } catch (Exception ignored) {
        } // 1.13 LAVA uses Levelled

        Material PORTAL = EnumUtil.getMaterial("NETHER_PORTAL", "PORTAL");

        if (world.getBlockAt(x, y, z).getType() == PORTAL) {
            return true;
        }

        return (!HOLLOW_MATERIALS.contains(world.getBlockAt(x, y, z).getType())) || (!HOLLOW_MATERIALS.contains(world.getBlockAt(x, y + 1, z).getType()));
    }

    // Not needed if using getSafeDestination(loc)
    public static Location getRoundedDestination(final Location loc) {
        final World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
    }

    public static Location getSafeDestination(final Player player, final Location loc) {
        return getSafeDestination(loc);
    }

    public static Location getRandLocation(@NotNull final World world) {
        final double borderSize = world.getWorldBorder().getSize();
        final double randomX = ThreadLocalRandom.current().nextDouble(0, borderSize / 2);
        final double randomZ = ThreadLocalRandom.current().nextDouble(0, borderSize / 2);
        return world.getWorldBorder().getCenter().add(randomX, 0, randomZ);
    }

    public static Location getSafeDestination(final Location loc) {
        if (loc == null || loc.getWorld() == null) {
            return null;
        }
        final World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        final int origX = x;
        final int origY = y;
        final int origZ = z;
        while (isBlockAboveAir(world, x, y, z)) {
            y -= 1;
            if (y < 0) {
                y = origY;
                break;
            }
        }
        if (isBlockUnsafe(world, x, y, z)) {
            x = Math.round(loc.getX()) == origX ? x - 1 : x + 1;
            z = Math.round(loc.getZ()) == origZ ? z - 1 : z + 1;
        }
        int i = 0;
        while (isBlockUnsafe(world, x, y, z)) {
            i++;
            if (i >= VOLUME.length) {
                x = origX;
                y = origY + RADIUS;
                z = origZ;
                break;
            }
            x = origX + VOLUME[i].x;
            y = origY + VOLUME[i].y;
            z = origZ + VOLUME[i].z;
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y += 1;
            if (y >= world.getMaxHeight()) {
                x += 1;
                break;
            }
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y -= 1;
            if (y <= 1) {
                x += 1;
                y = world.getHighestBlockYAt(x, z);
                if (x - 48 > loc.getBlockX()) {
                    return null;
                }
            }
        }
        return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
    }
}