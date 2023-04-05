package nz.ac.wgtn.jenz.stackagent;

import java.util.Objects;

/**
 * Simple representation of a method.
 * @author jens dietrich
 */
public class JMethod {

    // this is the class defining the method
    private String owner = null;
    private String name = null;
    private String descriptor = null;

    public JMethod(String owner, String name, String descriptor) {
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JMethod jMethod = (JMethod) o;
        return Objects.equals(owner, jMethod.owner) && Objects.equals(name, jMethod.name) && Objects.equals(descriptor, jMethod.descriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name, descriptor);
    }

    @Override
    public String toString() {
        return "JMethod{" +
                "owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", descriptor='" + descriptor + '\'' +
                '}';
    }
}
