package io.github.pulsebeat02.urbandictionaryplugin.message;

import net.kyori.adventure.text.Component;

public interface Sender {

  void sendMessage(final Component component);

  boolean hasPermission(final String permission);
}
