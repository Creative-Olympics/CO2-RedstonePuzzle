package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;
import javax.annotation.Nullable;
import org.bukkit.NamespacedKey;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;

public class RoomManager {

  private static final ObjectMapper MAPPER = CreativeRedstonePuzzlePlugin.createDefaultMapper();

  private Map<String, PuzzleRoom> roomByName = new HashMap<>();

  private final File saveFile;

  public RoomManager() {
    saveFile = new File(CreativeRedstonePuzzlePlugin.getPlugin().getDataFolder(), "rooms.json");
    loadFile();
    saveInFile();
  }

  // FIXME test PuzzleRoom loading and saving
  private boolean loadFile() {
    if (saveFile.exists()) {

      try {
        roomByName = MAPPER.readValue(saveFile, new TypeReference<Map<String, PuzzleRoom>>() {});
      } catch (IOException e) {
        throw new RuntimeException("Error while loading team datas", e);
      }

      return true;
    } else {
      return false;
    }
  }

  private CompletableFuture<Void> saveInFile() {
    Map<String, PuzzleRoom> savingMap = ImmutableMap.copyOf(roomByName);
    return CompletableFuture.runAsync(() -> {
      try {
        MAPPER.writeValue(saveFile, savingMap);
      } catch (IOException e) {
        CreativeRedstonePuzzlePlugin.getPlugin().getLogger().log(Level.SEVERE, "Error while saving room datas", e);
        throw new RuntimeException("Error while saving room datas", e);
      }
    });
  }

  public PuzzleRoom createRoom(String name, NamespacedKey structurePath) {
    if (roomByName.containsKey(name)) {
      throw new IllegalArgumentException("Room already exist");
    }

    PuzzleRoom newRoom = new PuzzleRoom(name, structurePath);

    // TODO create room event

    roomByName.put(name, newRoom);

    saveInFile();
    return newRoom;
  }

  @Nullable
  public PuzzleRoom removeRoom(String name) {
    PuzzleRoom room = roomByName.get(name);
    if (room != null) {

      // TODO remove room event

      roomByName.remove(name);
      saveInFile();
    }
    return room;
  }

  public void clearRooms() {
    Iterator<PuzzleRoom> roomIterator = roomByName.values().iterator();

    while (roomIterator.hasNext()) {
      // TODO remove room event
      roomIterator.remove();
    }

    saveInFile();
  }

  @Nullable
  public PuzzleRoom getRoom(String name) {
    return roomByName.get(name);
  }

  @Nullable
  public PuzzleRoom modifyRoom(String name, Consumer<PuzzleRoom.Spec> modifier) {
    PuzzleRoom room = getRoom(name);
    if (room == null)
      return null;

    // TODO update room event

    modifier.accept(room.new Spec());

    saveInFile();
    return room;
  }
}
