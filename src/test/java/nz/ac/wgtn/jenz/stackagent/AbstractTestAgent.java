package nz.ac.wgtn.jenz.stackagent;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractTestAgent {

    static {
        System.setProperty("stackagent.stacktrace","nz.ac.wgtn.jenz.stackagent.Foo::foo1(I)V,nz.ac.wgtn.jenz.stackagent.Foo::foo2(I)V,nz.ac.wgtn.jenz.stackagent.Foo::foo3(I)V");
    }

    @BeforeAll
    public static void installAgent () throws Exception {
        File agent = new File("target/stacktrace-agent.jar");
        if (!agent.exists()) {
            throw new IllegalStateException("agent must be build before testing -- use \"mvn clean package -DskipTests\"");
        }


        final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        final long pid = runtime.getPid();
        VirtualMachine self = null;

        try {
            self = VirtualMachine.attach(""+pid);
        } catch (AttachNotSupportedException e) {
            System.out.println("Cannot attach to this VM, program must be started with JVM option \"-Djdk.attach.allowAttachSelf=true\"");
        }
        System.out.println("self attached to: " + self);

        self.loadAgent(agent.getAbsolutePath());

    }


    protected Set<Integer> getRecordedElements(String path) throws IOException {
        String value = Files.readString(Paths.get(path));
        return Stream.of(value.split(",")).map(Integer::new).collect(Collectors.toSet());
    }

}
