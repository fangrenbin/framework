#framework#

##Instruction##
The framework can help you generate j2ee application as quick as possible.it also include a `maven-repo` branch, so you can add it into your pom.xml easily. framework-configuration is used to load xml configuraiton files.

Include modue:

	framework-configuration

##How to use##
add dependency to your pom.xml.

	    <repositories>
            <repository>
                <id>framework-mvn-repo</id>
                <url>https://raw.github.com/fangrenbin/framework/mvn-repo/</url>
                <snapshots>
                    <enabled>true</enabled>
                    <updatePolicy>always</updatePolicy>
                </snapshots>
            </repository>
        </repositories>

        <dependencies>
            <dependency>
                <groupId>name.frb.framework</groupId>
                <artifactId>framework-configuration</artifactId>
                <version>1.0.0-SNAPSHOT</version>
            </dependency>
        </dependencies>

