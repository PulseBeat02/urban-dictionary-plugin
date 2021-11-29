package io.github.pulsebeat02.urbandictionaryplugin.json;

import com.google.gson.Gson;

public final class GsonProvider {

  private static final Gson GSON;

  static {
    GSON = new Gson();
  }

  private GsonProvider() {}

  public static Gson getGson() {
    return GSON;
  }
}
