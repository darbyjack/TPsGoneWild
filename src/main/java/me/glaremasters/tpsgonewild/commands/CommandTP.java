package me.glaremasters.tpsgonewild.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import io.papermc.lib.PaperLib;
import me.glaremasters.tpsgonewild.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAlias("tpgw")
public class CommandTP extends BaseCommand {

    @Default
    @CommandPermission("tpgw.tp")
    public final void execute(@NotNull final Player player) {
        final Location location = LocationUtil.getRandLocation(player.getWorld());
        PaperLib.teleportAsync(player, LocationUtil.getSafeDestination(location));
    }
}
