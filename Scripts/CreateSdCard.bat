set PATH_APP=%1
set PATH_SDK=%2
set PATH_LOG=%3
set AVD_NAME=%4

mkdir %PATH_LOG%

echo Creation of the SdCard > %PATH_LOG%\log_SDCARD.txt
cd %PATH_SDK%\tools
mksdcard -l mySdCard 1024M mySdCardFile.img

echo Lunching the emulator with the created SdCard >> %PATH_LOG%\log_SDCARD.txt
cd %PATH_SDK%\tools
emulator -avd %AVD_NAME% -sdcard mySdCardFile.img

echo Timeout while the emulator lunches and scan the sdcard >> %PATH_LOG%\log_SDCARD.txt
TIMEOUT 120
 
echo Create the SdCard's directory to save pictures >> %PATH_LOG%\log_SDCARD.txt
cd %PATH_SDK%\platform-tools
adb shell mkdir /mnt/sdcard/Pictures

echo Create the SdCard's directory to save videos >> %PATH_LOG%\log_SDCARD.txt
cd %PATH_SDK%\platform-tools
adb shell mkdir /mnt/sdcard/Videos 

for %%X in (%PATH_APP%\SDcard\*.jpg) do ( 
cd %PATH_SDK%\platform-tools 
adb push %%X /mnt/sdcard/Pictures >> %PATH_LOG%\log_SDCARD.txt
echo add pictures %%X >> %PATH_LOG%\log_SDCARD.txt
cd %PATH_APP%\SDCard
)
for %%X in (%PATH_APP%\SDcard\*.3gp) do ( 
cd %PATH_SDK%\platform-tools 
adb push %%X /mnt/sdcard/Videos >> %PATH_LOG%\log_SDCARD.txt
echo add video %%X >> %PATH_LOG%\log_SDCARD.txt
cd %PATH_APP%\SDCard
)

echo Kill the emulator >> %PAHT_LOG%\log_SDCARD.txt
taskkill /F /IM emulator-arm.exe

  
	

