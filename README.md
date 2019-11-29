## CMPUT 301 Team 14 Project Part 4

### APP Name: Mooditude

### Android Requirement

-  Android API Level 26 (Android 8 :Oreo) Required At Minimum
-  Android API level 29 will not support the camera feature(cant create directory to save file)
-  Google APIs are required for Emulators

### Register

- For a new user to register, username and emails must be unique
- Password length must be >= 6

### About Google Service API Key

- `google-services.json` is required for connection to the database. This file excluded from the project folder due to security issues.
- `google-services.json`  is uploaded to CMPUT 301 Team 14 Slack Chanel (FOR TA and Team Members)
- `google-services.json` needs to be put under `/Mooditude/App` folder for the APP to be correctly built. 

### About Google Map API

- The API authentication not only has the key which is already in the project source code in GitHub, it also requires device   fingerprint to be added to the api auth in Google Cloud Platform when running on an android emulator.

### Known Issues:

- Firebase connection may be **Unstable** When Running on Android Emulators. If Login or Register Failed, please try **Uninstall** the APP and **Reinstall** APP 
- IntentTest(UITest) sometimes failed to click the ListView to pop up the fragment resulting test failed, please rerun them

### Reference:

- Launch icon: https://www.eventbrite.co.uk/e/what-a-mood-the-launch-party-tickets-79769341247
- Drawable icons: https://icons8.com
