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
        assertThat(transform("1")).isEqualTo("Double.valueOf(1)");
    }

    @Test
    public void should_tranform_unary_minus() {
        assertThat(transform("-2")).isEqualTo("-Double.valueOf(2)");
    }

    @Test
    public void should_transform_high_priority_operation() {
        assertThat(transform("-2*3")).isEqualTo("(-Double.valueOf(2)).multiply(Double.valueOf(3))");
    }

    @Test
    public void should_transform_low_priority_operation() {
        assertThat(transform("-2-3")).isEqualTo("(-Double.valueOf(2)).minus(Double.valueOf(3))");
    }

    @Test
    public void should_transform_high_priority_operation_before_low_priority() {
        assertThat(transform("-2-3*4")).isEqualTo("(-Double.valueOf(2)).minus((Double.valueOf(3)).multiply(Double.valueOf(4)))");
    }

    private String transform(String input) {
        GrandMereLexer lexer = new GrandMereLexer(new ANTLRInputStream(input));
        GrandMereParser parser = new GrandMereParser(new CommonTokenStream(lexer));
        return new GrandMereVisitor().visit(parser.parse());
    }
}