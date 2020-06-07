package me.glaremasters.tpsgonewild.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import io.papermc.lib.PaperLib;
import me.glaremasters.tpsgonewild.Tpsgonewild;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAlias("tpgw")
public class CommandTP extends BaseCommand {
    @Dependency private Tpsgonewild tpsgonewild;

    @Default
    @CommandPermission("tpgw.tp")
    public void execute(@NotNull final Player player) {
        final Location location = tpsgonewild.getLocationUtil().getRandLocation(player.getWorld());
        PaperLib.teleportAsync(player, player.getWorld().getHighestBlockAt(location).getLocation());
    }
}
