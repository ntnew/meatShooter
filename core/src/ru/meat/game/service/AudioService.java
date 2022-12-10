package ru.meat.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AudioService {

  public void playShoot(String shootSound) {
    Music music = Gdx.audio.newMusic(Gdx.files.internal(shootSound));
    music.setOnCompletionListener(Music::dispose);
    music.play();
  }
}
