package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.map;

import java.util.Objects;
import org.bukkit.Location;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances.PuzzleRoomInstance;

public class PuzzleRoom {

  private Location playerSpawnpoint;
  private boolean  shouldTeleport;

  public PuzzleRoom(Location playerSpawnpoint) {
    setPlayerSpawnpoint(playerSpawnpoint);
  }

  public Location getPlayerSpawnpoint() {
    return playerSpawnpoint.clone();
  }

  public void setPlayerSpawnpoint(Location playerSpawnpoint) {
    this.playerSpawnpoint = Objects.requireNonNull(playerSpawnpoint);
  }

  public boolean shouldTeleport() {
    return shouldTeleport;
  }

  public void setShouldTeleport(boolean shouldTeleport) {
    this.shouldTeleport = shouldTeleport;
  }

  public PuzzleRoomInstance createInstance() {
    return new PuzzleRoomInstance(this);
  }
}
