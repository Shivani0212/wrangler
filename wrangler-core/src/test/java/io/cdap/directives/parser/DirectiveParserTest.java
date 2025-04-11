/*
 *  Copyright © 2017-2019 Cask Data, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package io.cdap.directives.parser;

import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DirectivesParserTest {

    @Test
    public void testByteSizeParsing() {
        String input = "10MB";
        DirectivesLexer lexer = new DirectivesLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectivesParser parser = new DirectivesParser(tokens);

        DirectivesParser.ByteSizeArgContext context = parser.byteSizeArg();
        assertNotNull(context);
    }

    @Test
    public void testTimeDurationParsing() {
        String input = "5h";
        DirectivesLexer lexer = new DirectivesLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectivesParser parser = new DirectivesParser(tokens);

        DirectivesParser.TimeDurationArgContext context = parser.timeDurationArg();
        assertNotNull(context);
    }
}
