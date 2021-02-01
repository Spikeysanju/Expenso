

![GitHub Cards Preview](https://github.com/TheCodeMonks/Notes-App/blob/master/screenshots/NOTZZ%20APP%20CARD.jpg?raw=true)

# Expenso
A Simple Expense Tracker App 📱 built to demonstrate the use of modern android architecture component with MVVM Architecture 🏗. *Made with love ❤️ by [Spikeysanju](https://github.com/Spikeysanju)*


***Try latest Notzz app apk from below 👇***

[![Expenso](https://img.shields.io/badge/Expenso📊-APK-black.svg?style=for-the-badge&logo=android)](https://github.com/TheCodeMonks/Notzz-App/releases/download/v1.1/notzz.apk)



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


# Package Structure
    
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
    |   │   └── viewmodel   # ViewHolder for RecyclerView 
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

 

 This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://github.com/TheCodeMonks/Notes-App/blob/master/screenshots/ANDROID%20ROOM%20DB%20DIAGRAM.jpg)


## Contribute
If you want to contribute to this library, you're always welcome!
See [Contributing Guidelines](https://github.com/TheCodeMonks/Notzz-App/blob/master/CONTRIBUTION.md). 

## Contact
Have an project? DM us at 👇

Drop a mail to:- spikeysanju98@gmail.com

# Donation
If this project help you reduce time to develop, you can give me a cup of coffee :) 

[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/paypalme2/spikeysanju)


## License
```
MIT License

Copyright (c) 2020 Spikey Sanju

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```




