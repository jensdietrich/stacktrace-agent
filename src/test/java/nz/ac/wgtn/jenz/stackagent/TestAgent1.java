package nz.ac.wgtn.jenz.stackagent;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAgent1 extends AbstractTestAgent {

    static {
        System.setProperty("stackagent.out","stackagent-out1.txt");
    }

    @Test
    public void test1() throws IOException {
        System.setProperty("stackagent.out","stackagent-out1.txt");
        Foo.foo1(1);
        Set<Integer> collectedElements = getRecordedElements("stackagent-out1.txt");
        assertEquals(1,collectedElements.size());
        assertTrue(collectedElements.contains(0));
    }
}
