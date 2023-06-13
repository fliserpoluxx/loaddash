@echo off
java -Dlog4j.configurationFile=logging-network.xml -Xmx512M -Xms512M -jar "%~dp0PlayPen-1.0.jar" network %*