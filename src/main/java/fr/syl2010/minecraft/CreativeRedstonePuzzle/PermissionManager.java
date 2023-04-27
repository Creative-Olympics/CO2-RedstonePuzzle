package fr.syl2010.minecraft.CreativeRedstonePuzzle;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PermissionManager {
  
  public boolean isOperator(OfflinePlayer player) {
    return player.isOp();
  }
  
  public boolean isOperator(UUID player) {
    return Bukkit.getOfflinePlayer(player).isOp();
  }

}
