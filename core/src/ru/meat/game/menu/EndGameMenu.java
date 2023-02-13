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

  final MyGame game;
  private Table table;

  private Button againButton;

  private Button toMenuButton;

  private final int selectedMap;

  public EndGameMenu(MyGame game, int rewardPoints, int map, LocalDateTime fromDateTime, int kills) {
    this.game = game;
    game.initStage();

    createButtons();

    selectedMap = map;

    table = new Table();
    table.setDebug(DEBUG);
    table.setSize(300, 300);
    table.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);

    Label killsLabel = new Label("kills", game.getLabelStyle());
    killsLabel.setAlignment(Align.center);
    Label labelCount = new Label(String.valueOf(kills), game.getLabelStyle());
    labelCount.setAlignment(Align.center);
    table.add(killsLabel).width(100).height(30);
    table.add(labelCount).width(100).height(30);
    table.row();

    Label pointsLabel = new Label("points", game.getLabelStyle());
    pointsLabel.setAlignment(Align.center);
    Label pointsLabel2 = new Label(String.valueOf(rewardPoints), game.getLabelStyle());
    pointsLabel2.setAlignment(Align.center);
    table.add(pointsLabel).width(100).height(30);
    table.add(pointsLabel2).width(100).height(30);
    table.row();

    Label timeLabel = new Label("time", game.getLabelStyle());
    timeLabel.setAlignment(Align.center);
    Label timeLabel2 = new Label(getTotalGameTime(fromDateTime), game.getLabelStyle());
    timeLabel2.setAlignment(Align.center);
    table.add(timeLabel).width(100).height(30);
    table.add(timeLabel2).width(100).height(30);
    table.row();

    table.add(againButton).width(100).height(30);
    table.add(toMenuButton).width(100).height(30);
    game.getStage().addActor(table);
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

    againButton = createButton(game.getTextButtonStyle(), "Again", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(new DeathMatch(selectedMap, game));
        AudioService.getInstance().initSteps();
        AudioService.getInstance().smoothStopMusic();
        System.out.println("here");
      }
    });

    toMenuButton = createButton(game.getTextButtonStyle(), "Menu", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(new MainMenu(game));
        System.out.println("here");
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

    game.drawStage();
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
