![GitHub Cards Preview](https://github.com/Spikeysanju/Expenso/blob/master/art/EXPENSO-ANDROID.png?raw=true)

# Expenso 📊
A Simple Expense Tracker App 📱 built to demonstrate the use of modern android architecture component with MVVM Architecture 🏗. *Made with love ❤️ by [Spikeysanju](https://github.com/Spikeysanju)*

<br />

***Try latest Expenso app apk from below 👇***

[![Expenso](https://img.shields.io/badge/Expenso-APK-black.svg?style=for-the-badge&logo=android)](https://github.com/Spikeysanju/Expenso/releases/download/v1.0.0-alpha01/Expenso.apk)

<br />

## UI Design 🎨

***Click to View Expenso app Design from below 👇***

[![Expenso](https://img.shields.io/badge/Expenso-FIGMA-black.svg?style=for-the-badge&logo=figma)](https://www.figma.com/file/Z5KMfiwo9RYtYBUMRSIfHh/Expense-Tracker-App?node-id=140%3A1016)

<br />

## Day Mode 🌞
Dashboard | All Income | All Expense | Details | Add Transaction 
--- | --- | --- |--- |--- 
![](https://github.com/Spikeysanju/Expenso/blob/master/art/DASHBOARD.png) | ![](https://github.com/Spikeysanju/Expenso/blob/master/art/INCOME.png) | ![](https://github.com/Spikeysanju/Expenso/blob/master/art/EXPENSE.png) | ![](https://github.com/Spikeysanju/Expenso/blob/master/art/DETAILS.png) | ![](https://github.com/Spikeysanju/Expenso/blob/master/art/ADD-TRANSACTION.png) 

<br />

## We Support Dark Mode Too 🌚
Dashboard | All Income | All Expense | Details | Add Transaction 
--- | --- | --- |--- |--- 
![](https://github.com/Spikeysanju/Expenso/blob/master/art/DARK-DASHBOARD.png) | ![](https://github.com/Spikeysanju/Expenso/blob/master/art/DARK-INCOME.png) | ![](https://github.com/Spikeysanju/Expenso/blob/master/art/DARK-EXPENSE.png) | ![](https://github.com/Spikeysanju/Expenso/blob/master/art/DARK-DETAILS.png) | ![](https://github.com/Spikeysanju/Expenso/blob/master/art/DARK-ADD-TRANSACTION.png) 

<br />


## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [Stateflow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) - StateFlow is a state-holder observable flow that emits the current and new state updates to its collectors. 
  - [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) - A flow is an asynchronous version of a Sequence, a type of collection whose values are lazily produced.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - SQLite object mapping library.
  - [Jetpack Navigation](https://developer.android.com/guide/navigation) - Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app
  - [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers. DataStore uses Kotlin coroutines and Flow to store data asynchronously, consistently, and transactionally.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
- [Figma](https://figma.com/) - Figma is a vector graphics editor and prototyping tool which is primarily web-based.

<br />

## Package Structure 📦
    
    dev.spikeysanju.expenso # Root Package
    ├── di                  # Hilt DI Modules 
    ├── data                # For data handling.
    │   ├── local           # Local Persistence Database. Room (SQLite) database
    |   │   ├── dao         # Data Access Object for Room   
    |   |   |── database    # Database Instance
    |
    ├── model               # Model classes [Transaction]
    |
    |-- repo                # Used to handle all data operations
    |
    ├── view                # Activity/Fragment View layer
    │   ├── main            # Main root folder
    |   │   ├── main        # Main Activity for RecyclerView
    |   │   └── viewmodel   # Transaction ViewModel
    |   │   ├── adapter     # Adapter for RecyclerView
    │   ├── Dashboard       # Dashboard root folder
    |   |   |__ dashboard   # Dashboard 
    │   ├── Add             # Add Transaction root folder
    |   |   |__ add         # Add Transaction 
    │   ├── Edit            # Edit Transaction root folder
    |   |   |__ edit        # Edit Transaction
    │   ├── Details         # Add Transaction root folder
    |   |   |__ details     # Transaction Details
    │   ├── About           # About root folder
    |   |   |__ about       # About 
    │   ├── Dialog          # All Dialogs root folder
    |   |   |__ dialog      # Error Dialog 
    ├── utils               # All extension functions


<br />


## Architecture 🗼
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://github.com/TheCodeMonks/Notes-App/blob/master/screenshots/ANDROID%20ROOM%20DB%20DIAGRAM.jpg)

## Build-tool 🧰
You need to have [Android Studio Beta 3 or above](https://developer.android.com/studio/preview) to build this project.
<br>
<img src="./beta_android.png" height="200" alt="Beta-studio"/>

<br>

## Ohh You want iOS App Too? 📱 
Well, we've iOS version here, Checkout the iOS version of this app <a href="https://github.com/sameersyd/Expenso">Expenso</a>

<br />

## Contribute 🤝
If you want to contribute to this app, you're always welcome!
See [Contributing Guidelines](https://github.com/Spikeysanju/Expenso/blob/master/CONTRIBUTION.md). 

<br>

## Contact 📩
Have an project? DM us at 👇

Drop a mail to:- spikeysanju98@gmail.com

<br>

## Donation 💰
If this project help you reduce time to develop, you can give me a cup of coffee :) 

<a href="https://www.buymeacoffee.com/Li0hsl4" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/yellow_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>

<br>


## Credits 🤗

- 🤓 Icons are from [tablericons.com](https://tablericons.com) 
- 📄 Thanks for [NotyKT](https://github.com/PatilShreyas/NotyKT)

<br />

## License 🔖
```
    Apache 2.0 License


    Copyright 2021 Spikey sanju

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

```

