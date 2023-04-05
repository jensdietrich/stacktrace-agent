package nz.ac.wgtn.jenz.stackagent;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAgent3 extends AbstractTestAgent {

    static {
        System.setProperty("stackagent.out","stackagent-out3.txt");
    }

    @Test
    public void test3() throws IOException {
        Foo.foo1(3);
        Set<Integer> collectedElements = getRecordedElements("stackagent-out3.txt");
        assertEquals(3,collectedElements.size());
        assertTrue(collectedElements.contains(0));
        assertTrue(collectedElements.contains(1));
        assertTrue(collectedElements.contains(2));
    }

}
