package nz.ac.wgtn.jenz.stackagent;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStackParsing {

    @Test
    public void test1() {
        String value = "pck.Foo::bar(I)V";
        List<JMethod> parsed = StackTraceParser.parseTargetStack(value);
        assertEquals(1,parsed.size());
        JMethod method = parsed.get(0);
        assertEquals("pck/Foo",method.getOwner());
        assertEquals("bar",method.getName());
        assertEquals("(I)V",method.getDescriptor());
    }

    @Test
    public void test2() {
        String value = "pck.Foo1::bar1(I)V,pck.Foo2::bar2(I)J";
        List<JMethod> parsed = StackTraceParser.parseTargetStack(value);
        assertEquals(2,parsed.size());

        JMethod method1 = parsed.get(0);
        assertEquals("pck/Foo1",method1.getOwner());
        assertEquals("bar1",method1.getName());
        assertEquals("(I)V",method1.getDescriptor());

        JMethod method2 = parsed.get(1);
        assertEquals("pck/Foo2",method2.getOwner());
        assertEquals("bar2",method2.getName());
        assertEquals("(I)J",method2.getDescriptor());
    }

}
