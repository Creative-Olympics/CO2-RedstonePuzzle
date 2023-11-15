package fr.syl2010.minecraft.CreativeRedstonePuzzle.team;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.event.team.TeamEvent;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.event.team.TeamEvent.TeamChangeNameEvent;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.event.team.TeamMemberEvent;

public class TeamManager {

  private static final ObjectMapper MAPPER = CreativeRedstonePuzzlePlugin.createDefaultMapper();

  private final Map<String, GameTeam> teamById     = new HashMap<>();
  private final Map<UUID, String>     teamByMember = new HashMap<>(32);

  private final File saveFile;

  public TeamManager() {
    saveFile = new File(CreativeRedstonePuzzlePlugin.getPlugin().getDataFolder(), "teams.json");
    loadFile();
    saveInFile();
  }

  // FIXME test GameTeam loading and saving
  private boolean loadFile() {
    if (saveFile.exists()) {

      Map<String, GameTeam> newMap;
      try {
        newMap = MAPPER.readValue(saveFile, new TypeReference<Map<String, GameTeam>>() {});
      } catch (IOException e) {
        throw new RuntimeException("Error while loading team datas", e);
      }
      teamById.clear();
      teamByMember.clear();

      teamById.putAll(newMap);
      teamById.forEach((id, team) -> {
        for (UUID member : team.getMembers()) {
          teamByMember.put(member, id);
        }
      });

      return true;
    } else return false;
  }

  private CompletableFuture<Void> saveInFile() {
    Map<String, GameTeam> savingMap = ImmutableMap.copyOf(teamById);
    return CompletableFuture.runAsync(() -> {
      try {
        MAPPER.writeValue(saveFile, savingMap);
      } catch (IOException e) {
        CreativeRedstonePuzzlePlugin.getPlugin().getLogger().log(Level.SEVERE, "Error while saving team datas", e);
        throw new RuntimeException("Error while saving team datas", e);
      }
    });
  }

  public GameTeam createTeam(String id, String name, ChatColor color) {
    if (teamById.containsKey(id)) throw new IllegalArgumentException("Team already exist");

    GameTeam newTeam = new GameTeam(id, name, color);

    Bukkit.getPluginManager().callEvent(new TeamEvent.TeamCreateEvent(newTeam));

    teamById.put(id, newTeam);

    saveInFile();
    return newTeam;
  }

  @Nullable
  public GameTeam removeTeam(String id) {
    GameTeam team = teamById.get(id);
    if (team != null) {

      Bukkit.getPluginManager().callEvent(new TeamEvent.TeamRemoveEvent(team));

      teamById.remove(id);
      team.getMembers().forEach(teamByMember::remove);

      saveInFile();
    }
    return team;
  }

  public void clearTeams() {
    Iterator<GameTeam> teamIterator = teamById.values().iterator();

    while (teamIterator.hasNext()) {
      Bukkit.getPluginManager().callEvent(new TeamEvent.TeamRemoveEvent(teamIterator.next()));
      teamIterator.remove();
    }
    teamByMember.clear();

    saveInFile();
  }

  @Nullable
  public GameTeam getTeam(String id) {
    return teamById.get(id);
  }

  public boolean setTeamName(String id, String newName) {
    GameTeam team = getTeam(id);
    if (team == null)
      return false;

    TeamChangeNameEvent event = new TeamEvent.TeamChangeNameEvent(team, newName);
    Bukkit.getPluginManager().callEvent(event);
    if (event.isCancelled())
      return false;

    team.setName(event.getNewName());

    saveInFile();
    return true;
  }

  public boolean addMember(String id, UUID uuid) {
    GameTeam team = getTeam(id);
    if (team == null)
      return false;

    GameTeam oldTeam = getTeamOf(uuid);
    if (team.equals(oldTeam))
      return false;

    else if (oldTeam != null) {
      Bukkit.getPluginManager().callEvent(new TeamMemberEvent.TeamMemberLeaveEvent(oldTeam, uuid));
      oldTeam.removeMember(uuid);
      teamByMember.remove(uuid);
    }
    Bukkit.getPluginManager().callEvent(new TeamMemberEvent.TeamMemberJoinEvent(team, uuid, oldTeam));

    team.addMember(uuid);
    teamByMember.put(uuid, id);

    saveInFile();
    return true;
  }

  public boolean removeMember(UUID uuid) {
    GameTeam team = getTeamOf(uuid);
    if (team == null)
      return false;

    Bukkit.getPluginManager().callEvent(new TeamMemberEvent.TeamMemberLeaveEvent(team, uuid));

    team.removeMember(uuid);
    teamByMember.remove(uuid);

    saveInFile();
    return true;
  }

  public boolean removeMember(String id, UUID uuid) {
    GameTeam team = getTeam(id);
    if (team == null || team.isMember(uuid))
      return false;

    Bukkit.getPluginManager().callEvent(new TeamMemberEvent.TeamMemberLeaveEvent(team, uuid));

    team.removeMember(uuid);
    teamByMember.remove(uuid);

    saveInFile();
    return true;
  }

  @Nullable
  public GameTeam getTeamOf(UUID uuid) {
    String id = teamByMember.get(uuid);
    return id != null ? getTeam(id) : null;
  }

  public int getTeamCount() {
    return teamById.size();
  }

  public Map<String, GameTeam> getTeams() {
    return Collections.unmodifiableMap(teamById);
  }

}
