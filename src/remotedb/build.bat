@echo off

set warName=womsWS

:: Make the build directory in case this hasn't been run before
mkdir build\WEB-INF\classes > NUL 2> NUL

:: Get the list of all java files and place in a temporary file
cd java
dir /s /B *.java > ..\temp
cd ..

:: Compile the main java files and place them in the war classes directory
javac -d build\WEB-INF\classes @temp

:: Compile the services using jax-ws's wsgen and place them in the war classes directory
wsgen -d build\WEB-INF\classes -cp build\WEB-INF\classes ^
    cs2340.woms.model.database.RemoteDatabaseAPIImpl ^
    > NUL

:: Copy the resources recursively, ignoring overwrite prompts
xcopy /y /S resources\** build\WEB-INF > NUL

:: War the build files
jar -cf build\%warName%.war -C build WEB-INF

:: Cleanup
del temp