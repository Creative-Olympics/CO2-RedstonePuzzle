package fr.syl2010.minecraft.CreativeRedstonePuzzle.team;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.ChatColor;

public class GameTeam {

  private final Set<UUID> members = new HashSet<>();
  
  private final ChatColor color;
  private String name;
  
  GameTeam(String name, ChatColor color) {
    this.name = Objects.requireNonNull(name);
    this.color = Objects.requireNonNull(color);
  }
  
  public String getName() {
    return name;
  }
  
  void setName(String name) {
    this.name = Objects.requireNonNull(name);
  }
  
  public ChatColor getColor() {
    return color;
  }
  
  public Set<UUID> getMembers() {
    return Collections.unmodifiableSet(members);
  }
  
  public boolean isMember(UUID uuid) {
    return members.contains(uuid);
  }
  
  void addMember(UUID uuid) {
    members.add(uuid);
  }
  
void removeMember(UUID uuid) {
  members.remove(uuid);
  }
}
