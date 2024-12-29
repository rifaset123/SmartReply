# SmartReply

**SmartReply** is an Android app that simulates a chat between two people. The app utilizes Google's ML Kit Smart Reply API to provide intelligent reply suggestions for each message in the conversation, creating an engaging and dynamic chat simulation.

## Features

- **Chat Simulation**: Simulate conversations between two users.
- **Smart Reply Suggestions**: Each message triggers AI-generated reply suggestions using ML Kit.
- **Dynamic Interaction**: Live chat experience with real-time smart replies.

## Screenshots
![Gambar WhatsApp 2024-12-29 pukul 13 34 06_e5c2ef80](https://github.com/user-attachments/assets/9bf5625f-6db9-433b-9d9a-e67532687818)
![Gambar WhatsApp 2024-12-29 pukul 13 34 05_6f42f5b1](https://github.com/user-attachments/assets/e87182fb-1e98-4597-8a36-58882285e131)
![Gambar WhatsApp 2024-12-29 pukul 13 34 45_728e240a](https://github.com/user-attachments/assets/caa2451b-e231-4b22-b92b-8e94a4be7e53)

## Getting Started

### Prerequisites

- Android Studio (latest version recommended)
- Minimum SDK: 26
- Compile SDK: 34

### Installation

1. Clone this repository:
    ```bash
    git clone https://github.com/rifaset123/SmartReply.git
    ```

2. Open the project in Android Studio.

3. Sync the project to download dependencies:
    - Go to **File > Sync Project with Gradle Files**.

4. Build and run the app on an emulator or physical device.

## Dependencies

The app uses the following libraries:

```groovy
// Core Android libraries
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.appcompat:appcompat:1.6.1")
implementation("com.google.android.material:material:1.10.0")
implementation("androidx.constraintlayout:constraintlayout:2.1.4")

// Testing
testImplementation("junit:junit:4.13.2")
androidTestImplementation("androidx.test.ext:junit:1.1.5")
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

// Lifecycle components for MVVM architecture
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

// ML Kit Smart Reply
implementation("com.google.mlkit:smart-reply:17.0.2")
