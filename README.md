# Web-Driver-Manager
Web Driver Manager - Page/Browser/Enviroment selector + Logs File Generator (exec-maven-plugin)

3 arguments (class name / browser / enviroment)

commands (examples):

mvn exec:java@wdmanager -Dexec.args="home c" (localhost by default)

mvn exec:java@wdmanager -Dexec.args="contact f pro"

mvn exec:java@wdmanager -Dexec.args="all c"



Maven plugin:

<plugin>
		<groupId>org.codehaus.mojo</groupId>
		<artifactId>exec-maven-plugin</artifactId>
		<version>1.6.0</version>
		<executions>
			<execution>
				<id>wdmanager</id>
				<goals>
					<goal>java</goal>
				</goals>
				<configuration>
					<mainClass>org.tests.WebDriverManager</mainClass>
					<classpathScope>test</classpathScope>
					<cleanupDaemonThreads>false</cleanupDaemonThreads>
				</configuration>
			</execution>
		</executions>
</plugin>



