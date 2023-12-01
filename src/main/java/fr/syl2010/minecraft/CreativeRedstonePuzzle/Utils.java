package fr.syl2010.minecraft.CreativeRedstonePuzzle;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Utils {
  private Utils() {}

  public static <T> boolean iterateThroughTicks(Iterable<T> iterable, Consumer<T> forEach, int time,
                                                TimeUnit unit, JavaPlugin plugin) throws InterruptedException {
    for (T element : iterable) {
      CountDownLatch latch = new CountDownLatch(1);
      Bukkit.getScheduler().runTask(plugin, () -> {
        forEach.accept(element);
        latch.countDown();
      });
      if (!latch.await(time, unit)) return false;
    }
    return true;
  }

  public static <T> T syncAndAwait(Supplier<T> supplier, JavaPlugin plugin) throws InterruptedException {
    MutableObject<T> result = new MutableObject<>();
    CountDownLatch latch = new CountDownLatch(1);
    Bukkit.getScheduler().runTask(plugin, () -> {
      result.setValue(supplier.get());
      latch.countDown();
    });
    latch.await();
    return result.getValue();
  }

  public static <T> T syncAndAwait(Supplier<T> supplier, int time, TimeUnit unit, JavaPlugin plugin) throws InterruptedException {
    MutableObject<T> result = new MutableObject<>();
    CountDownLatch latch = new CountDownLatch(1);
    Bukkit.getScheduler().runTask(plugin, () -> {
      result.setValue(supplier.get());
      latch.countDown();
    });
    if (!latch.await(time, unit)) return null;
    else
      return result.getValue();
  }
}
