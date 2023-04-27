package fr.syl2010.minecraft.CreativeRedstonePuzzle.player;

import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerData {

  public final UUID uuid;
  
  PlayerData(UUID player) {
    this.uuid = player;
  }
  
  public UUID getUuid() {
    return uuid;
  }
  
  @Nullable
  public Player getOnlinePlayer() {
    return Bukkit.getPlayer(uuid);
  }
  
  public OfflinePlayer getOfflinePlayer() {
    return Bukkit.getOfflinePlayer(uuid);
  }
}
