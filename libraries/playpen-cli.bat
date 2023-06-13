@echo off
java -Dlog4j.configurationFile=logging-cli.xml -jar "%~dp0PlayPen-1.0.jar" cli %*