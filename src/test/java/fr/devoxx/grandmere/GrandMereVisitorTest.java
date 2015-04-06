package fr.devoxx.grandmere;

import com.devoxx.GrandMereLexer;
import com.devoxx.GrandMereParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GrandMereVisitorTest {

    @Test
    public void should_tranform_atom() {
        assertThat(transform("1")).isEqualTo("1");
    }

    private String transform(String input) {
        GrandMereLexer lexer = new GrandMereLexer(new ANTLRInputStream(input));
        GrandMereParser parser = new GrandMereParser(new CommonTokenStream(lexer));
        return new GrandMereVisitor().visit(parser.parse());
    }
}