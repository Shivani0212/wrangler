package io.cdap.wrangler.executor;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.TestingRig;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AggregateSizeAndTimeTest {

  @Test
  public void testAggregateSizeAndTime() throws Exception {
    // Input rows with ByteSize and TimeDuration fields
    List<Row> rows = new ArrayList<>();
    rows.add(new Row("data_transfer_size", new ByteSize("10KB"))
                .add("response_time", new TimeDuration("500ms")));
    rows.add(new Row("data_transfer_size", new ByteSize("2MB"))
                .add("response_time", new TimeDuration("2.5s")));

    String[] recipe = new String[] {
      "#pragma version 2.0;",
      "#pragma load-directives aggregate-size-and-time;",
      "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec sizeUnit=MB timeUnit=seconds"
    };

    List<Row> results = TestingRig.execute(recipe, rows);

    // Only 1 expected row (aggregated output per row)
    Assert.assertEquals(2, results.size());

    // total size = 10KB + 2MB = 10*1024 + 2*1024*1024 = 10485760 bytes → 10MB
    double expectedSizeMB = (10 * 1024 + 2 * 1024 * 1024) / (1024.0 * 1024.0);
    // total time = 500ms + 2500ms = 3000ms = 3 seconds
    double expectedTimeSeconds = 3.0;

    for (Row row : results) {
      ByteSize totalSize = (ByteSize) row.getValue("total_size_mb");
      TimeDuration totalTime = (TimeDuration) row.getValue("total_time_sec");

      Assert.assertEquals(expectedSizeMB, totalSize.getBytes() / (1024.0 * 1024.0), 0.001);
      Assert.assertEquals(expectedTimeSeconds, totalTime.getMilliseconds() / 1000.0, 0.001);
    }
  }
}
