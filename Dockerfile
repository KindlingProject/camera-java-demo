FROM java
WORKDIR /workspace
ENV JAVA_OPTS=""
ADD springboot-stuck-demo-1.0.jar /workspace/springboot-stuck-demo-1.0.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /workspace/springboot-stuck-demo-1.0.jar" ]