# Actual Price Calculator

This app displays the selling price of an item AFTER tax.
Currently this application only works in the state of California.

## Usage
The apk can be found under `app/release/app-release.apk`. After downloading the apk, you may need to scan it as it's not trusted by Android yet.

The user can input the price in the top field and needs to select which county they're from from the dropdown list.

## Build
Building the app yourself requires you to have Android Studio pre-installed. It can be downloaded at https://developer.android.com/studio

You need to have git installed on your PC to clone it.
1. Open the terminal and migrate to the `~/AndroidStudioProjects` directory or any directory where you store your Android Studio projects
2. Clone this repository by entering the command `git clone https://github.com/PJ-004/ActualPriceCalculator`
3. In Android Studio go to File -> New -> Import Project and then select ActualPriceCalculator from the directory you placed it in
4. After the project opens up, Gradle will start building the dependencies required, you can then build the project under the build menu