package nz.ac.wgtn.jenz.stackagent;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAgent4 extends AbstractTestAgent {

    static {
        System.setProperty("stackagent.out","stackagent-out4.txt");
    }

    @Test
    public void test4() throws IOException {
        Foo.bypass();
        Set<Integer> collectedElements = getRecordedElements("stackagent-out4.txt");
        assertEquals(1,collectedElements.size());
        assertTrue(collectedElements.contains(2));
    }
}
