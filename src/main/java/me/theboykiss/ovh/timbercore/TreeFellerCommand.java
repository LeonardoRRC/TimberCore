package me.theboykiss.ovh.timbercore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TreeFellerCommand implements CommandExecutor {
    private final HashMap<UUID, Boolean> autoFellStates = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerId = player.getUniqueId();
            boolean newState = !autoFellStates.getOrDefault(playerId, true);
            autoFellStates.put(playerId, newState);
            player.sendMessage("El talado automatico esta " + (newState ? "Activado" : "Desactivado") + ".");
            return true;
        } else {
            sender.sendMessage("Este comando solo puede ser ejecutado por jugadores.");
            return false;
        }
    }

    public boolean isAutoFellingEnabled(UUID playerId) {
        return autoFellStates.getOrDefault(playerId, true);
    }
}
