package com.letscode.api.starwars.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

import com.letscode.api.starwars.domains.Inventory;
import com.letscode.api.starwars.domains.Location;
import com.letscode.api.starwars.domains.enums.Gender;

import lombok.*;

@Builder
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class RebelDTO {
  @NotNull
  @Length(min = 5, max = 127)
  private String name;

  @NotNull
  @Positive
  private int age = 1;

  @NotNull
  private Gender gender;

  @NotNull
  private Location location;

  @NotNull
  private Inventory inventory;

}
