package com.letscode.api.starwars.services;

import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.letscode.api.starwars.domains.Location;
import com.letscode.api.starwars.domains.Rebel;
import com.letscode.api.starwars.exception.RebelNotFoundException;
import com.letscode.api.starwars.models.RebelDTO;
import com.letscode.api.starwars.repository.RebelRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p><b>Rebel Management Service</b></p>
 * <p>Manages rebels through out the galaxy.</p>
 * This class will add, list and update rebel information in the galaxy far far away.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RebelService {

  private final RebelRepository repository;
  private final DozerBeanMapper mapper = new DozerBeanMapper();

  /**
   * <p><b>Add Rebel</b></p>
   * Add a new rebel and get it's ID back
   *
   * @param rebel An instance of {@link RebelDTO}
   * @return The ID of the {@link RebelDTO}
   */
  public Long add(RebelDTO rebel) {
    log.info("Adding a new rebel to the resistance {}", rebel);
    return repository.save(mapper.map(rebel,Rebel.class)).getId();
  }

  /**
   * <p><b>List a page of rebels</b></p>
   * Retrieve a single page of rebels.
   *
   * @param page A {@link Pageable} instance that can contain the number of the page, the size of the response and sorting information.
   * @return A single {@link Page} of {@link Rebel} entries.
   */
  public Page<Rebel> getAll(Pageable page) {
    log.info("Retrieving page {} of rebels from the rebel book", page);
    return repository.findAll(page);
  }

  /**
   * <p><b>Get a rebel by ID</b></p>
   * Look for a rebel based on it's ID
   *
   * @param id The ID of the rebel being requested
   * @return An {@link Optional} {@link Rebel} in case of found or an Empty result if not
   */
  public Optional<Rebel> getRebel(Long id) {
    log.info("Looking for a rebel with ID {}", id);
    return repository
        .findById(id)
        .map(rebel -> {
          log.info("Rebel with ID {} was found {}", id, rebel);
          return rebel;
        });
  }

  /**
   * <p><b>Update a rebel location</b></p>
   *
   * @param id       The ID of the rebel being updated
   * @param location A {@link Location} containing the new coordinates of the rebel
   * @return Returns the updated {@link Rebel}
   * @throws {@link RebelNotFoundException} in case of no such rebel being found with that particular ID
   */
  public Rebel updateLocation(Long id, Location location) {
    log.info("Updating location of rebel with ID {} as {}", id, location);
    return getRebel(id)
        .map(rebel -> rebel.withLocation(location))
        .map(repository::save)
        .orElseThrow(() -> new RebelNotFoundException(id));
  }

  /**
   * <p><b>Report as a traitor</b></p>
   * Report a rebel as a traitor.
   *
   * @param id The ID of the rebel being reported
   * @return Returns the saved {@link Rebel}
   * @throws {@link RebelNotFoundException} in case of no such rebel being found with that particular ID
   */
  public Rebel reportAsTraitor(Long id) {
    log.info("WARNING WARNING A rebel with ID {} was reported as a traitor", id);
    return
    getRebel(id)
        .map(rebel -> rebel.withReportCounter(rebel.getReportCounter() + 1))
        .map(repository::save)
        .map(rebel -> {
          log.info("{}", rebel.isTraitor() ? rebel.getName() + " is now a traitor" : rebel.getName() + " got " + rebel.getReportCounter() + " report(s) as traitor");
          return rebel;
        })
        .orElseThrow(() -> new RebelNotFoundException(id));
  }
}
