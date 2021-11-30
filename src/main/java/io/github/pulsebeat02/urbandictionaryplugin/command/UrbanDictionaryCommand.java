package io.github.pulsebeat02.urbandictionaryplugin.command;

import io.github.pulsebeat02.urbandictionaryplugin.UrbanDictionaryPlugin;
import io.github.pulsebeat02.urbandictionaryplugin.json.structure.UrbanDictionaryRequest;
import io.github.pulsebeat02.urbandictionaryplugin.message.Locale;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class UrbanDictionaryCommand implements CommandExecutor, TabCompleter {

  private static final Executor REQUEST_EXECUTOR;

  static {
    REQUEST_EXECUTOR = Executors.newWorkStealingPool();
  }

  private final UrbanDictionaryPlugin plugin;

  public UrbanDictionaryCommand(@NotNull final UrbanDictionaryPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
      @NotNull final String label,
      @NotNull final String[] args) {

    if (!this.hasPermission(sender)) {
      return true;
    }

    if (!this.checkArguments(sender, args)) {
      return true;
    }

    this.handleResult(sender, args);

    return true;
  }

  private void handleResult(@NotNull final CommandSender sender, @NotNull final String[] args) {
    CompletableFuture.runAsync(() -> this.constructMessage(sender, args), REQUEST_EXECUTOR)
        .whenComplete((msg, exception) -> exception.printStackTrace());
  }

  private void constructMessage(@NotNull final CommandSender sender, @NotNull final String[] args) {
    final Optional<Integer> optional = this.hasIndex(args);
    final String[] query = optional.isPresent() ? this.excludeLastArgument(args) : args;
    final int index = optional.orElse(0);
    final UrbanDictionaryRequest request = this.createRequest(query);
    this.plugin.getAudiences().sender(sender)
        .sendMessage(Locale.URBAN_DICTIONARY_MESSAGE.build(request, index));
  }


  private UrbanDictionaryRequest createRequest(@NotNull final String[] args) {
    final String result = this.getApiResponse(args);
    return UrbanDictionaryRequest.parseUrbanDictionaryRequest(result);
  }

  @Contract(value = "_ -> new", pure = true)
  private String @NotNull [] excludeLastArgument(@NotNull final String[] args) {
    return Arrays.copyOf(args, args.length - 1);
  }

  private @NotNull Optional<Integer> hasIndex(@NotNull final String @NotNull [] args) {
    try {
      if (args.length != 1) {
        return Optional.of(Integer.parseInt(args[args.length - 1]));
      }
    } catch (final NumberFormatException e) {
    }
    return Optional.empty();
  }

  private @NotNull String getApiResponse(@NotNull final String[] query) {
    return this.plugin.getCache().getEntry(String.join("-", query));
  }

  private boolean checkArguments(@NotNull final CommandSender sender,
      final String @NotNull [] arguments) {
    if (arguments.length == 0) {
      this.plugin.getAudiences().sender(sender).sendMessage(Locale.INSUFFICIANT_ARGUMENTS.build());
      return false;
    }
    return true;
  }

  private boolean hasPermission(@NotNull final CommandSender sender) {
    if (!sender.hasPermission("urbandictionaryplugin.urban")) {
      this.plugin.getAudiences().sender(sender)
          .sendMessage(Locale.INSUFFICIANT_PERMISSIONS.build());
      return false;
    }
    return true;
  }

  @Contract(pure = true)
  @Override
  public @NotNull @Unmodifiable List<String> onTabComplete(
      @NotNull final CommandSender sender,
      @NotNull final Command command,
      @NotNull final String alias,
      @NotNull final String @NotNull [] args) {
    return List.of(args.length == 0 ? "[some word here]" : "[optional definition index]");
  }
}
