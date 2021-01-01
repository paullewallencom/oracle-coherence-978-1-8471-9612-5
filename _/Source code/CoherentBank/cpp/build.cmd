@echo off
setlocal

@rem specify the development root directory
set DEV_ROOT=%~dp0

set MODE=%1

if "%INCLUDE%"=="" (
    echo This command must be executed from a Visual Studio command prompt.
    goto exit
  )

if "%COHERENCE_CPP_HOME%"=="" (
	set COHERENCE_CPP_HOME="%COHERENCE_HOME%\coherence-cpp"
	)

if not exist "%COHERENCE_CPP_HOME%" (
	echo Check system variables. 
	echo	1. COHERENCE_HOME needs to point to the root of Coherence instalation location
	echo	and inside that folder "coherence-cpp" folder needs to contain coherence.cpp instalation
	echo    or
	echo	2. COHERENCE_CPP_HOME needs to point to the root of coherence.cpp instalation folder
	goto exit
	)
	
set OPT=/c /nologo /EHsc /GR /DWIN32
set LOPT=/NOLOGO /SUBSYSTEM:CONSOLE /INCREMENTAL:NO

set INC=/I"%DEV_ROOT%\include\seovic\samples\bank\domain"
set INC=%INC% /I"%DEV_ROOT%\include\seovic\samples\bank\serializer" 
set INC=%INC% /I"%DEV_ROOT%\include\seovic\samples\bank\processor" 
set INC=%INC% /I"%DEV_ROOT%\include\seovic\samples\bank\helper"
set INC=%INC% /I"%DEV_ROOT%\include\seovic\samples\bank\service"
set INC=%INC% /I"%DEV_ROOT%\include\seovic\samples\bank\exception"
set INC=%INC% /I"%COHERENCE_CPP_HOME%\include" 
set INC=%INC% /I.



set SRC="%DEV_ROOT%\src\seovic\samples\bank\*.cpp"
set SRC=%SRC% "%DEV_ROOT%\src\seovic\samples\bank\domain\*.cpp"
set SRC=%SRC% "%DEV_ROOT%\src\seovic\samples\bank\serializer\*.cpp"
set SRC=%SRC% "%DEV_ROOT%\src\seovic\samples\bank\processor\*.cpp"
set SRC=%SRC% "%DEV_ROOT%\src\seovic\samples\bank\helper\*.cpp"
set SRC=%SRC% "%DEV_ROOT%\src\seovic\samples\bank\service\*.cpp"

set OUT=%DEV_ROOT%\dist\ATM.exe

set LIBPATH=%COHERENCE_CPP_HOME%\lib
set LIBS="%LIBPATH%\coherence.lib"

if "%MODE%"=="debug" (
    set OPT=%OPT% /Zi /MDd
  ) else (
    set OPT=%OPT% /O2 /MD
  )

if not exist "%DEV_ROOT%\dist" (
 	mkdir "%DEV_ROOT%\dist"
 )
  
echo building %OUT% ...
cl %OPT% %INC% %SRC%
link %LOPT% %LIBS% *.obj /OUT:"%OUT%"
mt -manifest "%OUT%.manifest" -outputresource:"%OUT%";1

del *.obj

@rem copy config files
echo copying coherence cofiguration files...
copy "%DEV_ROOT%\cfg\*.xml" "%DEV_ROOT%\dist"
copy "%COHERENCE_CPP_HOME%\lib\coherence.dll" "%DEV_ROOT%\dist"
echo done

:exit
