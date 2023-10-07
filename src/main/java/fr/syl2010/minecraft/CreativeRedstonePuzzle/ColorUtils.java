package fr.syl2010.minecraft.CreativeRedstonePuzzle;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class ColorUtils {

  private ColorUtils() {}

  public static final Material getBannerMaterial(ChatColor color) {
    return switch (color) {
      case AQUA -> Material.LIGHT_BLUE_BANNER;
      case BLACK -> Material.BLACK_BANNER;
      case BLUE, DARK_BLUE -> Material.BLUE_BANNER;
      case DARK_AQUA -> Material.CYAN_BANNER;
      case DARK_GRAY -> Material.GRAY_BANNER;
      case DARK_GREEN -> Material.GREEN_BANNER;
      case DARK_PURPLE -> Material.PURPLE_BANNER;
      case DARK_RED, RED -> Material.RED_BANNER;
      case GOLD -> Material.ORANGE_BANNER;
      case GRAY -> Material.LIGHT_GRAY_BANNER;
      case GREEN -> Material.LIME_BANNER;
      case LIGHT_PURPLE -> Material.PINK_BANNER;
      case WHITE -> Material.WHITE_BANNER;
      case YELLOW -> Material.YELLOW_BANNER;
      default -> throw new IllegalArgumentException("Unexpected team color: " + color);
    };
  }

  public static final Material getDyeMaterial(ChatColor color) {
    return switch (color) {
      case AQUA -> Material.LIGHT_BLUE_DYE;
      case BLACK -> Material.BLACK_DYE;
      case BLUE, DARK_BLUE -> Material.BLUE_DYE;
      case DARK_AQUA -> Material.CYAN_DYE;
      case DARK_GRAY -> Material.GRAY_DYE;
      case DARK_GREEN -> Material.GREEN_DYE;
      case DARK_PURPLE -> Material.PURPLE_DYE;
      case DARK_RED, RED -> Material.RED_DYE;
      case GOLD -> Material.ORANGE_DYE;
      case GRAY -> Material.LIGHT_GRAY_DYE;
      case GREEN -> Material.LIME_DYE;
      case LIGHT_PURPLE -> Material.PINK_DYE;
      case WHITE -> Material.WHITE_DYE;
      case YELLOW -> Material.YELLOW_DYE;
      default -> throw new IllegalArgumentException("Unexpected team color: " + color);
    };
  }

}
