/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * This class represents a time duration, parsing string values and converting them to milliseconds.
 * It supports units like ms (milliseconds), s (seconds), m (minutes), and h (hours).
 */
public class TimeDuration implements Token {
  private final String original;
  private final long milliseconds;

  /**
   * Constructs a TimeDuration object from a string representation.
   * 
   * @param value the time duration in string format (e.g., "10s", "2m", "1h")
   */
  public TimeDuration(String value) {
    this.original = value;
    this.milliseconds = parseDuration(value);
  }

  /**
   * Parses the string value to determine the time duration in milliseconds.
   * 
   * @param value the time duration in string format (e.g., "10s", "2m", "1h")
   * @return the equivalent time duration in milliseconds
   * @throws IllegalArgumentException if the string is in an invalid format
   */
  private long parseDuration(String value) {
    String v = value.trim().toLowerCase();
    double number = Double.parseDouble(v.replaceAll("[^0-9.]", ""));
    
    if (v.endsWith("ms")) {
      return (long) number;
    } else if (v.endsWith("s")) {
      return (long) (number * 1000);
    } else if (v.endsWith("m")) {
      return (long) (number * 60 * 1000);
    } else if (v.endsWith("h")) {
      return (long) (number * 60 * 60 * 1000);
    } else {
      throw new IllegalArgumentException("Invalid time duration format: " + value);
    }
  }

  /**
   * Returns the time duration in milliseconds.
   * 
   * @return the time duration in milliseconds
   */
  public long getMilliseconds() {
    return milliseconds;
  }

  /**
   * Returns the time duration value. This is an alias for getMilliseconds().
   * 
   * @return the time duration value in milliseconds
   */
  public long getValue() {
    return getMilliseconds();
  }

  @Override
  public Object value() {
    return milliseconds;
  }

  @Override
  public TokenType type() {
    return TokenType.TIME_DURATION;
  }

  @Override
  public JsonElement toJson() {
    return new JsonPrimitive(original);
  }
}
