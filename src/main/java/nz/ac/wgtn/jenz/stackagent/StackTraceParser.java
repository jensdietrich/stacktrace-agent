package nz.ac.wgtn.jenz.stackagent;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple utility to parse stack traces.
 * @author jens dietrich
 */
public class StackTraceParser {
    static List<JMethod> parseTargetStack (String value) {
        checkArg(value!=null,"value must not be null");
        checkArg(!value.trim().isEmpty(),"value must not be empty");
        List<JMethod> methods = new ArrayList<>();
        String[] elements = value.split(",");
        for (String element:elements) {
            String[] tokens = element.split("::");
            checkArg(!value.trim().isEmpty(),"stack trace element must contain :: to separate class name and method: " + element);
            String type = tokens[0].replace('.','/');
            String method = tokens[1];
            checkArg(method.contains("("),"method must contain descriptor <name>(<args>)<return>: " + method);
            int idx = method.indexOf('(');
            String name = method.substring(0,idx);
            String descriptor = method.substring(idx,method.length());
            JMethod jMethod = new JMethod(type,name,descriptor);
            System.out.println("will trace: " + jMethod);
            methods.add(jMethod);
        }
        return methods;
    }


    private static void checkArg(boolean condition,String msg) {
        if (!condition) throw new IllegalArgumentException(msg);
    }
}
