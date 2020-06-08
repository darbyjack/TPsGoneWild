package me.glaremasters.tpsgonewild.utils;

import org.bukkit.Material;

import java.util.Set;

/**
 * Modified from https://github.com/EssentialsX/Essentials/blob/2.x/Essentials/src/com/earth2me/essentials/utils/MaterialUtil.java
 */
public class MaterialUtil {


    private static final Set<Material> BEDS;

    static {

        BEDS = EnumUtil.getAllMatching(Material.class, "BED", "BED_BLOCK", "WHITE_BED", "ORANGE_BED",
                "MAGENTA_BED", "LIGHT_BLUE_BED", "YELLOW_BED", "LIME_BED", "PINK_BED", "GRAY_BED",
                "LIGHT_GRAY_BED", "CYAN_BED", "PURPLE_BED", "BLUE_BED", "BROWN_BED", "GREEN_BED",
                "RED_BED", "BLACK_BED");

    }

    public static boolean isBed(Material material) {
        return BEDS.contains(material);
    }
}