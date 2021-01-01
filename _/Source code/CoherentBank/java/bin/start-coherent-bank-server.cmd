@echo off
@
@rem This will start a cache server
@
setlocal

:config
@rem specify the development root directory
set DEV_ROOT=%~dp0..\


@rem specify the JVM heap size
set memory=128m


:start

if "%JAVA_HOME%"=="" (set JAVA_EXEC=java) else (set JAVA_EXEC=%JAVA_HOME%\bin\java)

:launch

set JAVA_OPTS="-Xms%memory% -Xmx%memory%"

set SYS_OPTS="-Dtangosol.coherence.cacheconfig=coherence-cache-config.xml"

set CLASSPATH=%DEV_ROOT%\cfg
set CLASSPATH=%CLASSPATH%;%DEV_ROOT%\build\classes
set CLASSPATH=%CLASSPATH%;%COHERENCE_HOME%\lib\coherence.jar
set CLASSPATH=%CLASSPATH%;%DEV_ROOT%\lib\coherence-tools\coherence-tools-1.0.jar
set CLASSPATH=%CLASSPATH%;%DEV_ROOT%\lib\commons-logging\commons-logging.jar
set CLASSPATH=%CLASSPATH%;%DEV_ROOT%\lib\jpa-2.0\javax.persistence_2.0_preview.jar
set CLASSPATH=%CLASSPATH%;%DEV_ROOT%\lib\jscience-4.3\jscience.jar
set CLASSPATH=%CLASSPATH%;%DEV_ROOT%\lib\spring-framework-3.0.0-RC1\org.springframework.beans-3.0.0.RC1.jar
set CLASSPATH=%CLASSPATH%;%DEV_ROOT%\lib\spring-framework-3.0.0-RC1\org.springframework.context-3.0.0.RC1.jar
set CLASSPATH=%CLASSPATH%;%DEV_ROOT%\lib\spring-framework-3.0.0-RC1\org.springframework.core-3.0.0.RC1.jar
set CLASSPATH=%CLASSPATH%;%DEV_ROOT%\lib\spring-framework-3.0.0-RC1\org.springframework.expression-3.0.0.RC1.jar
set CLASSPATH=%CLASSPATH%;%DEV_ROOT%\lib\mvel-2.0.13\mvel2-2.0.13.jar
set CLASSPATH=%CLASSPATH%;%DEV_ROOT%\lib\log4j-1.2.15\log4j-1.2.15.jar

"%JAVA_EXEC%" -server -showversion "%JAVA_OPTS%" "%SYS_OPTS%" -cp "%CLASSPATH%" com.tangosol.net.DefaultCacheServer %1

goto exit

:exit
endlocal
@echo on