package io.github.pulsebeat02.urbandictionaryplugin.json.structure;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

public final class Definition {

  @SerializedName("defid")
  private String id;

  @SerializedName("sound_urls")
  private String[] soundUrls;

  @SerializedName("thumbs_down")
  private String thumbsDown;

  @SerializedName("author")
  private String author;

  @SerializedName("written_on")
  private String dateWritten;

  @SerializedName("definition")
  private String definition;

  @SerializedName("permalink")
  private String permalink;

  @SerializedName("thumbs_up")
  private String thumbsUp;

  @SerializedName("word")
  private String word;

  @SerializedName("current_vote")
  private String currentVote;

  @SerializedName("example")
  private String example;

  public @NotNull String getId() {
    return this.id;
  }

  public @NotNull String[] getSoundUrls() {
    return this.soundUrls;
  }

  public @NotNull String getThumbsDown() {
    return this.thumbsDown;
  }

  public @NotNull String getAuthor() {
    return this.author;
  }

  public String getDateWritten() {
    return this.dateWritten;
  }

  public String getDefinition() {
    return this.definition;
  }

  public String getPermalink() {
    return this.permalink;
  }

  public String getThumbsUp() {
    return this.thumbsUp;
  }

  public String getWord() {
    return this.word;
  }

  public String getCurrentVote() {
    return this.currentVote;
  }

  public String getExample() {
    return this.example;
  }
}
