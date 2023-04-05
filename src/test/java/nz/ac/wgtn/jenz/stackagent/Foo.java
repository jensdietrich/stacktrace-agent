package nz.ac.wgtn.jenz.stackagent;

/**
 * Used as test data.
 * The target stack trace is:  "nz.ac.wgtn.jenz.stackagent.Foo::foo1(I)V,nz.ac.wgtn.jenz.stackagent.Foo::foo2(I)V,nz.ac.wgtn.jenz.stackagent.Foo::foo3(I)V"
 * @author jens dietrich
 */
public class Foo {

    public static void foo1(int depth) {
        if (depth>1) {
            foo2(depth);
        }
    }

    public static void foo2(int depth) {
        if (depth>2) {
            foo3(depth);
        }
    }

    public static void foo3(int depth) {

    }

    public static void bypass() {
        foo3(0);
    }
}
