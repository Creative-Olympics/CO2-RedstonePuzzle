package fr.syl2010.minecraft.CreativeRedstonePuzzle.team;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.ChatColor;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.serialisation.JsonMapKey;

public class GameTeam {

  @JsonMapKey
  private final String    id;
  private final ChatColor color;

  private final Set<UUID> members = new HashSet<>();

  private String name;

  GameTeam(String id, String name, ChatColor color) {
    this.id = Objects.requireNonNull(id);
    this.name = Objects.requireNonNull(name);
    this.color = Objects.requireNonNull(color);
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDisplayName() {
    return color.toString() + name;
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

  boolean addMember(UUID uuid) {
    return members.add(uuid);
  }

  boolean removeMember(UUID uuid) {
    return members.remove(uuid);
  }
}
