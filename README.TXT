Äëÿ WIN x64 you should download java wrapper service from source forge which is opesource. As for Java Wrapper service can make warning that
wrapper.dll is not suitable for the current version. In these case i copy the wrapper.dll from SONAR and paste it to the java service wrapper folder and everything went well.

Some key moments oà using java service wrapper. See the documentation for more information. I have use SimpleApp to run the service.

Here is the most key moments of wrapper.conf
# using sample app - you can choose any variant
wrapper.java.mainclass=org.tanukisoftware.wrapper.WrapperSimpleApp
# Java Classpath (include wrapper.jar)  Add class path elements as
#  needed starting from 1
wrapper.java.classpath.1=../lib/wrappertest.jar
wrapper.java.classpath.2=../lib/wrapper.jar
# rest of all is user defined libs
wrapper.java.classpath.3=../lib/webserver-availability-1.0-SNAPSHOT.jar
wrapper.java.classpath.4=../lib/slf4j-api-1.4.0.jar
wrapper.java.classpath.5=../lib/slf4j-log4j12-1.4.0.jar
wrapper.java.classpath.6=../lib/log4j-1.2.13.jar
wrapper.java.classpath.7=../lib/activation-1.1.jar
wrapper.java.classpath.8=../lib/junit-3.8.1.jar
wrapper.java.classpath.9=../lib/mail-1.4.jar
# name of the class - entry point or starting point of your program
wrapper.app.parameter.1=ru.fors.diagnostics.Test

# Name of the service (change for your choose)
wrapper.name=ServiceAvailability

# Display name of the service (display name)
wrapper.displayname=ServiceAvailability Application

# Description of the service
wrapper.description=ServiceAvailability Description

# Service dependencies.  Add dependencies as needed starting from 1 [here you can add dependencies of another windows service]
wrapper.ntservice.dependency.1=
