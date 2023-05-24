package fr.syl2010.minecraft.CreativeRedstonePuzzle.team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.event.team.TeamEvent;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.event.team.TeamEvent.TeamChangeNameEvent;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.event.team.TeamMemberEvent;

public class TeamManager {

  private final Map<String, GameTeam> teamById     = new HashMap<>();
  private final Map<UUID, String>     teamByMember = new HashMap<>(32);

  public GameTeam createTeam(String id, String name, ChatColor color) {
    GameTeam newTeam = new GameTeam(name, color);

    Bukkit.getPluginManager().callEvent(new TeamEvent.TeamCreateEvent(newTeam));

    teamById.put(id, newTeam);
    return newTeam;
  }

  @Nullable
  public GameTeam removeTeam(String id) {
    GameTeam team = teamById.get(id);
    if (team != null) {

      Bukkit.getPluginManager().callEvent(new TeamEvent.TeamRemoveEvent(team));

      teamById.remove(id);
      team.getMembers().forEach(teamByMember::remove);
    }
    return team;
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
    return true;
  }

  public boolean removeMember(UUID uuid) {
    GameTeam team = getTeamOf(uuid);
    if (team == null)
      return false;

    Bukkit.getPluginManager().callEvent(new TeamMemberEvent.TeamMemberLeaveEvent(team, uuid));

    team.removeMember(uuid);
    teamByMember.remove(uuid);
    return true;
  }

  public boolean removeMember(String id, UUID uuid) {
    GameTeam team = getTeam(id);
    if (team == null || team.isMember(uuid))
      return false;

    Bukkit.getPluginManager().callEvent(new TeamMemberEvent.TeamMemberLeaveEvent(team, uuid));

    team.removeMember(uuid);
    teamByMember.remove(uuid);
    return true;
  }

  @Nullable
  public GameTeam getTeamOf(UUID uuid) {
    String id = teamByMember.get(uuid);
    return id != null ? getTeam(id) : null;
  }

}
