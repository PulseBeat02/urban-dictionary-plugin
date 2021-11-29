package io.github.pulsebeat02.urbandictionaryplugin;

import io.github.pulsebeat02.urbandictionaryplugin.json.structure.UrbanDictionaryRequest;
import io.github.pulsebeat02.urbandictionaryplugin.utils.RequestUtils;
import java.io.IOException;
import java.net.URISyntaxException;

public final class RequestTest {

  public static void main(final String[] args)
      throws URISyntaxException, IOException, InterruptedException {
    final String response = RequestUtils.getResponse("https://api.urbandictionary.com/v0/define?term=lol");
    final UrbanDictionaryRequest request = UrbanDictionaryRequest.parseUrbanDictionaryRequest(response);
    System.out.println(request.getList()[0].getDefinition());
  }

}
