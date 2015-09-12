This app generates the following data for an Android device:
- accounts
- call log
- sms
- calendar
- phone book
- import videos and pictures into the AVD sdcard

The quantity of data desired for each of those category can be configured in the Config.java class: https://github.com/adetalhouet/MOM/blob/master/app/src/com/onmobile/mom/app/Config.java#L148

The automaton is managed by VB scripts and Batch scripts.

The purpose was to automate tests against Voxmobili's SDK. It's a synchronization SDK to backup any kind of data listed above.