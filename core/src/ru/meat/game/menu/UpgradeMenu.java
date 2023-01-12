package ru.meat.game.menu;


import static ru.meat.game.settings.Constants.DEBUG;
import static ru.meat.game.settings.Constants.LVL_EXP_STEP;
import static ru.meat.game.utils.GDXUtils.createButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import ru.meat.game.MyGame;
import ru.meat.game.service.RpgStatsService;

public class UpgradeMenu implements Screen {

  final MyGame game;
  private Table table;

  private Label lvlLabel;
  private Label experience;

  private Label newLvlExperience;

  private Label hpLabel;
  private Button hpButton;
  private Label dmgLabel;
  private Button dmgButton;
  private Label moveSpeedLabel;
  private Button moveSpeedButton;
  private Label reloadSpeedLabel;
  private Button reloadSpeedButton;

  private Button saveButton;
  private Button backButton;
  private Label fireSpeedLabel;
  private Button fireSpeedButton;

  private long lvl;


  public UpgradeMenu(final MyGame game) {
    this.game = game;

    game.initStage();

    lvl = RpgStatsService.getInstance().getStats().getLvl();

    createButtons();
    createLabels();

    table = new Table();
    table.setSize(300, 300);
    table.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);
    table.setDebug(DEBUG);

    table.row();
    Label label = new Label("LvL", game.getLabelStyle());
    label.setAlignment(Align.center);
    table.add(label).width(100).height(30);
    table.add(lvlLabel).width(100).height(30).align(Align.center);

    table.row();
    table.add(new Label("Exp", game.getLabelStyle()));
    table.add(experience);
    table.add(newLvlExperience);

    table.row();
    table.add(new Label("HP", game.getLabelStyle())).height(30);
    table.add(hpLabel);
    table.add(hpButton);

    table.row();
    table.add(new Label("Dmg", game.getLabelStyle())).height(30);
    table.add(dmgLabel);
    table.add(dmgButton);

    table.row();
    table.add(new Label("Move speed", game.getLabelStyle())).height(30);
    table.add(moveSpeedLabel);
    table.add(moveSpeedButton);

    table.row();
    table.add(new Label("Fire speed", game.getLabelStyle())).height(30);
    table.add(fireSpeedLabel);
    table.add(fireSpeedButton);

    table.row();
    table.add(new Label("Reloading", game.getLabelStyle())).height(30);
    table.add(reloadSpeedLabel);
    table.add(reloadSpeedButton);

    table.row();
    table.add(saveButton).height(30);
    table.add();
    table.add(backButton);

    game.getStage().addActor(table);
  }

  private void createLabels() {
    hpLabel = new Label(String.format("%.0f", RpgStatsService.getInstance().getStats().getHp()), game.getLabelStyle());
    dmgLabel = new Label(String.valueOf(RpgStatsService.getInstance().getStats().getDamage()), game.getLabelStyle());
    moveSpeedLabel = new Label(String.valueOf(RpgStatsService.getInstance().getStats().getMoveSpeed()),
        game.getLabelStyle());
    fireSpeedLabel = new Label(String.valueOf(RpgStatsService.getInstance().getStats().getFireSpeed()),
        game.getLabelStyle());
    reloadSpeedLabel = new Label(String.valueOf(RpgStatsService.getInstance().getStats().getReloadSpeed()),
        game.getLabelStyle());

    newLvlExperience = new Label(String.valueOf(lvl * LVL_EXP_STEP), game.getLabelStyle());
    experience = new Label(String.valueOf(RpgStatsService.getInstance().getStats().getExperience()),
        game.getLabelStyle());

    lvlLabel = new Label(String.valueOf(RpgStatsService.getInstance().getStats().getLvl()), game.getLabelStyle());
    lvlLabel.setAlignment(Align.center);
  }

  private void createButtons() {

    hpButton = createButton(game.getTextButtonStyle(), ">>", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        hpLabel.setText(String.format("%.0f", Double.parseDouble(hpLabel.getText().toString()) + 1));
        addLevel();
      }
    });

    dmgButton = createButton(game.getTextButtonStyle(), ">>", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        dmgLabel.setText(String.valueOf(new BigDecimal(
            (Double.parseDouble(dmgLabel.getText().toString()) + 0.001))
            .setScale(3, RoundingMode.HALF_UP)));
        addLevel();
      }
    });

    moveSpeedButton = createButton(game.getTextButtonStyle(), ">>", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        moveSpeedLabel.setText(String.valueOf(new BigDecimal(
            (Double.parseDouble(moveSpeedLabel.getText().toString()) + 0.001))
            .setScale(3, RoundingMode.HALF_UP)));
        addLevel();
      }
    });

    fireSpeedButton = createButton(game.getTextButtonStyle(), ">>", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        fireSpeedLabel.setText(String.valueOf(new BigDecimal(
            (Double.parseDouble(fireSpeedLabel.getText().toString()) + 0.001))
            .setScale(3, RoundingMode.HALF_UP)));
        addLevel();
      }
    });

    reloadSpeedButton = createButton(game.getTextButtonStyle(), ">>", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        reloadSpeedLabel.setText(String.valueOf(new BigDecimal(
            (Double.parseDouble(reloadSpeedLabel.getText().toString()) + 0.001))
            .setScale(3, RoundingMode.HALF_UP)));
        addLevel();
      }
    });

    saveButton = createButton(game.getTextButtonStyle(), "Save",
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            RpgStatsService.getInstance().getStats().setHp(Double.parseDouble(hpLabel.getText().toString()));
            RpgStatsService.getInstance().getStats().setDamage(Float.parseFloat(dmgLabel.getText().toString()));
            RpgStatsService.getInstance().getStats()
                .setMoveSpeed(Float.parseFloat(moveSpeedLabel.getText().toString()));
            RpgStatsService.getInstance().getStats()
                .setFireSpeed(Float.parseFloat(fireSpeedLabel.getText().toString()));
            RpgStatsService.getInstance().getStats()
                .setReloadSpeed(Float.parseFloat(reloadSpeedLabel.getText().toString()));
            RpgStatsService.getInstance().getStats().setExperience(Long.parseLong(experience.getText().toString()));
            RpgStatsService.getInstance().getStats().setLvl(lvl);
            RpgStatsService.getInstance().saveStats();
            game.setScreen(new MainMenu(game));
          }
        }
    );

    backButton = createButton(game.getTextButtonStyle(), "Back",
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            game.setScreen(new MainMenu(game));
          }
        }
    );
  }

  private void addLevel() {
    BigInteger subtract = new BigInteger(experience.getText().toString()).subtract(
        new BigInteger(newLvlExperience.getText().toString()));
    experience.setText(subtract.toString());

    lvl += 1;
    lvlLabel.setText(String.valueOf(lvl));
    newLvlExperience.setText(String.valueOf(lvl * LVL_EXP_STEP));
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1);

    setEnableIncreaseStats(
        Long.parseLong(experience.getText().toString()) > Long.parseLong(newLvlExperience.getText().toString()));

    game.drawStage();
  }

  private void setEnableIncreaseStats(boolean b) {
    reloadSpeedButton.setVisible(b);
    moveSpeedButton.setVisible(b);
    hpButton.setVisible(b);
    fireSpeedButton.setVisible(b);
    dmgButton.setVisible(b);
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
