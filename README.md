nodemon --watch src -e java --exec "./mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005'"


./mvnw.cmd test

./mvnw.cmd surefire-report:report