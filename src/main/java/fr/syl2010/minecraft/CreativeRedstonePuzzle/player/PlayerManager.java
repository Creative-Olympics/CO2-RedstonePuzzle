package fr.syl2010.minecraft.CreativeRedstonePuzzle.player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

public class PlayerManager {

  private final Map<UUID, PlayerData> playerDataMap = new HashMap<>(32);
  
  public PlayerData getOrCreatePlayerData(UUID player) {
    return playerDataMap.computeIfAbsent(player, PlayerData::new);
  }
  
  public PlayerData replacePlayerData(UUID player) {
    PlayerData playerData = new PlayerData(player);
    playerDataMap.put(player, playerData);
    return playerData;
  }
  
  @Nullable
  public PlayerData deletePlayerData(UUID player) {
    return playerDataMap.remove(player);
  }
  
  public boolean hasData(UUID player) {
    return playerDataMap.containsKey(player);
  }
  
  @Nullable
  public PlayerData getPlayerData(UUID player) {
    return playerDataMap.get(player);
  }
  
  public Map<UUID, PlayerData> getAllDatas() {
    return Collections.unmodifiableMap(playerDataMap);
  }
  
}
