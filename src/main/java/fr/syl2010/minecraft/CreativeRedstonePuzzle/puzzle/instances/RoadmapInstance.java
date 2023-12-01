package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.Roadmap;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room.PuzzleRoom;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.GameTeam;

public class RoadmapInstance {

  private static final int SPACE_BETWEEN_ROOMS = 15;

  private final GameTeam team;

  private final World                    world;
  private final List<PuzzleRoomInstance> rooms;

  private int                currentRoomIndex = -1;
  private PuzzleRoomInstance currentRoom      = null;

  public RoadmapInstance(Roadmap roadmap, GameTeam team) {
    this.team = Objects.requireNonNull(team);

    world = CreativeRedstonePuzzlePlugin.getPlugin().getWorldManager().generateTeamWorld(team);

    Location roomGenLocation = new Location(world, 0, 0, 0);

    List<PuzzleRoom> roomModels = roadmap.getRooms();
    if (roomModels.isEmpty()) throw new IllegalArgumentException("Can't generate a map if no room is assigned");

    rooms = new ArrayList<>(roomModels.size());

    for (PuzzleRoom roomModel : roadmap.getRooms()) {
      if (roomModel.getSteps().isEmpty()) throw new IllegalArgumentException("Can't complete a room without any step!");

      roomModel.getStructure().place(roomGenLocation, true, StructureRotation.NONE, Mirror.NONE, 0, 1, ThreadLocalRandom.current());
      PuzzleRoomInstance room = new PuzzleRoomInstance(roomModel, roomGenLocation);
      rooms.add(room);

      if (rooms.size() == 1) {
        world.setSpawnLocation(room.getPlayerSpawnpoint());
      }

      roomGenLocation.add(0, 0, SPACE_BETWEEN_ROOMS + roomModel.getStructure().getSize().getBlockZ());
    }
  }

  public GameTeam getTeam() {
    return team;
  }

  public World getWorld() {
    return world;
  }

  public PuzzleRoomInstance getCurrentRoom() {
    return currentRoom;
  }

  private PuzzleRoomInstance nextRoom() {
    if (++currentRoomIndex >= getRoomCount()) return currentRoom = null;
    else
      return currentRoom = rooms.get(currentRoomIndex);
  }

  public boolean teleportToNextRoom() {
    PuzzleRoomInstance room = nextRoom();
    if (room == null) return false;
    else {
      for (UUID member : team.getMembers()) {
        Player player = Bukkit.getPlayer(member);
        if (player != null) {
          if (currentRoomIndex > 0) {
            world.getNearbyEntities(player.getLocation(), 3, 3, 3, entity -> entity.getType() == EntityType.DROPPED_ITEM)
              .forEach(Entity::remove);
          }
          player.getInventory().clear();
          player.teleport(room.getPlayerSpawnpoint());
        }
      }
      return true;
    }
  }

  public int getCurrentRoomIndex() {
    return currentRoomIndex;
  }

  public int getRoomCount() {
    return rooms.size();
  }

  public boolean isCompleted() {
    return currentRoomIndex >= getRoomCount();
  }

}
