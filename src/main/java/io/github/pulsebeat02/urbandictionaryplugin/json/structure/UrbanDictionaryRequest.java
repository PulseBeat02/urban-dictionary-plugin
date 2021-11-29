package io.github.pulsebeat02.urbandictionaryplugin.json.structure;

import io.github.pulsebeat02.urbandictionaryplugin.json.GsonProvider;

public final class UrbanDictionaryRequest {

  private Definition[] list;

  public Definition[] getList() {
    return this.list;
  }

  public static UrbanDictionaryRequest parseUrbanDictionaryRequest(final String json) {
    return GsonProvider.getGson().fromJson(json, UrbanDictionaryRequest.class);
  }
}
