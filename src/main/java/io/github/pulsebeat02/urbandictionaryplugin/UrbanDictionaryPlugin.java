package io.github.pulsebeat02.urbandictionaryplugin;

import static java.util.Objects.requireNonNull;

import io.github.pulsebeat02.urbandictionaryplugin.cache.CacheManager;
import io.github.pulsebeat02.urbandictionaryplugin.command.UrbanDictionaryCommand;
import io.github.pulsebeat02.urbandictionaryplugin.message.Locale;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class UrbanDictionaryPlugin extends JavaPlugin {

  private CacheManager cache;
  private BukkitAudiences audiences;
  private Audience console;

  @Override
  public void onEnable() {
    this.registerAudiences();
    this.console.sendMessage(Locale.PLUGIN_INIT.build());
    this.registerCommands();
    this.setupCache();
    this.console.sendMessage(Locale.PLUGIN_FINISH_INIT.build());
  }

  private void setupCache() {
    this.cache = new CacheManager();
  }

  private void registerAudiences() {
    this.audiences = BukkitAudiences.create(this);
    this.console = this.audiences.console();
  }

  private void registerCommands() {
    final UrbanDictionaryCommand command = new UrbanDictionaryCommand(this);
    final PluginCommand urban = requireNonNull(this.getCommand("urban"));
    urban.setExecutor(command);
  }

  @Override
  public void onDisable() {
    this.console.sendMessage(Locale.PLUGIN_SHUTDOWN.build());
    this.console.sendMessage(Locale.PLUGIN_FINISH_SHUTDOWN.build());
    this.cache.shutdown();
  }

  public @NotNull BukkitAudiences getAudiences() {
    return this.audiences;
  }

  public @NotNull CacheManager getCache() {
    return this.cache;
  }

  public @NotNull Audience getConsole() {
    return this.console;
  }
}
