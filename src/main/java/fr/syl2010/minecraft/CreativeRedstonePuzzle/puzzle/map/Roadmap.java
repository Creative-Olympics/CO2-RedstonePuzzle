package fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.google.common.collect.Lists;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.puzzle.instances.RoadmapInstance;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.GameTeam;

public class Roadmap {

  private final List<PuzzleRoom> rooms;
  private GameTeam               team = null;

  public Roadmap(List<PuzzleRoom> rooms) {
    this.rooms = new ArrayList<>(rooms);
  }

  public Roadmap(PuzzleRoom... rooms) {
    this.rooms = Lists.newArrayList(rooms);
  }

  public GameTeam getTeam() {
    return team;
  }

  public void setTeam(GameTeam team) {
    this.team = Objects.requireNonNull(team);
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

  public RoadmapInstance createInstance() {
    Objects.requireNonNull(team, "A team has not been assigned to this roadmap!");
    return new RoadmapInstance(this);
  }
}
