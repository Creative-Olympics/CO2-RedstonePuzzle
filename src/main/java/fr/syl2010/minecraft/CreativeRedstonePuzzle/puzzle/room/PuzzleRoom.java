package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Location;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances.PuzzleRoomInstance;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.serialisation.JsonMapKey;

public class PuzzleRoom {

  @JsonMapKey
  private final String name;

  private Location playerSpawnpoint;
  private boolean  shouldTeleport;

  private final Set<String> steps;

  public PuzzleRoom(String name, Location playerSpawnpoint) {
    this.name = name;
    this.playerSpawnpoint = Objects.requireNonNull(playerSpawnpoint);
    steps = new HashSet<String>();
  }

  public String getName() {
    return name;
  }

  public Location getPlayerSpawnpoint() {
    return playerSpawnpoint.clone();
  }

  public boolean shouldTeleport() {
    return shouldTeleport;
  }

  public Set<String> getSteps() {
    return Collections.unmodifiableSet(steps);
  }

  public boolean hasStep(String step) {
    return steps.contains(step);
  }

  public int getStepCount() {
    return steps.size();
  }

  public PuzzleRoomInstance createInstance() {
    return new PuzzleRoomInstance(this);
  }

  public class Spec {
    Spec() {}

    public String getName() {
      return name;
    }

    public boolean shouldTeleport() {
      return shouldTeleport;
    }

    public void setShouldTeleport(boolean newShouldTeleport) {
      shouldTeleport = newShouldTeleport;
    }

    public Location getPlayerSpawnpoint() {
      return playerSpawnpoint;
    }

    public void setPlayerSpawnpoint(Location newSpawnpoint) {
      playerSpawnpoint = Objects.requireNonNull(newSpawnpoint);
    }

    public Set<String> getSteps() {
      return steps;
    }
  }
}
