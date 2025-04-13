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

package io.cdap.wrangler.executor;

import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.DirectiveExecutionException;
import io.cdap.wrangler.api.DirectiveParseException;
import io.cdap.wrangler.api.ErrorRowException;
import io.cdap.wrangler.api.Executor;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.ReportErrorAndProceed;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Add javadoc
 */
public class AggregateSizeAndTime implements Executor<List<Row>, List<Row>> {

  private String sourceSizeColumn;
  private String sourceTimeColumn;
  private String targetSizeColumn;
  private String targetTimeColumn;
  private String sizeUnit;
  private String timeUnit;

  private long totalSizeBytes;
  private long totalTimeMillis;

  @Override
  public void initialize(Arguments args) throws DirectiveParseException {
    try {
      this.sourceSizeColumn = args.value("sourceSizeColumn");
      this.sourceTimeColumn = args.value("sourceTimeColumn");
      this.targetSizeColumn = args.value("targetSizeColumn");
      this.targetTimeColumn = args.value("targetTimeColumn");
      this.sizeUnit = args.value("sizeUnit");
      this.timeUnit = args.value("timeUnit");

    } catch (Exception e) {
      throw new DirectiveParseException("Failed to parse arguments for AggregateSizeAndTime: " + e.getMessage(), e);
    }
  }

  @Override
  public List<Row> execute(List<Row> rows, ExecutorContext context)
      throws DirectiveExecutionException, ErrorRowException, ReportErrorAndProceed {

    totalSizeBytes = 0;
    totalTimeMillis = 0;

    // First pass: Aggregate all values
    for (Row row : rows) {
      Object byteSizeObj = row.getValue(sourceSizeColumn);
      if (byteSizeObj instanceof ByteSize) {
        totalSizeBytes += ((ByteSize) byteSizeObj).getBytes();
      }

      Object timeDurationObj = row.getValue(sourceTimeColumn);
      if (timeDurationObj instanceof TimeDuration) {
        totalTimeMillis += ((TimeDuration) timeDurationObj).getMilliseconds();
      }
    }

    // Second pass: Add aggregated results to each row
    List<Row> outputRows = new ArrayList<>();
    for (Row row : rows) {
      Row updated = new Row(row); // Create a copy

      long finalSize = convertSize(totalSizeBytes, sizeUnit);
      long finalTime = convertTime(totalTimeMillis, timeUnit);

      updated.add(targetSizeColumn, new ByteSize(finalSize + "B"));
      updated.add(targetTimeColumn, new TimeDuration(finalTime + "ms"));

      outputRows.add(updated);
    }

    return outputRows;
  }

  private long convertSize(long bytes, String unit) {
    switch (unit.toUpperCase()) {
      case "MB":
        return bytes / (1024 * 1024);
      case "GB":
        return bytes / (1024 * 1024 * 1024);
      default:
        return bytes;
    }
  }

  private long convertTime(long millis, String unit) {
    switch (unit.toLowerCase()) {
      case "seconds":
        return millis / 1000;
      case "minutes":
        return millis / (60 * 1000);
      default:
        return millis;
    }
  }

  @Override
  public void destroy() {
    // No resources to clean up in this implementation
  }
}
