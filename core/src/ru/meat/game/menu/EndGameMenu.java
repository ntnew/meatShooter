package ru.meat.game.menu;

import static ru.meat.game.settings.Constants.DEBUG;
import static ru.meat.game.utils.GDXUtils.createButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.Data;
import ru.meat.game.MyGame;
import ru.meat.game.game.DeathMatch;
import ru.meat.game.service.AudioService;

@Data
public class EndGameMenu implements Screen {

  private Table table;

  private Button againButton;

  private Button toMenuButton;

  private final int selectedMap;

  public EndGameMenu( int rewardPoints, int map, LocalDateTime fromDateTime, int kills) {

    MyGame.getInstance().initStage();

    createButtons();

    selectedMap = map;

    table = new Table();
    table.setDebug(DEBUG);
    table.setSize(300, 300);
    table.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);

    Label killsLabel = new Label("kills", MyGame.getInstance().getLabelStyle());
    killsLabel.setAlignment(Align.center);
    Label labelCount = new Label(String.valueOf(kills), MyGame.getInstance().getLabelStyle());
    labelCount.setAlignment(Align.center);
    table.add(killsLabel).width(100).height(30);
    table.add(labelCount).width(100).height(30);
    table.row();

    Label pointsLabel = new Label("points", MyGame.getInstance().getLabelStyle());
    pointsLabel.setAlignment(Align.center);
    Label pointsLabel2 = new Label(String.valueOf(rewardPoints), MyGame.getInstance().getLabelStyle());
    pointsLabel2.setAlignment(Align.center);
    table.add(pointsLabel).width(100).height(30);
    table.add(pointsLabel2).width(100).height(30);
    table.row();

    Label timeLabel = new Label("time", MyGame.getInstance().getLabelStyle());
    timeLabel.setAlignment(Align.center);
    Label timeLabel2 = new Label(getTotalGameTime(fromDateTime), MyGame.getInstance().getLabelStyle());
    timeLabel2.setAlignment(Align.center);
    table.add(timeLabel).width(100).height(30);
    table.add(timeLabel2).width(100).height(30);
    table.row();

    table.add(againButton).width(100).height(30);
    table.add(toMenuButton).width(100).height(30);
    MyGame.getInstance().addActor(table);
  }

  private String getTotalGameTime(LocalDateTime fromDateTime) {
    LocalDateTime toDateTime = LocalDateTime.now();
    LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);
    long years = tempDateTime.until(toDateTime, ChronoUnit.YEARS);
    tempDateTime = tempDateTime.plusYears(years);

    long months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS);
    tempDateTime = tempDateTime.plusMonths(months);

    long days = tempDateTime.until(toDateTime, ChronoUnit.DAYS);
    tempDateTime = tempDateTime.plusDays(days);

    long hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS);
    tempDateTime = tempDateTime.plusHours(hours);

    long minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES);
    tempDateTime = tempDateTime.plusMinutes(minutes);

    long seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS);

    StringBuilder builder = new StringBuilder();
    if (hours > 0) {
      builder.append(hours).append(":");
    }
    builder.append(minutes).append(":").append(seconds);
    return builder.toString();
  }

  private void createButtons() {

    againButton = createButton(MyGame.getInstance().getTextButtonStyle(), "Again", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        MyGame.getInstance().setScreen(new DeathMatch(selectedMap));
        AudioService.getInstance().initSteps();
        AudioService.getInstance().smoothStopMusic();
      }
    });

    toMenuButton = createButton(MyGame.getInstance().getTextButtonStyle(), "Menu", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        MyGame.getInstance().setScreen(new MainMenu());
      }
    });
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    AudioService.getInstance().playMainMenuMusic();
    ScreenUtils.clear(0, 0, 0, 1);

    MyGame.getInstance().drawStage();
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {

  }
}
