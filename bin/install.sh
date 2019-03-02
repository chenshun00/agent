#!/usr/bin/env bash
git clone https://github.com/chenshun00/agent.git
cd agent
mvn clean install -Dmaven.test.skip=true
cp boot-agent/target/boot-agent-1.0-SNAPSHOT.zip ~/Desktop

#-javaagent:/Users/chenshun/Desktop/boot-agent-1.0-SNAPSHOT/boot-strap-1.0-SNAPSHOT.jar -noverify