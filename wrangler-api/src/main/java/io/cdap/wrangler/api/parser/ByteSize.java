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
 * This class represents a byte size, parsing string values and converting them to bytes.
 * It supports multiple units such as KB, MB, GB, and TB.
 */
public class ByteSize implements Token {
  private final String original;
  private final long bytes;

  /**
   * Constructs a ByteSize object from a string representation.
   * 
   * @param value the byte size in string format (e.g., "10MB", "2GB")
   */
  public ByteSize(String value) {
    this.original = value;
    this.bytes = parseByteSize(value);
  }

  /**
   * Parses the string value to determine the byte size in bytes.
   * 
   * @param value the byte size in string format (e.g., "10MB", "2GB")
   * @return the equivalent byte value
   * @throws IllegalArgumentException if the string is in an invalid format
   */
  private long parseByteSize(String value) {
    String v = value.trim().toUpperCase();
    double number = Double.parseDouble(v.replaceAll("[^0-9.]", ""));
    
    if (v.endsWith("KB")) {
      return (long) (number * 1024);
    } else if (v.endsWith("MB")) {
      return (long) (number * 1024 * 1024);
    } else if (v.endsWith("GB")) {
      return (long) (number * 1024 * 1024 * 1024);
    } else if (v.endsWith("TB")) {
      return (long) (number * 1024L * 1024L * 1024L * 1024L);
    } else if (v.endsWith("B")) {
      return (long) number;
    } else {
      throw new IllegalArgumentException("Invalid byte size format: " + value);
    }
  }

  /**
   * Returns the byte size in bytes.
   * 
   * @return the byte size
   */
  public long getBytes() {
    return bytes;
  }

  /**
   * Returns the byte size value. This is an alias for getBytes().
   * 
   * @return the byte size value
   */
  public long getValue() {
    return getBytes();
  }

  @Override
  public Object value() {
    return bytes;
  }

  @Override
  public TokenType type() {
    return TokenType.BYTE_SIZE;
  }

  @Override
  public JsonElement toJson() {
    return new JsonPrimitive(original);
  }
}
