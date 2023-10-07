package fr.syl2010.minecraft.CreativeRedstonePuzzle.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;

public class MainPuzzleMenu extends Menu {

  private final MainTeamMenu teamMenu;

  public MainPuzzleMenu() {
    super(builder -> builder.identifier("main_puzzle_menu")
      .title("Main Puzzle Configuration")
      .permanentCache()
      .rows(3)
      .disableUpdateTask());

    teamMenu = new MainTeamMenu(this);
  }

  @Override
  public void init(Player player, InventoryContents contents) {
    contents.set(2, 3, IntelligentItem.of(new ItemStack(Material.PLAYER_HEAD), event -> ifClick(event, () -> teamMenu.open(player))));
    contents.set(2, 5, IntelligentItem.of(new ItemStack(Material.JIGSAW), event -> {
      // FIXME open puzzle menu
    }));
    contents.set(2, 7, IntelligentItem.of(new ItemStack(Material.STRUCTURE_BLOCK), event -> {
      // FIXME open rooms menu
    }));
  }
}
