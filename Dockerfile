FROM apache/skywalking-java-agent:8.5.0-jdk8
WORKDIR /workspace
ADD target/demo2.jar /workspace/demo2.jar
ENTRYPOINT [ "sh", "-c", "java -jar /workspace/demo2.jar" ]

