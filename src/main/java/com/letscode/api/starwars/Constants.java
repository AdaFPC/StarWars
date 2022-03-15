package com.letscode.api.starwars;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

  public static final int FOOD_POINTS = 1;
  public static final int WATER_POINTS = 2;
  public static final int AMMO_POINTS = 3;
  public static final int WEAPON_POINTS = 4;


  public static final String NEGATIVE = "negative";
  public static final String CANNOT_BE_NEGATIVE = "cannot be negative";
  public static final String FIELD_REQUIRED = "field.required";
  public static final String FIELD_REQUIRED_MESSAGE = "field is required";
  public static final String LESS_THAN_3_CHARACTERS_MESSAGE = "cannot be less than 3 characters";
  public static final String MORE_THAN_255_CHARACTERS_MESSAGE = "cannot be more than 255 characters";
  public static final String LESS_THAN_5_CHARACTERS_MESSAGE = "cannot be less than 5 characters";
  public static final String MORE_THAN_127_CHARACTERS_MESSAGE = "cannot be more than 127 characters";
  public static final String LATITUDE_VALUE_INVALID = "latitude.value.invalid";
  public static final String LATITUDE_MESSAGE = "A latitude value must be between -90 and 90";
  public static final String LONGITUDE_VALUE_INVALID = "longitude.value.invalid";
  public static final String LONGITUDE_MESSAGE = "A longitude value must be between -180 and 180";
  public static final String CANNOT_BE_NEGATIVE_MESSAGE = "cannot be negative";
  public static final String NEGATIVE_VALUE = "negative.value";
  public static final String LESSTHAN_3 = "lessthan3";
  public static final String BIGGERTHAN_255 = "biggerthan255";
  public static final String LESSTHAN_5 = "lessthan5";
  public static final String BIGGERTHAN_127 = "biggerthan127";
  public static final String TRADE_GIVING_INVALID = "trade.giving.invalid";
  public static final String TRADE_RECEIVING_INVALID = "trade.receiving.invalid";
  public static final String TRADE_POINTS_INVALID = "trade.points.invalid";
  public static final String TRADE_POINTS_INVALID_MESSAGE = "Trade points must be equal";
  public static final String TRADE_RECEIVING_INVALID_MESSAGE = "Invalid receiving trade options, it must receive at least one item";
  public static final String TRADE_GIVING_INVALID_MESSAGE = "Invalid giving trade options, it must give at least one item";
  public static final String REQUEST_ERROR = "There was an error on the request";
}
