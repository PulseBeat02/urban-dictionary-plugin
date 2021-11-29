package io.github.pulsebeat02.urbandictionaryplugin.utils;

import static java.time.Duration.ofSeconds;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import org.jetbrains.annotations.NotNull;

public final class RequestUtils {

  private static final HttpClient HTTP_CLIENT;

  static {
    HTTP_CLIENT = HttpClient.newBuilder().connectTimeout(ofSeconds(10L)).build();
  }

  private RequestUtils() {
  }

  public static @NotNull String getResponse(@NotNull final String url)
      throws URISyntaxException, IOException, InterruptedException {
    return HTTP_CLIENT.send(createResponse(url), BodyHandlers.ofString()).body();
  }

  private static @NotNull HttpRequest createResponse(@NotNull final String url) throws URISyntaxException {
    return HttpRequest.newBuilder(new URI(url)).build();
  }
}
