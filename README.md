Homesafe - Developer Documentation
========
###Table of Contents
1. New to HomeSafe
 * Our vision
2. Getting Started
 * Cloning the repository
 * Building HomeSafe
 * Version control and you!
 * Android development and you!
3. Using HomeSafe
 * HomeSafe basics overview
 * Adding a contact
 * Setting up a trip
 * Ending a trip
 * Sending a message
 * Settings
5. Contact HomeSafe
 * Common questions
 * Report a bug

###New to HomeSafe?
New to the HomeSafe security app? Read the [short product description](https://docs.google.com/document/d/1mRl2jZ4gIVV2BKpTckHCqAkJ_6wEhcdgAwXyakDqQ3E/edit "HomeSafe product description") to find out more about it!

###Getting Started
The HomeSafe Security app is an open-source safety app for Android devices. In order to use the app or contribute to the project, you will first need to download the repository from the Github webpage:
* Never used Github before? No problem, start [here](https://github.com/ "Github")!
* For everyone else here's a handy [Github cheatsheet](https://training.github.com/kit/downloads/github-git-cheat-sheet.pdf "Github cheatsheet")!

###Cloning the Repository
Open a terminal window (or a Git terminal window for non-Linux users) and enter
```
git clone https://github.com/Vivekparam/homesafe
```
this will create a local copy of the HomeSafe repository on your machine so you will now have access to the most recent copy of the source code and executable.

Before you start anything, open your copy of the homesafe repository and make sure that you have all the material you should:
```
$ cd homesafe/
$ ls
  HomeSafe README.md
$ cd HomeSafe/
$ ls
  app          gradle            gradlew     HomeSafe.iml
  build.gradle gradle.properties gradlew.bat settings.gradle
```
Note that when we first opened the folder there was a file titled README.md. If you get lost or confused please read it, it contains lots of useful information about HomeSafe.

P.S. if there are any problems at all grabbing the HomeSafe material using git or you have no interest in adding to the project and only want to develop it on your own, then you can also just download the zipper version of the HomeSafe folder from the Github [HomeSafe webpage](https://github.com/Vivekparam/homesafe "HomeSafe on Github") to get the contents directly.

###Building HomeSafe
There are several options for this step that all boil down to the preference of you the developer. Android apps are written in Java so in order to compile and run the HomeSafe source code you need both the [Android SDK](https://developer.android.com/sdk/index.html "Android SDK") and an IDE to look at the code in. I can recommend [Eclipse](https://eclipse.org/ "Eclipse") and [Android Studio](https://developer.android.com/tools/studio/index.html "Android Studio") for this purpose. Both Eclipse and Android Studio will run Android Java code and both allow use of an emulator and the testing of code on your android device. It's super neat and easy to get started with either of these if you don't have either installed. If you've never used either tool before, I highly recommend spending a half an hour to learn how to use either before continuing on.

Once you have your IDE installed and set up, you can open the source files located in the HomeSafe/app/src/main/java/cse403/homesafe folder. user documentation is on the website.
Layouts for the app are in HomeSafe/app/src/main/res.
Press the build button on Android studio


###Layout

###



* How to build the software (e.g., compile it from the original sources).
* How to test their software
* How to set up automated build + test
* How to release a new version
 * update revision nums
 * create zip file
 * copy files to site
 * checking everything works
* where to find list of bugs / how to resolve bugs
* source code documentation + rep invariants
