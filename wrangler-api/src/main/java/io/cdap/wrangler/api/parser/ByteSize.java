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

public class ByteSize implements Token {
  private final String original;
  private final long bytes;

  public ByteSize(String value) {
    this.original = value;
    this.bytes = parseByteSize(value);
  }

  private long parseByteSize(String value) {
    String v = value.trim().toUpperCase();
    double number = Double.parseDouble(v.replaceAll("[^0-9.]", ""));
    if (v.endsWith("KB")) return (long) (number * 1024);
    if (v.endsWith("MB")) return (long) (number * 1024 * 1024);
    if (v.endsWith("GB")) return (long) (number * 1024 * 1024 * 1024);
    if (v.endsWith("TB")) return (long) (number * 1024L * 1024L * 1024L * 1024L);
    if (v.endsWith("B")) return (long) number;
    throw new IllegalArgumentException("Invalid byte size format: " + value);
  }

  public long getBytes() {
    return bytes;
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
