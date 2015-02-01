set PATH_APP=%1
set PATH_SDK=%2
set PATH_LOG=%3
set AVD_NAME=%4

mkdir %PATH_LOG%

del %PATH_APP%\SDCard\mySdCardFile.img
echo Copy the local SDCard into the test environment
copy %PATH_SDK%\tools\mySdCardFile.img %PATH_APP%\SDCard

echo Lunching the emulator with the created SdCard > %PATH_LOG%\log.txt
cd %PATH_SDK%\tools
emulator -avd %AVD_NAME% -sdcard %PATH_APP%\SDCard\mySdCardFile.img

echo Timeout while the emulator lunches and scan the sdcard >> %PATH_LOG%\log.txt
TIMEOUT 120
 
echo Installing the application >> %PATH_LOG%\log.txt
cd %PATH_SDK%\platform-tools
adb install -r %PATH_APP%\App\bin\MOM.apk

echo Clear the logcat >> %PATH_LOG%\log.txt
cd %PATH_SDK%\platform-tools
adb logcat -c

echo Lunching the application >> %PATH_LOG%\log.txt
cd %PATH_SDK%\platform-tools
adb shell am instrument -w com.onmobile.mom/android.test.InstrumentationTestRunner 1> %PATH_LOG%\log_ERREUR.txt

echo Extract application logcat >> %PATH_LOG%\log.txt
cd %PATH_SDK%\platform-tools
adb logcat -d MOM:D *:S > %PATH_LOG%\logcat_APP.txt

echo Extract librairy logcat >> %PATH_LOG%\log.txt
cd %PATH_SDK%\platform-tools
adb logcat -d CoreServices:D *:S > %PATH_LOG%\logcat_CORESERVICES.txt

echo Uninstalling the application >> %PATH_LOG%\log.txt
cd %PATH_SDK%\platform-tools
adb uninstall com.onmobile.mom
