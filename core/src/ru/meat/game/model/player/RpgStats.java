package ru.meat.game.model.player;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RpgStats {

  private double hp;

  private long experience;
  /**
   * увеличивает скорость бега
   * принимает значение от 1 (не изменяет перезарядку) 2 (увеличена в 2 раза)
   */
  private float moveSpeed;

  /**
   * уменьшает скорость перезарядки
   * принимает значение от 1 (не изменяет перезарядку) до 0.001 (уменьшена в 100 раз)
   */
  private float reloadSpeed;
  /**
   * увеличивает скорость стрельбы
   * принимает значение от 1 (не изменяет стрельбу) до 0.001 (уменьшена в 100 время без стрельбы)
   */
  private float fireSpeed;

  /**
   * увеличивает урон
   * принимает значение от 1 (не изменяет) 3 (увеличена в 3 раза)
   */
  private float damage;

  private float resist;
}
