REM MAKEFILE
@echo off
javac md\ceiti\*.java
jar -cvmf META-INF\MANIFEST.MF Depozit.jar md\ceiti\*.class