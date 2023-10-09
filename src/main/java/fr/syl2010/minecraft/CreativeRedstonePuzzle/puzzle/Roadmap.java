package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room.PuzzleRoom;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room.serialization.PuzzleRoomNameDeserializer;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.room.serialization.PuzzleRoomNameSerializer;

public class Roadmap {

  @JsonSerialize(contentUsing = PuzzleRoomNameSerializer.class)
  @JsonDeserialize(contentUsing = PuzzleRoomNameDeserializer.class)
  private final List<PuzzleRoom> rooms;

  public Roadmap() {
    rooms = new ArrayList<>();
  }

  public Roadmap(List<PuzzleRoom> rooms) {
    this.rooms = new ArrayList<>(rooms);
  }

  public Roadmap(PuzzleRoom... rooms) {
    this.rooms = Lists.newArrayList(rooms);
  }

  public List<PuzzleRoom> getRooms() {
    return Collections.unmodifiableList(rooms);
  }

  public int getRoomCount() {
    return rooms.size();
  }

  public PuzzleRoom getRoom(int index) {
    return rooms.get(index);
  }

  void clear() {
    rooms.clear();
  }

  public class Spec {
    Spec() {}

    public List<PuzzleRoom> getRooms() {
      return rooms;
    }

    public int getRoomCount() {
      return rooms.size();
    }

    public PuzzleRoom getRoom(int index) {
      return rooms.get(index);
    }

    public PuzzleRoom removeRoom(int index) {
      return rooms.remove(index);
    }

    public void addRoom(PuzzleRoom room) {
      rooms.add(room);
    }

    public void addRoom(int index, PuzzleRoom room) {
      rooms.add(index, room);
    }

    public void switchRoom(int firstIndex, int secondIndex) {
      Objects.checkIndex(firstIndex, rooms.size());
      Objects.checkIndex(secondIndex, rooms.size());

      rooms.set(firstIndex, rooms.set(secondIndex, rooms.get(firstIndex)));
    }

    public void clearRooms() {
      clear();
    }
  }
}
