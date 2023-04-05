package nz.ac.wgtn.jenz.stackagent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Instrument methods to record executions of methods contained in some target stack.
 * @author jens dietrich
 */
public class StackTraceVisitor extends ClassVisitor {

    public StackTraceVisitor(ClassWriter writer) {
        super(Opcodes.ASM9,writer);
    }

    private String className = null;

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature,exceptions);
        JMethod method = new JMethod(className,name,descriptor);
        final int idx = StackTraceAgent.TARGET_STACK.indexOf(method);
        if (idx>-1) {
            System.out.println("instrumenting: " + method);
            return new MethodEntryVisitor(mv,idx);
        }
        else {
            return mv;
        }

    }

    static class MethodEntryVisitor extends MethodVisitor {
        private MethodVisitor mv = null;
        private int idx = -1;
        public MethodEntryVisitor(MethodVisitor mv, int idx) {
            super(Opcodes.ASM9, mv);
            this.mv = mv;
            this.idx = idx;
        }
        @Override
        public void visitCode() {
            super.visitCode();
            mv.visitLdcInsn(idx);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, StackTraceAgent.class.getName().replace('.', '/'), "logExecution", "(I)V", false);
        }
    }
}

