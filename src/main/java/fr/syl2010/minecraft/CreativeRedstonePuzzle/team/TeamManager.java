package fr.syl2010.minecraft.CreativeRedstonePuzzle.team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.ChatColor;

public class TeamManager {

  private final Map<String, GameTeam> teamByIdMap = new HashMap<>();
  private final Map<UUID, String> teamByMember = new HashMap<>(32);
  
  public void createTeam(String id, String name, ChatColor color) {
    //FIXME event
    teamByIdMap.put(id, new GameTeam(name, color));
  }

  //FIXME event
  @Nullable
  public GameTeam removeTeam(String id) {
    return teamByIdMap.remove(id);
  }
  

  @Nullable
  public GameTeam getTeam(String id) {
    return teamByIdMap.get(id);
  }
  
  public boolean setTeamName(String id, String newName) {
    GameTeam team = getTeam(id);
    if(team == null) {
      return false;
    }
    //FIXME event
    
    team.setName(newName);
    return true;
  }
  
  public boolean addMember(String id, UUID uuid) {
    GameTeam team = getTeam(id);
    if(team == null) {
      return false;
    }
    //FIXME event
    
    team.addMember(uuid);
    teamByMember.put(uuid, id);
    return true;
  }
  
  public boolean removeMember(UUID uuid) {
    String id = teamByMember.get(uuid);
    if(id == null) {
      return false;
    }
    GameTeam team = getTeam(id);
    if(team == null) {
      return false;
    }
    //FIXME event
    
    teamByMember.remove(uuid);
    team.removeMember(uuid);
    return true;
  }

  @Nullable
  public GameTeam getTeamOf(UUID uuid) {
    String id = teamByMember.remove(uuid);
    if(id == null) {
      return null;
    }
    return getTeam(id);
  }
  
}
