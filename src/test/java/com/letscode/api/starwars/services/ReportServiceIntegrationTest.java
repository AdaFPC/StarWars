package com.letscode.api.starwars.services;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.javafaker.Faker;
import com.letscode.api.starwars.domains.Inventory;
import com.letscode.api.starwars.domains.Location;
import com.letscode.api.starwars.domains.Rebel;
import com.letscode.api.starwars.domains.enums.Gender;
import com.letscode.api.starwars.domains.enums.InventoryItems;
import com.letscode.api.starwars.repository.RebelRepository;
import com.letscode.api.starwars.utils.TradeUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Integrated Test :: Report Service")
class ReportServiceIntegrationTest {

  @Autowired
  private ReportService service;

  @Autowired
  private RebelRepository repository;
  private final Faker faker = new Faker();
  private final Random random = new Random();

  @Test
  @DisplayName("Half of my base is traitor")
  void shouldHaveHalfSiths() {
    repository.saveAll(List.of(buildRandomRebel(0), buildRandomRebel(4)));
    assertEquals(50.0, service.getTraitorPercentage());
  }

  @Test
  @DisplayName("Half of my base is rebel")
  void shouldHaveHalfRebels() {
    repository.saveAll(List.of(buildRandomRebel(0), buildRandomRebel(4)));
    assertEquals(50.0, service.getRebelsPercentage());
  }

  @Test
  @DisplayName("Resource Average")
  void shouldHaveAnAveragePercentageOfResource() {

    Rebel rebel = buildRandomRebel(0);
    repository.save(rebel);

    assertEquals((double) rebel.getInventory().getWater(), service.getAverageResourcePerRebel(InventoryItems.WATER));
    assertEquals((double) rebel.getInventory().getFood(), service.getAverageResourcePerRebel(InventoryItems.FOOD));
    assertEquals((double) rebel.getInventory().getWeapon(), service.getAverageResourcePerRebel(InventoryItems.WEAPON));
    assertEquals((double) rebel.getInventory().getAmmo(), service.getAverageResourcePerRebel(InventoryItems.AMMO));

  }

  @Test
  @DisplayName("Sith won a few")
  void shouldHaveLostAFewToTraitors() {

    Rebel rebel = buildRandomRebel(0);
    Rebel sith = buildRandomRebel(4);
    repository.saveAll(List.of(rebel, sith));

    assertEquals(
        TradeUtils.getTradeValue(sith.getInventory()),
        service.getLossesToTraitors().getTotalLost()
    );

  }

  @AfterEach
  public void tearDown() {
    repository.deleteAll();
  }

  private Rebel buildRandomRebel(int darknesLevel) {
    String name = faker.starTrek().villain();
    name = name.length() > 120 ? name.substring(0, 120) : name;
    name = name.length() < 5 ? name + "Lerp" : name;
    return Rebel
        .builder()
        .name(name)
        .gender(Gender.UNKNOWN)
        .age(random.nextInt(75) + 1)
        .inventory(
            Inventory
                .builder()
                .ammo(random.nextInt(10))
                .food(random.nextInt(10))
                .water(random.nextInt(10))
                .weapon(random.nextInt(10))
                .build()
        )
        .location(buildLocation())
        .reportCounter(darknesLevel)
        .build();
  }

  private Location buildLocation() {
    return Location
        .builder()
        .latitude(Float.parseFloat(faker.address().latitude().replace(",",".")))
        .longitude(Float.parseFloat(faker.address().longitude().replace(",",".")))
        .name(faker.starTrek().location())
        .build();
  }

}
