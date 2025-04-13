package io.cdap.wrangler.utils;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import org.junit.Assert;
import org.junit.Test;

public class ByteSizeTimeDurationParserTest {

    @Test
    public void testByteSizeParser() {
        ByteSize byteSize = new ByteSize("10KB");
        Assert.assertEquals(10240, byteSize.getBytes()); // 10KB = 10240 bytes
        
        byteSize = new ByteSize("1MB");
        Assert.assertEquals(1048576, byteSize.getBytes()); // 1MB = 1048576 bytes
        
        byteSize = new ByteSize("2GB");
        Assert.assertEquals(2147483648L, byteSize.getBytes()); // 2GB = 2147483648 bytes
    }

    @Test
    public void testTimeDurationParser() {
        TimeDuration timeDuration = new TimeDuration("150ms");
        Assert.assertEquals(150, timeDuration.getMilliseconds()); 
        
        timeDuration = new TimeDuration("2s");
        Assert.assertEquals(2000, timeDuration.getMilliseconds()); 
        
        timeDuration = new TimeDuration("1.5m");
        Assert.assertEquals(90000, timeDuration.getMilliseconds()); 
    }
}
