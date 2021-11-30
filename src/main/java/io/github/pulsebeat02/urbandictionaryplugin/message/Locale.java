package io.github.pulsebeat02.urbandictionaryplugin.message;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.JoinConfiguration.separator;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

import io.github.pulsebeat02.urbandictionaryplugin.json.structure.Definition;
import io.github.pulsebeat02.urbandictionaryplugin.json.structure.UrbanDictionaryRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

public interface Locale {

  NullComponent<Sender> PLUGIN_INIT = () ->
      text()
          .color(GOLD)
          .append(text("Urban Dictionary Plugin is loading... - Version v1.0.0"))
          .build();

  NullComponent<Sender> PLUGIN_FINISH_INIT = () ->
      text().color(GOLD)
          .append(text("Urban Dictionary Plugin finished loading!"))
          .build();

  NullComponent<Sender> PLUGIN_SHUTDOWN = () ->
      text()
          .color(GOLD)
          .append(text("Urban Dictionary Plugin is shutting down..."))
          .build();

  NullComponent<Sender> PLUGIN_FINISH_SHUTDOWN = () ->
      text()
          .color(GOLD)
          .append(text("Urban Dictionary Plugin finished shutting down!"))
          .build();

  NullComponent<Sender> PLUGIN_PREFIX = () ->
      text()
          .color(AQUA)
          .append(
              text('['), text("Urban Dictionary Plugin", GOLD), text(']'), space(), text("»", GRAY))
          .build();

  NullComponent<Sender> INSUFFICIANT_PERMISSIONS = () ->
      text()
          .color(RED)
          .append(text("You do not have sufficient permissions to execute this command!"))
          .build();

  NullComponent<Sender> INSUFFICIANT_ARGUMENTS = () ->
      text()
          .color(RED)
          .append(text(
              "You must specify at least one argument for the word or phrase you choose! (/urban [phrase with spaces] [optional index]"))
          .build();

  NullComponent<Sender> INVALID_WORD = () -> text("The word you put in is not on Urban Dictionary!",
      RED);

  NullComponent<Sender> HIGH_INDEX_DEFINITION = () -> text("There are not as many definitions!",
      RED);

  NullComponent<Sender> LOW_INDEX_DEFINITION = () -> text(
      "The definition index cannot be negative!", RED);

  String URBAN_CMD = "/urban %s %s";

  BiComponent<Sender, UrbanDictionaryRequest, Integer> URBAN_DICTIONARY_MESSAGE = (request, index) -> {

    final Definition[] definitions = request.getList();

    if (definitions.length == 0) {
      return INVALID_WORD.build();
    } else if (index >= definitions.length) {
      return HIGH_INDEX_DEFINITION.build();
    } else if (index <= -1) {
      return LOW_INDEX_DEFINITION.build();
    }

    final Definition definition = request.getList()[index];
    final String word = definition.getWord();
    final String author = definition.getAuthor();
    final String date = definition.getDateWritten();
    final String thumbsUp = definition.getThumbsUp();
    final String thumbsDown = definition.getThumbsDown();
    final String text = definition.getDefinition();
    final Style style = style(GOLD, BOLD);

    return join(separator(newline()),
        join(separator(space()), text("Word", style), text(word, AQUA)),
        join(separator(space()), text("Author", style), text(author, AQUA)),
        join(separator(space()), text("Date", style), text(date, AQUA)),
        join(separator(space()), text("Rating", style),
            createRatingComponent(thumbsUp, thumbsDown)),
        join(separator(space()), text("Definition", style)),
        createDefinitionComponent(text),
        empty(),
        join(separator(space()),
            createPreviousDefinition(word, index),
            createNextDefinition(word, index))
    );
  };

  static @NotNull Component createNextDefinition(@NotNull final String word, final int index) {
    return text("[Next Definition]",
        style(GOLD, BOLD, text("Click to view the next definition", GOLD).asHoverEvent(),
            runCommand(URBAN_CMD.formatted(word, index + 1))));
  }

  static @NotNull Component createPreviousDefinition(@NotNull final String word, final int index) {
    return text("[Previous Definition]",
        style(GOLD, BOLD, text("Click to view the previous definition", GOLD).asHoverEvent(),
            runCommand(URBAN_CMD.formatted(word, index - 1))));
  }

  static @NotNull Component createRatingComponent(@NotNull final String thumbsUp,
      @NotNull final String thumbsDown) {
    return join(separator(space()), text(thumbsUp, GREEN), text("(↑)", GREEN), text("-", GOLD),
        text(thumbsDown, RED),
        text("(↓)", RED));
  }

  static @NotNull Component createDefinitionComponent(@NotNull final String definition) {
    final Builder component = text();
    final char[] characters = definition.toCharArray();
    boolean start = false;
    int open = -1;
    for (int i = 0; i < characters.length; i++) {
      final char character = characters[i];
      if (character == '[') {
        open = i + 1;
        start = true;
      } else if (character == ']') {
        final String text = definition.substring(open, i);
        component.append(text(text,
            style(GOLD, BOLD, runCommand(URBAN_CMD.formatted(text, 0)),
                text("Click to view the definition of %s".formatted(text), GOLD).asHoverEvent())));
        start = false;
      } else if (!start) {
        component.append(text(character, AQUA));
      }
    }
    return component.build();
  }


  static @NotNull Component format(@NotNull final Component message) {
    return join(separator(space()), PLUGIN_PREFIX.build(), message);
  }

  @FunctionalInterface
  interface NullComponent<S extends Sender> {

    Component build();

    default void send(@NotNull final S sender) {
      sender.sendMessage(format(this.build()));
    }
  }

  @FunctionalInterface
  interface UniComponent<S extends Sender, A0> {

    Component build(A0 arg0);

    default void send(@NotNull final S sender, @NotNull final A0 arg0) {
      sender.sendMessage(format(this.build(arg0)));
    }
  }

  @FunctionalInterface
  interface BiComponent<S extends Sender, A0, A1> {

    Component build(A0 arg0, A1 arg1);

    default void send(@NotNull final S sender, @NotNull final A0 arg0, @NotNull final A1 arg1) {
      sender.sendMessage(format(this.build(arg0, arg1)));
    }
  }

  @FunctionalInterface
  interface TriComponent<S extends Sender, A0, A1, A2> {

    Component build(A0 arg0, A1 arg1, A2 arg2);

    default void send(
        @NotNull final S sender,
        @NotNull final A0 arg0,
        @NotNull final A1 arg1,
        @NotNull final A2 arg2) {
      sender.sendMessage(format(this.build(arg0, arg1, arg2)));
    }
  }

  @FunctionalInterface
  interface QuadComponent<S extends Sender, A0, A1, A2, A3> {

    Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3);

    default void send(
        @NotNull final S sender,
        @NotNull final A0 arg0,
        @NotNull final A1 arg1,
        @NotNull final A2 arg2,
        @NotNull final A3 arg3) {
      sender.sendMessage(format(this.build(arg0, arg1, arg2, arg3)));
    }
  }

  @FunctionalInterface
  interface PentaComponent<S extends Sender, A0, A1, A2, A3, A4> {

    Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4);

    default void send(
        @NotNull final S sender,
        @NotNull final A0 arg0,
        @NotNull final A1 arg1,
        @NotNull final A2 arg2,
        @NotNull final A3 arg3,
        @NotNull final A4 arg4) {
      sender.sendMessage(format(this.build(arg0, arg1, arg2, arg3, arg4)));
    }
  }

  @FunctionalInterface
  interface HexaComponent<S extends Sender, A0, A1, A2, A3, A4, A5> {

    Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5);

    default void send(
        @NotNull final S sender,
        @NotNull final A0 arg0,
        @NotNull final A1 arg1,
        @NotNull final A2 arg2,
        @NotNull final A3 arg3,
        @NotNull final A4 arg4,
        @NotNull final A5 arg5) {
      sender.sendMessage(format(this.build(arg0, arg1, arg2, arg3, arg4, arg5)));
    }
  }

}
