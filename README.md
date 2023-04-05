# stacktrace-agent
An agent that records how an execution reproduces a given stacktrace.

# Requirements

The agent requires Java 11 to be built and run.

# Building 

Testing agents is tricky as the packaged agent is needed to run the tests. This can be achieved as follows:

1. build agents without tests, this will generate agent jar in `/target`: `mvn package -DskipTests`
2. then run tests: `mvn test`
3. the agent generated is `/target/stacktrace-agent.jar`

Note that some of the tests must run in a separate JVM. This is achieved through surefire settings and 
some tests with only one method per class. When running tests without Maven (e.g., in an IDE),
the JVM must be started with `-Djdk.attach.allowAttachSelf=true`.

# Running an Instrumented Program

`java -javaagent:stacktrace-agent.jar -Dstackagent.stacktrace=<stacktrace> -Dstackagent.out=out.txt -cp <classpath> <main-class>`

This requires two additional properties to be set: 

1. `stackagent.stacktrace` -- a stacktrace to be traced
2. `stackagent.out` -- the file to which the output will be written

The stacktrace is encoded as follows: the string is a comma-separated list of elements, each element has the following form:

`<classname>::<methodname><descriptor>`

Example: `pck.Foo::foo1(I)V,pck.Foo::foo2(I)V,pck.Foo::foo3(I)V`

The output file will be produced when the JVM exists. The file will contain a single line with comma-separated integer values, 
representing the indices of the functions observed in the stacktrace, starting with 0. For instance, if the file contained 
the line `0,2`, then this means that the agent would have encountered the execution of `pck.Foo::foo1(I)V` and `pck.Foo::foo3(I)V`, 
but not `pck.Foo::foo2(I)V`. 

## Notes

1. The stacktrace is **not** in the format produced by the JVM dynamically (e.g. in exception stack traces), it does contain descriptors but lacks line numbers. The agent has been build to work with call chains produced by static analyses.
2. Communication via JVM exit codes would have been somehow preferrable, but this is difficult to achieve as the agent would need to override exit codes set by explicit `System::exit` calls. To the best of our knowledge, there is no "official" way to do this.
3. The agent uses *asm* but does not (yet) shade it. This might lead to classpath hell issues when another version of *asm* is in the classpath, and could be addressed in a future release. 
4. Porting this to Java 1.8 is possible.


## Example Input (Stacktrace)

Stacktrace to trigger DOS in *snakeyaml* .

```
Driver::main([Ljava/lang/String;)V,org.yaml.snakeyaml.Yaml::load(Ljava/io/Reader;)Ljava/lang/Object;,org.yaml.snakeyaml.Yaml::loadFromReader(Lorg/yaml/snakeyaml/reader/StreamReader;Ljava/lang/Class;)Ljava/lang/Object;,org.yaml.snakeyaml.constructor.BaseConstructor::getSingleData(Ljava/lang/Class;)Ljava/lang/Object;,org.yaml.snakeyaml.composer.Composer::getSingleNode()Lorg/yaml/snakeyaml/nodes/Node;,org.yaml.snakeyaml.composer.Composer::getNode()Lorg/yaml/snakeyaml/nodes/Node;,org.yaml.snakeyaml.composer.Composer::composeNode(Lorg/yaml/snakeyaml/nodes/Node;)Lorg/yaml/snakeyaml/nodes/Node;,org.yaml.snakeyaml.composer.Composer::composeSequenceNode(Ljava/lang/String;)Lorg/yaml/snakeyaml/nodes/Node;

```