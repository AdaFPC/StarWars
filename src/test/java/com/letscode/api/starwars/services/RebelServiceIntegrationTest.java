package com.letscode.api.starwars.services;

import java.util.Random;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.javafaker.Faker;
import com.letscode.api.starwars.domains.Inventory;
import com.letscode.api.starwars.domains.Location;
import com.letscode.api.starwars.domains.Rebel;
import com.letscode.api.starwars.domains.enums.Gender;
import com.letscode.api.starwars.exception.RebelNotFoundException;
import com.letscode.api.starwars.models.RebelDTO;
import com.letscode.api.starwars.repository.RebelRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Integrated Test :: Rebel Service")
class RebelServiceIntegrationTest {

  @Autowired
  private RebelService service;

  @Autowired
  private RebelRepository repository;

  private final Faker faker = new Faker();
  private final Random random = new Random();

  @Test
  @DisplayName("Fail when Rebel is invalid")
  void shouldNotSaveInvalidRebel() {
    assertThrows(ConstraintViolationException.class, () -> service.add(RebelDTO.builder().build()));
  }

  @Test
  @DisplayName("Save a Rebel")
  void shouldSaveValidRebel() {
    assertThat(service.add(buildRandomRebel()))
        .isNotNull()
        .isNotNegative()
        .isPositive();
  }

  @Test
  @DisplayName("List paginated")
  void shouldListPaginated() {

    for (int counter = 0; counter < 10; counter++) {
      service.add(buildRandomRebel());
    }

    Page<Rebel> rebelPage = service.getAll(PageRequest.of(0, 20, Sort.unsorted()));
    assertThat(rebelPage)
        .isNotNull()
        .isInstanceOf(Page.class)
        .hasNoNullFieldsOrProperties()
        .matches(Slice::hasContent)
        .matches(rebelPagePredicate -> rebelPagePredicate.getTotalElements() >= 10)
        .matches(rebelPagePredicate -> rebelPagePredicate.getNumberOfElements() == rebelPagePredicate.getContent().size())
        .matches(rebelPagePredicate -> rebelPagePredicate.getPageable() != null)
        .matches(rebelPagePredicate -> rebelPagePredicate.getPageable().getPageNumber() == 0);

  }

  @Test
  @DisplayName("List paginated passing size and page")
  void shouldListPaginatedWithSizeAndPage() {

    for (int counter = 0; counter < 10; counter++) {
      service.add(buildRandomRebel());
    }

    Page<Rebel> rebelPage = service.getAll(PageRequest.of(1, 1));
    assertThat(rebelPage)
        .isNotNull()
        .isInstanceOf(Page.class)
        .hasNoNullFieldsOrProperties()
        .matches(Slice::hasContent)
        .matches(rebelPagePredicate -> rebelPagePredicate.getTotalElements() >= 10)
        .matches(rebelPagePredicate -> rebelPagePredicate.getNumberOfElements() == 1)
        .matches(rebelPagePredicate -> rebelPagePredicate.getContent().size() == 1)
        .matches(rebelPagePredicate -> rebelPagePredicate.getPageable() != null)
        .matches(rebelPagePredicate -> rebelPagePredicate.getPageable().getPageNumber() == 1)
        .matches(rebelPagePredicate -> rebelPagePredicate.getPageable().getPageSize() == 1);

  }

  @Test
  @DisplayName("Get rebel by ID")
  void shouldFindRebelById() {
    RebelDTO rebel = buildRandomRebel();
    Long id = service.add(rebel);

    assertThat(service.getRebel(id))
        .isNotNull()
        .isNotEmpty();
  }

  @Test
  @DisplayName("No Rebel with that ID")
  void shouldNotFindRebel() {
    assertThat(service.getRebel(375L))
        .isNotNull()
        .isEmpty();
  }

  @Test
  @DisplayName("Updating Location")
  void shouldUpdateLocationOfRebel() {
    Long id = service.add(buildRandomRebel());
    Location newLocation = buildLocation();
    service.updateLocation(id, newLocation);

    assertThat(service.getRebel(id))
        .isNotNull()
        .isNotEmpty()
        .matches(r -> r.get().getLocation().equals(newLocation));
  }

  @Test
  @DisplayName("Updating Location? Sorry")
  void shouldNotUpdateLocationOfNoRebel() {
    assertThrows(RebelNotFoundException.class, () -> service.updateLocation(399L, buildLocation()));
  }

  @Test
  @DisplayName("TR8TR")
  void shouldReportRebelAsTraitor() {
    Long id = service.add(buildRandomRebel());
    service.reportAsTraitor(id);

    assertThat(service.getRebel(id))
        .isNotNull()
        .isNotEmpty()
        .matches(r -> r.get().getReportCounter() == 1);
  }

  @Test
  @DisplayName("No treason if doesn't exist")
  void shouldNotReportTraitorForUnexistingRebel() {
    assertThrows(RebelNotFoundException.class, () -> service.reportAsTraitor(399L));
  }

  @AfterEach
  public void tearDown() {
    repository.deleteAll();
  }

  private RebelDTO buildRandomRebel() {
    String name = faker.starTrek().villain();
    name = name.length() > 120 ? name.substring(0, 120) : name;
    name = name.length() < 5 ? name + "Lerp" : name;
    return RebelDTO
        .builder()
        .name(name)
        .gender(Gender.UNKNOWN)
        .age(random.nextInt(75) + 1)
        .inventory(
            Inventory
                .builder()
                .ammo(random.nextInt())
                .food(random.nextInt())
                .water(random.nextInt())
                .weapon(random.nextInt())
                .build()
        )
        .location(buildLocation())
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
