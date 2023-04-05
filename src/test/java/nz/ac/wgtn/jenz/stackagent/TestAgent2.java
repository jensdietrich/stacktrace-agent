package nz.ac.wgtn.jenz.stackagent;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAgent2 extends AbstractTestAgent {

    static {
        System.setProperty("stackagent.out","stackagent-out2.txt");
    }

    @Test
    public void test2() throws IOException {
        Foo.foo1(2);
        Set<Integer> collectedElements = getRecordedElements("stackagent-out2.txt");
        assertEquals(2,collectedElements.size());
        assertTrue(collectedElements.contains(0));
        assertTrue(collectedElements.contains(1));
    }

}
