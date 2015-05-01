Homesafe - Developer Documentation
========
###Table of Contents
1. New to HomeSafe
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
this will create a local copy of the HomeSafe repository on your machine so you will now have access to the most recent copy of the source code.

Before you start anything, open your copy of the homesafe repository and make sure that you have all the following items:
```
$ cd homesafe/
$ ls
  HomeSafe README.md
$ cd HomeSafe/app/src/
$ ls
  androidTest  main
$ ls
  AndroidManifest.xml  java  res
$ cd java/cse403/homesafe
$ ls
  Data  HSTimer.java  Messaging  StartScreen.java  Trip.java
```
Note that when we first opened the folder there was a file titled README.md, which is a copy of the document you're reading right now.

P.S. if there are any problems at all grabbing the HomeSafe material using git then you can also simply download the zipped version of the HomeSafe source code folder from the Github [HomeSafe webpage](https://github.com/Vivekparam/homesafe "HomeSafe on Github") to get the contents directly.

###Building HomeSafe
There are several options for this step that all boil down to the preference of you, the developer. Android apps are all written in Java; in order to compile and run the HomeSafe source code you need both the [Android SDK](https://developer.android.com/sdk/index.html "Android SDK") and an IDE. I can recommend [Eclipse](https://eclipse.org/ "Eclipse") and [Android Studio](https://developer.android.com/tools/studio/index.html "Android Studio") for this purpose. Both Eclipse and Android Studio will run Android Java code and both allow use of an emulator and the testing of code on your android device. It's super neat and easy to get started with either of these if you don't have either installed. If you've never used either tool before, I highly recommend spending a half an hour to learn how to use them before continuing on.

Once you are inside your chosen IDE (I'm using Android Studio for this tutorial) open the project folder; the actual java source code is located in the HomeSafe/app/src/main/java/cse403/homesafe folder and all the layout source files you might need are located in the HomeSafe/app/src/main/res. Any user documentation you might need is on the [HomeSafe website](http://homesafe.github.io/ "HomeSafe website"), please read it if you need a high-level view of the product.

![alt text](README assets\screenshot1.png "screenshot1")

When you first open the project folder in your IDE you should get a file structure similar to the one shown in the above picture.

Once you have added any code you want and are ready to build locate and press the build button in your IDE (in Android Studio it is under the Build menu). You can then run the code and test it in an [Android Device Emulator](http://developer.android.com/tools/devices/emulator.html).

###Testing Your Software and Not Breaking Ours
Testing is very important (especially if you plan to add your code to our code). I cannot emphasize enough how important it is that you test your code thoroughly and make sure it builds and runs correctly before adding it to the repository. **DO NOT ADD BROKEN CODE**. If you don't know where to start in terms of testing here are a couple handy sites to get you started:
* [Android testing](http://tools.android.com/tech-docs/unit-testing-support)
* [JUnit Testing](http://www.javacodegeeks.com/2014/11/junit-tutorial-unit-testing.html)
* [JUnit Testing in Eclipse](http://help.eclipse.org/luna/index.jsp?topic=%2Forg.eclipse.jdt.doc.user%2FgettingStarted%2Fqs-junit.htm)

###Setting Up Automated Builds and Testing
to do tomorrow

###Releasing a New Version
Before adding anything to the repository please update the version name located in the Androidmanifest.xml and build.grade module:app files by incrementing the decimal portion by 1 (so 1.1 becomes 1.2 and 1.9 becomes 1.10 and so on). This is for good book keeping, and let's us know what is featured in which version. I've highlighted which items specifically must be changed in the below pictures.

![alt text](README assets/screenshot3.png "screenshot3")

![alt text](README assets/screenshot2.png "screenshot2")


The following command will archive your files into a tar ball that can then be added to the repository
```
tar czf <your_archive_name>.tar.gz <your_project_folder_name>
```

###Where to Find Our Bug List and Add to It
to do tomorrow
