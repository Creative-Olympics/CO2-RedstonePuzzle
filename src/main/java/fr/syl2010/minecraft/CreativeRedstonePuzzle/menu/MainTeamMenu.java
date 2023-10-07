package fr.syl2010.minecraft.CreativeRedstonePuzzle.menu;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.ColorUtils;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.GameTeam;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.team.TeamManager;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;

public class MainTeamMenu extends Menu {

  private final TeamManager teamManager;

  private final Menu previousMenu;

  public MainTeamMenu(Menu previousMenu) {
    super(builder -> builder.identifier("main_team_menu")
      .title("Main Team Configuration")
      .permanentCache()
      .rows(5)
      .disableUpdateTask());

    teamManager = CreativeRedstonePuzzlePlugin.getPlugin().getTeamManager();
    this.previousMenu = previousMenu;
  }

  @Override
  public void init(Player player, InventoryContents contents) {
    fillCorners(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), contents);

    Pagination pagination = contents.pagination();
    pagination.setItemsPerPage(28);
    pagination.iterator(FULL_PAGINATION);

    for (GameTeam team : teamManager.getTeams().values()) {
      pagination.addItem(buildTeamItem(team, pagination));
    }

    pagination.addItem(IntelligentItem.of(Head.LIME_PLUS.buildHead(1), event -> {
      // FIXME Open team creation menu
    }));

    placePaginationArrows(47, 51, contents);

    // FIXME Reset Teams

    contents.set(48, buildBackArrow(previousMenu));

    // teamManager.createTeam(null, null, null);
    // teamManager.removeTeam(null);
    // teamManager.clearTeams();

  }

  private IntelligentItem buildTeamItem(GameTeam team, Pagination pagination) {
    return IntelligentItem.of(buildBanner(team), event -> {
      if (event.getClick() == ClickType.DROP) {
        teamManager.removeTeam(team.getId());
        pagination.remove(event.getSlot());
      } else if (event.getClick().isLeftClick()) {
        // FIXME open team menu
      }
    });
  }

  private ItemStack buildBanner(GameTeam team) {
    ItemStack item = new ItemStack(ColorUtils.getBannerMaterial(team.getColor()));
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(team.getDisplayName());
    meta.setLore(List.of()); // FIXME teamInfo

    return item;
  }

}
