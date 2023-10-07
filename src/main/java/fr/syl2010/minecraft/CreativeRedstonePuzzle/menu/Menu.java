package fr.syl2010.minecraft.CreativeRedstonePuzzle.menu;

import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import fr.syl2010.minecraft.CreativeRedstonePuzzle.CreativeRedstonePuzzlePlugin;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.InventoryOptions;
import io.github.rysefoxx.inventory.plugin.other.CornerType;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory.Builder;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator.SlotIteratorType;

public abstract class Menu implements InventoryProvider {

  private static final RyseInventory.Builder DEFAULT_BUILDER = RyseInventory.builder()
    .options(InventoryOptions.NO_ITEM_PICKUP);

  protected static final SlotIterator FULL_PAGINATION = SlotIterator.builder()
    .startPosition(1, 1)
    .endPosition(4, 7)
    .blackList(17, 18, 26, 27, 35, 36)
    .type(SlotIteratorType.HORIZONTAL)
    .build();

  protected final RyseInventory menu;

  public Menu(Consumer<RyseInventory.Builder> menuBuilder) {
    Builder builder = DEFAULT_BUILDER.newInstance().provider(this);
    menuBuilder.accept(builder);
    menu = builder.build(CreativeRedstonePuzzlePlugin.getPlugin());
  }

  protected final RyseInventory getInternalMenu() {
    return menu;
  }

  public final void open(Player player) {
    menu.open(player);
  }

  public final void open(Player... players) {
    menu.open(players);
  }

  public final void openAll() {
    menu.openAll();
  }

  public final void openTo(int page, Player player) {
    menu.open(page, player);
  }

  public final void openTo(int page, Player... players) {
    menu.open(page, players);
  }

  public final void openAllTo(int page) {
    menu.openAll(page);
  }

  public final void fillCorners(ItemStack item, InventoryContents content) {
    int rows = menu.size(content) / 9;

    if (rows < 3) {
      content.set(0, item);
      content.set(8, item);

      if (rows == 2) {
        content.set(9, item);
        content.set(17, item);
      }
    } else {
      content.set(0, item);
      content.set(1, item);
      content.set(9, item);

      content.set(7, item);
      content.set(8, item);
      content.set(17, item);

      int bottomLeft = content.findCorner(CornerType.BOTTM_LEFT);
      content.set(bottomLeft - 9, item);
      content.set(bottomLeft, item);
      content.set(bottomLeft + 1, item);

      int bottomRight = content.findCorner(CornerType.BOTTOM_RIGHT);
      content.set(bottomRight - 9, item);
      content.set(bottomRight - 1, item);
      content.set(bottomRight, item);
    }
  }

  public static final void placePaginationArrows(int backArrowSlot, int frontArrowSlot, InventoryContents content) {
    Pagination pagination = content.pagination();
    RyseInventory inventory = pagination.inventory();
    pagination.next();
    if (!pagination.isFirst()) {
      content.set(backArrowSlot, IntelligentItem.of(Head.QUARTZ_ARROW_LEFT.buildHead(pagination.page() - 1),
        event -> ifClick(event, () -> inventory.open((Player) event.getWhoClicked(), pagination.previous().page()))));
    }
    if (!pagination.isLast()) {
      content.set(frontArrowSlot, IntelligentItem.of(Head.QUARTZ_ARROW_RIGHT.buildHead(pagination.page() + 1),
        event -> ifClick(event, () -> inventory.open((Player) event.getWhoClicked(), pagination.next().page()))));
    }
  }

  public static final IntelligentItem buildBackArrow(Menu previousMenu) {
    return IntelligentItem.of(Head.QUARTZ_ARROW_LEFT_UP.buildHead(1),
      event -> ifClick(event, () -> previousMenu.open((Player) event.getWhoClicked())));
  }

  public static final boolean isClick(InventoryClickEvent event) {
    return event.isLeftClick() || event.isRightClick();
  }

  public static final boolean ifClick(InventoryClickEvent event, Runnable ifClick) {
    if (isClick(event)) {
      ifClick.run();
      return true;
    } else {
      return false;
    }
  }
}
