# FitLife Pro — Professional Video Workout App

**FitLife Pro** is a production-oriented native Android fitness application engineered with Jetpack Compose, Material 3, Android Media3 ExoPlayer, and Room Database. Designed for Bangladesh and international fitness enthusiasts, FitLife Pro brings gym-quality video instruction, customizable workout programs, local data persistence, and bilingual Bengali/English localization.

---

## 1. Ownership & Developer Attribution

* **App Name:** FitLife Pro
* **Category:** Health & Fitness
* **App Owner:** Md Jahangir Alam
* **App Creator & Developer:** Md Jahangir Alam
* **Contact Email:** `mdjahangiralam2528@gmail.com`
* **Contact Phone:** `+8801778600028`
* **Initial Target Market:** Bangladesh and International
* **Primary Language:** Bengali (`বাংলা`)
* **Secondary Language:** English
* **Application ID:** `com.mdjahangiralam.fitlifepro`
* **Version:** 1.0.0 (Build 1)
* **Copyright Notice:** `© 2026 Md Jahangir Alam. All rights reserved.`

Centralized configuration is maintained in `app/src/main/java/com/example/config/AppConfig.kt`.

---

## 2. Key Architecture & Features

### Core Technology Stack
* **Language & Runtime:** Kotlin exclusively, Coroutines & Flow
* **UI & Design:** Jetpack Compose with Material Design 3 (Dynamic Color, Dark & Light mode)
* **Video Streaming & Playback:** Android Media3 ExoPlayer (`androidx.media3:media3-exoplayer`) with custom controls (play/pause, 10s seek, speed adjustment `0.75x - 1.5x`, full-screen, mute toggle, error retry handling, and lifecycle listeners)
* **Local Persistence:** Room Database (`androidx.room`) with Flow-based reactive queries for workout records, daily calendar streaks, favorite exercises, and offline downloads
* **Preferences:** Jetpack DataStore Preferences for user profiles, age category, workout goals, language, and theme
* **Notifications & Reminders:** Android `NotificationManager` and `AlarmManager` with `BOOT_COMPLETED` rescheduling receiver
* **Navigation:** Navigation Compose (`androidx.navigation.compose`)

### 21 Complete Native Screens
1. **Splash Screen:** Animated brand emblem with owner attribution
2. **Onboarding Screen:** High-energy photography and core fitness highlights
3. **Language Selection:** Instant toggling between Bengali (বাংলা) and English
4. **User Profile & Fitness Preferences:** Goal selection, teen-safe routines, duration preference
5. **Home Dashboard:** Personalized greetings, motivational quotes, Today's Workout, Continue Workout card, and genuine real-time statistics
6. **Workout Categories:** 13 complete categories (Full Body, Abs & Core, Chest, Arms, Shoulders, Back, Legs, Stretching, Warm-up, Beginner, Cardio, Mobility, Yoga)
7. **Workout Program Details:** 7-Day, 14-Day, 28-Day, and Teen-Safe routines
8. **Exercise Video Player:** Media3 player with speed controls, form tips, and safety instructions
9. **Active Workout Session:** Step-by-step progress, countdown timer, video demonstration, pause/skip/previous controls
10. **Rest Timer:** Circular countdown animation, +10s boost, and Next Exercise preview
11. **Daily Workout Calendar:** 14-day tracking grid, streak badges, and session logs
12. **Workout Completion Summary:** Duration breakdown, exercises completed, transparent non-medical calorie burn estimate, and personal notes
13. **Progress Dashboard:** Total workouts, minutes trained, weekly activity bar chart
14. **Workout History:** Chronological log with individual delete and bulk clear options
15. **Downloaded Videos:** Offline media manager with storage usage in MB and delete controls
16. **Favorites Screen:** Bookmarked movements for rapid access
17. **Reminder Settings:** Scheduled alarms with Android 13+ `POST_NOTIFICATIONS` runtime permission and instant test notification
18. **Profile and Settings:** Preference editor, theme switcher, and local data reset
19. **About & Owner Information:** Full creator credentials, contact details, and legal disclosure
20. **Privacy Policy:** Transparent zero-tracking, offline-first privacy statement
21. **Help & Support:** FAQs on workout safety, streaming tips, and direct email/phone contact buttons

---

## 3. Google Play Release & Build Instructions

### Building Signed APK / App Bundle (AAB)

To generate a signed release build for deployment to Google Play Console:

1. **Generate a Production Keystore:**
   ```bash
   keytool -genkey -v -keystore my-upload-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload
   ```

2. **Set Environment Variables:**
   ```bash
   export KEYSTORE_PATH="/path/to/my-upload-key.jks"
   export STORE_PASSWORD="your-keystore-password"
   export KEY_PASSWORD="your-key-password"
   ```

3. **Build the Android App Bundle (AAB for Google Play):**
   ```bash
   gradle :app:bundleRelease
   ```
   Output file: `app/build/outputs/bundle/release/app-release.aab`

4. **Build the Signed APK (for direct device installation):**
   ```bash
   gradle :app:assembleRelease
   ```
   Output file: `app/build/outputs/apk/release/app-release.apk`

---

## 4. Google Play Store Pre-Launch Checklist

- [x] **Application ID:** Set to unique `com.mdjahangiralam.fitlifepro`
- [x] **App Name:** `FitLife Pro` (12 characters, compliant with <30 char rule)
- [x] **Adaptive Launcher Icon:** High-resolution 108dp adaptive launcher icon generated with mipmap PNG raster fallbacks
- [x] **Privacy Policy:** Published URL configured with transparent offline data explanation
- [x] **Permissions:** Least-privilege only (`INTERNET`, `ACCESS_NETWORK_STATE`, `POST_NOTIFICATIONS`, `VIBRATE`, `RECEIVE_BOOT_COMPLETED`)
- [x] **Families Policy & Teen Safety:** Non-extreme movement routine tailored for youth; no adult bodybuilding or unsafe weight-loss advice
- [x] **Health Disclaimer:** Clear disclosures that FitLife Pro provides educational fitness guidance and is not a substitute for professional medical advice
