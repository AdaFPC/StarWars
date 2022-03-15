package com.letscode.api.starwars.utils;

import com.letscode.api.starwars.Constants;
import com.letscode.api.starwars.domains.Inventory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TradeUtils {

  public static int getTradeValue(Inventory inventory) {
    if (inventory == null)
      return 0;
    return inventory.getFood() * Constants.FOOD_POINTS +
        inventory.getWater() * Constants.WATER_POINTS +
        inventory.getWeapon() * Constants.WEAPON_POINTS +
        inventory.getAmmo() * Constants.AMMO_POINTS;
  }

  public static Inventory add(Inventory target, Inventory trade) {
    if (target == null || trade == null)
      return target;
    return
        target
            .withWater(target.getWater() + trade.getWater())
            .withFood(target.getFood() + trade.getFood())
            .withAmmo(target.getAmmo() + trade.getAmmo())
            .withWeapon(target.getWeapon() + trade.getWeapon());
  }

  public static Inventory remove(Inventory target, Inventory trade) {
    if (target == null || trade == null)
      return target;
    return
        target
            .withWater(target.getWater() - trade.getWater())
            .withFood(target.getFood() - trade.getFood())
            .withAmmo(target.getAmmo() - trade.getAmmo())
            .withWeapon(target.getWeapon() - trade.getWeapon());
  }

  public static boolean canRemove(Inventory target, Inventory trade) {
    return
        (target.getWeapon() - trade.getWeapon()) >= 0
            &&
            (target.getWater() - trade.getWater()) >= 0
            &&
            (target.getFood() - trade.getFood()) >= 0
            &&
            (target.getAmmo() - trade.getAmmo()) >= 0;
  }

}
