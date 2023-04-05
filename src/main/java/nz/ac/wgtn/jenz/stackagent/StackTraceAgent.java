package nz.ac.wgtn.jenz.stackagent;

import nz.ac.wgtn.jenz.stackagent.shaded.janala.SafeClassWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * ASM-based agent to log how execution resembles an existing stack trace.
 * @author jens dietrich
 */
public class StackTraceAgent {

    static final String STACKTRACE = System.getProperty("stackagent.stacktrace");
    static List<JMethod> TARGET_STACK = null;
    static Set<Integer> LOGGED_EXECUTIONS = new HashSet<>();

    static {
        if (STACKTRACE==null) throw new IllegalStateException("system property missing: \"stackagent.stacktrace\"");
        TARGET_STACK = StackTraceParser.parseTargetStack(STACKTRACE);
    }

    static void logExecution(int index) {
        // System.out.println("execution logged: " + index);
        LOGGED_EXECUTIONS.add(index);
    }

    // agentmain needed for testing
    public static void agentmain(String agentArgs, Instrumentation inst) {
        premain(agentArgs,inst);
    }
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("activating agent " + StackTraceAgent.class);
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
                ClassReader reader = new ClassReader(bytes);
                // this prevents some issues with the standard class writer leading to duplicate class errors
                ClassWriter writer = new SafeClassWriter(reader,classLoader,ClassWriter.COMPUTE_FRAMES);
                try {
                    StackTraceVisitor visitor = new StackTraceVisitor(writer);
                    reader.accept(visitor, 0);
                    return writer.toByteArray();
                }
                catch (Throwable x) {
                    x.printStackTrace();
                    return bytes;
                }
            }
        });

        Runnable writeLoggedExecutions = () -> {
            String out = System.getProperty("stackagent.out");
            if (out==null) throw new IllegalStateException("system property missing: \"stackagent.out\"");
            String content = LOGGED_EXECUTIONS.stream()
                .sorted()
                .map(v -> v.toString())
                .collect(Collectors.joining(","));
            try {
                Files.writeString(Paths.get(out),content,UTF_8,CREATE,TRUNCATE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Runtime.getRuntime().addShutdownHook(new Thread(writeLoggedExecutions));
    }


}