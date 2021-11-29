package io.github.pulsebeat02.urbandictionaryplugin.cache;

import static java.util.Objects.requireNonNull;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github.pulsebeat02.urbandictionaryplugin.utils.RequestUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public final class CacheManager {

  private static final String URBAN_DICTIONARY_URL;

  static {
    URBAN_DICTIONARY_URL = "https://api.urbandictionary.com/v0/define?term=%s";
  }

  private final LoadingCache<String, String> cache;
  private final ExecutorService executor;

  public CacheManager() {
    this.executor = Executors.newWorkStealingPool();
    this.cache = Caffeine.newBuilder()
        .executor(this.executor)
        .expireAfterAccess(1, TimeUnit.HOURS)
        .softValues()
        .build(this::getEntryInternal);
  }

  private @NotNull String getEntryInternal(@NotNull final String query) {
    try {
      return RequestUtils.getResponse(URBAN_DICTIONARY_URL.formatted(query));
    } catch (final URISyntaxException | IOException | InterruptedException e) {
      return "";
    }
  }

  public void shutdown() {
    this.cache.cleanUp();
    this.executor.shutdown();
  }

  public @NotNull String getEntry(@NotNull final String query) {
    return requireNonNull(this.cache.get(query));
  }
}
