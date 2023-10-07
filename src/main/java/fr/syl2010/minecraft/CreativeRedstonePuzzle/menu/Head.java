package fr.syl2010.minecraft.CreativeRedstonePuzzle.menu;

import java.lang.reflect.Field;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public enum Head {

  QUARTZ_ARROW_RIGHT("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0="),
  QUARTZ_ARROW_LEFT("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzNlOTE5MTlkYjBhY2VmZGMyNzJkNjdmZDg3YjRiZTg4ZGM0NGE5NTg5NTg4MjQ0NzRlMjFlMDZkNTNlNiJ9fX0="),
  QUARTZ_ARROW_LEFT_UP("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJlZWVmZDk4NmFkMTA2NDM0YzY0ZmFjNjY5ZjcwZWY4OGRjNmJiNmM4MmNiZDdmYThkNjY0MzY5NzNkYjFhZCJ9fX0="),

  LIME_PLUS("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19"),
  RED_MINUS("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=");

  private final String skin;

  private Head(String skin) {
    this.skin = skin;
  }

  public ItemStack buildHead(int amount) {
    ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount);
    SkullMeta meta = (SkullMeta) item.getItemMeta();

    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    profile.getProperties().removeAll("textures");
    profile.getProperties().put("textures", new Property("textures", skin, ""));

    try {
      Field field = meta.getClass().getDeclaredField("profile");
      field.setAccessible(true);
      field.set(meta, profile);
    } catch (Exception e) {
      return item;
    }

    item.setItemMeta(meta);
    return item;
  }
}
