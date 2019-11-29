## CMPUT 301 Team 14 Project Part 3

### APP Name: Mooditude

### Android Requirement

-  Android API Level 26 (Android 8 :Oreo) Required At Minimum
-  Google APIs are required for Emulators

### Register

- For a new user to register, username and emails must be unique
- Password length must be >= 6

### About Google Service API Key

- `google-services.json` is required for connection to the database. This file excluded from the project folder due to security issues.
- `google-services.json`  is uploaded to CMPUT 301 Team 14 Slack Chanel (FOR TA and Team Members)
- `google-services.json` needs to be put under `/Mooditude/App` folder for the APP to be correctly built. 

### About Google Map Service

- Only for emulators, users are required to provide a series of google map code to us to add on google map service console.

### Known Issues:

- Firebase connection may be **Unstable** When Running on Android Emulators. If Login or Register Failed, please try **Uninstall** the APP and **Reinstall** APP 
