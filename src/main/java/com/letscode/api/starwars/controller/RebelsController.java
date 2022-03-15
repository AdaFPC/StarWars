package com.letscode.api.starwars.controller;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.letscode.api.starwars.domains.Location;
import com.letscode.api.starwars.domains.Rebel;
import com.letscode.api.starwars.models.RebelDTO;
import com.letscode.api.starwars.services.RebelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rebels")
@Tag(name = "Rebels API", description = "The main rebel api")
public class RebelsController {

  private final RebelService rebelServices;

  @GetMapping
  @Operation(
      method = "GET",
      summary = "List Rebels",
      description = "Return a paginated list of rebels",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
      }
  )
  public ResponseEntity<Page<Rebel>> listRebels(
      @ParameterObject
      @PageableDefault(size = 20, sort = "id")
      Pageable page
  ) {
    return ResponseEntity.ok(rebelServices.getAll(page));
  }

  @PostMapping
  @Operation(
      method = "POST",
      summary = "Add Rebel",
      description = "Add a new rebel to the cause",
      responses = {
          @ApiResponse(responseCode = "201", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Missing rebel information")
      },
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The rebel to be added to our books",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(
                  implementation = RebelDTO.class,
                  example = "{\"name\":\"Robin Schuppe I\",\"age\":8,\"gender\":\"NONBINARY\",\"location\":{\"latitude\":-22.464636,\"longitude\":51.22044,\"name\":\"Quincy\"},\"inventory\":{\"water\":8,\"food\":4,\"ammo\":0,\"weapon\":5}}"
              )
          ),
          required = true
      )
  )
  public ResponseEntity<Void> addRebel(
      @io.swagger.v3.oas.annotations.parameters.RequestBody
      @Valid @RequestBody RebelDTO rebel,
      UriComponentsBuilder uriBuilder
  ) {
    return ResponseEntity.created(uriBuilder.replacePath("/api/rebels/{id}").buildAndExpand(rebelServices.add(rebel)).toUri()).build();
  }

  @GetMapping("/{id}")
  @Operation(
      method = "GET",
      summary = "Get Rebel Information",
      description = "Get the rebel information",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Rebel.class), examples = {@ExampleObject(value = "{\"id\":1,\"name\":\"Robin Schuppe I\",\"age\":8,\"gender\":\"NONBINARY\",\"location\":{\"latitude\":-22.464636,\"longitude\":51.22044,\"name\":\"Quincy\"},\"inventory\":{\"water\":8,\"food\":4,\"ammo\":0,\"weapon\":5}}")})),
          @ApiResponse(responseCode = "404", description = "Rebel not found")
      }
  )
  public ResponseEntity<Rebel> getRebel(
      @Parameter(name = "id", description = "Id of the rebel")
      @PathVariable("id") Long id
  ) {
    return rebelServices
        .getRebel(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @PostMapping("/{id}/location")
  @Operation(
      method = "POST",
      summary = "Update Location",
      description = "Update the current location of a rebel",
      responses = {
          @ApiResponse(responseCode = "202", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Missing data on request"),
          @ApiResponse(responseCode = "404", description = "Rebel not found")
      },
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The new location information",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(
                  implementation = Location.class,
                  example = "{\"latitude\":-10.1234,\"longitude\":35.4875,\"name\":\"Naboo Outpost\"}"
              )
          ),
          required = true
      )
  )
  public ResponseEntity<Void> reportRebelLocation(
      @Parameter(name = "id", description = "Id of the rebel to update the location")
      @PathVariable("id") Long id,

      @io.swagger.v3.oas.annotations.parameters.RequestBody
      @Valid @RequestBody Location location
  ) {
    rebelServices.updateLocation(id,location);
    return ResponseEntity.accepted().build();
  }

  @PutMapping("/{id}/report")
  @Operation(
      method = "PUT",
      summary = "Report Traitor",
      description = "Report a rebel as a traitor",
      responses = {
          @ApiResponse(responseCode = "202", description = "Success"),
          @ApiResponse(responseCode = "404", description = "Rebel not found")
      }
  )
  public ResponseEntity<Void> reportTraitorRebel(
      @Parameter(name = "id", description = "Id of the rebel you want to report as traitor")
      @PathVariable("id") Long id
  ) {
    rebelServices.reportAsTraitor(id);
    return ResponseEntity.accepted().build();
  }


}
