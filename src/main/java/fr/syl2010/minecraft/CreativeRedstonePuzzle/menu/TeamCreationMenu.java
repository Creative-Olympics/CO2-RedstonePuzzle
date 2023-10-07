package fr.syl2010.minecraft.CreativeRedstonePuzzle.menu;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.GameTeam;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.TeamManager;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;

public class TeamCreationMenu extends Menu {

  private static final class TeamSpec {
    private String    id;
    private String    name;
    private ChatColor color   = ChatColor.WHITE;
    private Set<UUID> members = new HashSet<>();

    private TeamSpec() {}

    public TeamSpec withId(String id) {
      this.id = id;
      if (name == null) {
        name = id;
      }
      return this;
    }

    public TeamSpec withName(String name) {
      this.name = name;
      if (id == null) {
        id = name;
      }
      return this;
    }

    public TeamSpec withColor(ChatColor color) {
      this.color = color;
      return this;
    }

    public TeamSpec addMember(UUID member) {
      this.members.add(member);
      return this;
    }

    public TeamSpec removeMember(UUID member) {
      this.members.remove(member);
      return this;
    }

    public TeamSpec clearMembers() {
      this.members.clear();
      return this;
    }

    public GameTeam build(TeamManager teamManager) {
      Objects.requireNonNull(id);
      Objects.requireNonNull(name);

      GameTeam team = teamManager.createTeam(id, name, color);
      members.forEach(member -> teamManager.addMember(id, member));
      return team;
    }
  }

  private final TeamManager teamManager;
  private final Menu        previousMenu;
  private final TeamSpec    spec = new TeamSpec();

  public TeamCreationMenu(Menu previousMenu) {
    super(builder -> builder
      .title("Team Creation Menu")
      .rows(6)
      .disableUpdateTask());

    this.previousMenu = previousMenu;
    teamManager = CreativeRedstonePuzzlePlugin.getPlugin().getTeamManager();
  }

  private IntelligentItem idBanner;

  @Override
  public void init(Player player, InventoryContents contents) {
    fillCorners(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), contents);

    contents.set(4, idBanner = IntelligentItem.of(new ItemStack(Material.WHITE_BANNER), event -> {
      // FIXME open id edit anvil
    }));

    contents.set(11, IntelligentItem.of(new ItemStack(Material.PAPER), event -> {
      // FIXME open name edit anvil
    }));
    contents.set(29, IntelligentItem.of(new ItemStack(Material.WHITE_DYE), event -> {
      // FIXME select banner color
      // FIXME recolor banner and dye
    }));

    contents.set(22, IntelligentItem.of(new ItemStack(Material.BARRIER), event -> {
      spec.clearMembers();
      // FIXME update idbanner descriptions
    }));

    // FIXME add member item
    // FIXME remove member item

    // FIXME approve item
    // FIXME cancel item
  }

}
