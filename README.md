# FLACd
## Overview
Music has always been a vital form of expression and connection. However, in the current landscape, music discovery is often driven by algorithms rather than personal recommendations.

FLACd addresses the gap between algorithm-driven discovery and personal curation. The platform will allow users to share their taste in music, discover new music through their social network, and create customizable, shareable lists.

## Features
- **Browse Albums:** Search and explore albums fetched from the Discogs API.
- **Responsive Design:** Seamless user experience across devices.
- **User Profiles:** Users can create profiles with their name, email, bio and upload an avatar.
- **List Curation**: Users can create lists of albums and give them a title and a description.

## Installation
Before you begin, ensure you have the following:
- **Android Studio** installed (latest stable version recommended).
- **Java Development Kit (JDK)** version 8 or higher installed.
- **Android SDK** set up.
- **Gradle** installed (if not included with Android Studio).
- **API Key for Discogs API**: Make sure to have your own Discogs API key set up and stored in `gradle.properties`.

### 1. Clone the repository
To get started with FLACd, clone the repository to your local machine.
```
git clone https://github.com/obafisoye/flac-d
```
### 2. Open the project in Android Studio
Open Android Studio and select `Open an existing project`. Navigate to the project folder you just cloned and open it.
### 3. Set up API Key for Discogs
You’ll need to add your Discogs API token to the `gradle.properties` file to enable API access.
### 4. Configure Permissions
Make sure to grant necessary permissions for the app to access the internet. These should already be added in the `AndroidManifest.xml` file.
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```
### 5. Build the Project
### 6. Run the App
