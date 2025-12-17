@echo off
echo ==========================================
echo Campus Connect Build (JDK-19)
echo ==========================================

set JAVA_HOME=C:\Program Files\Java\jdk-19
set PATH=%JAVA_HOME%\bin;C:\mingw64\bin;%PATH%

echo Using Java: %JAVA_HOME%
java -version
echo.

cd /d D:\DSA_FINAL_PROJECT\CampusConnect

REM === Check what folder structure exists ===
if exist src\cpp (
    set CPP_DIR=src\cpp
) else if exist cpp_src (
    set CPP_DIR=cpp_src
) else (
    echo [ERROR] Cannot find C++ source directory!
    echo Expected: src\cpp or cpp_src
    pause
    exit /b 1
)

echo Using C++ directory: %CPP_DIR%
echo.

REM === Compile C++ ===
echo [1/5] Compiling C++ code...
cd %CPP_DIR%
del *.o 2>nul

g++ -c -std=c++11 navigation\Graph.cpp attendance\HashTable.cpp attendance\AVLTree.cpp student\BTree.cpp student\QuickSort.cpp scheduling\PriorityQueue.cpp resources\Trie.cpp resources\KMP.cpp -I.

if errorlevel 1 (
    echo.
    echo [ERROR] C++ compilation failed!
    echo.
    echo Try compiling one file to see error:
    echo   cd %CPP_DIR%
    echo   g++ -c -std=c++11 navigation\Graph.cpp -I.
    echo.
    pause
    exit /b 1
)

echo [OK] C++ compiled
dir *.o
echo.

REM === Find Java classes ===
echo [2/5] Locating Java classes...

REM --- Ensure we are in the project root (D:\DSA_FINAL_PROJECT\CampusConnect) ---
cd /d D:\DSA_FINAL_PROJECT\CampusConnect

set CLASS_DIR=
REM --- Check NetBeans' build folder (where you confirmed the files are) ---
if exist build\classes\com\campusconnect (
    set CLASS_DIR=build\classes
    echo [OK] Found classes in NetBeans build folder
) else if exist bin\com\campusconnect (
    set CLASS_DIR=bin
    echo [OK] Found classes in bin folder
) else (
    echo [ERROR] Java classes not found!
    echo.
    echo Please build in NetBeans first:
    echo   Right-click project -> Clean and Build (F11)
    echo.
    exit /b 1
)
echo.
REM === Generate JNI header ===
echo [3/5] Generating JNI header...

REM --- Ensure we are at the project root for stability ---
cd /d D:\DSA_FINAL_PROJECT\CampusConnect

REM --- Create the output directory for the header file ---
set HEADER_OUTPUT_DIR=%CPP_DIR%\jni
if not exist %HEADER_OUTPUT_DIR% mkdir %HEADER_OUTPUT_DIR%
echo [DEBUG] Target Header Dir: %HEADER_OUTPUT_DIR%

REM --- Run javac -h using ABSOLUTE PATH and CALL to prevent early exit ---
echo [DEBUG] Executing JNI Header Command using %JAVA_HOME%\bin\javac.exe ...
CALL "%JAVA_HOME%\bin\javac.exe" -h %HEADER_OUTPUT_DIR% -cp %CLASS_DIR% com.campusconnect.CampusConnectJNI

if errorlevel 1 (
    echo.
    echo [ERROR] JNI Header generation failed (Errorlevel 1)!
    pause
    exit /b 1
)

if not exist %HEADER_OUTPUT_DIR%\com_campusconnect_CampusConnectJNI.h (
    echo [ERROR] JNI header file missing after execution!
    pause
    exit /b 1
)

echo [OK] JNI header generated in %HEADER_OUTPUT_DIR%
echo.

REM --- Continue compilation in C++ directory ---
cd %CPP_DIR%
REM === Compile JNI ===
echo [4/5] Compiling JNI bridge...
cd %CPP_DIR%

if not exist jni\com_campusconnect_CampusConnectJNI.h (
    echo [ERROR] JNI header missing!
    dir jni\
    pause
    exit /b 1
)

if not exist jni\com_campusconnect_CampusConnectJNI.cpp (
    echo [ERROR] JNI implementation missing!
    echo Expected: jni\com_campusconnect_CampusConnectJNI.cpp
    pause
    exit /b 1
)

g++ -shared -o campusconnect.dll ^
    -I"%JAVA_HOME%\include" ^
    -I"%JAVA_HOME%\include\win32" ^
    jni\com_campusconnect_CampusConnectJNI.cpp ^
    *.o -I.

if errorlevel 1 (
    echo.
    echo [ERROR] JNI compilation failed!
    echo.
    echo Checking requirements:
    dir "%JAVA_HOME%\include\jni.h"
    dir "%JAVA_HOME%\include\win32\jni_md.h"
    echo.
    pause
    exit /b 1
)

echo [OK] JNI bridge compiled
echo.

REM === Install DLL ===
echo [5/5] Installing library...
cd ..

if exist src\cpp (
    cd ..
)

if not exist lib mkdir lib

if exist %CPP_DIR%\campusconnect.dll (
    move /Y %CPP_DIR%\campusconnect.dll lib\
) else if exist src\cpp\campusconnect.dll (
    move /Y src\cpp\campusconnect.dll lib\
)

if not exist lib\campusconnect.dll (
    echo [ERROR] DLL not found!
    dir %CPP_DIR%\*.dll
    pause
    exit /b 1
)

echo [OK] Library installed
echo.

REM === Cleanup ===
del %CPP_DIR%\*.o 2>nul

echo ==========================================
echo BUILD SUCCESSFUL!
echo ==========================================
echo.
echo Native library: lib\campusconnect.dll
echo Java classes: %CLASS_DIR%
echo.
echo Next steps:
echo   1. In NetBeans, right-click project -> Properties -> Run
echo   2. VM Options: -Djava.library.path=lib
echo   3. Right-click Main.java -> Run File
echo.
pause