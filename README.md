Homesafe
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
The HomeSafe Security app is an open-source safety app for Android devices. In order to use the app or contribute to the project, you first need to download the repository from the Github webpage:
* Never used Github before? No problem, start [here](https://github.com/ "Github")!
* For everyone else here's a handy [Github cheatsheet](https://training.github.com/kit/downloads/github-git-cheat-sheet.pdf "Github cheatsheet")!

###Cloning the Repository
Open a terminal window (or a Git terminal window for non-Linux users) and enter
```
git clone https://github.com/Vivekparam/homesafe
```
this will create a local copy of the HomeSafe repository on your machine so you will now have access to the most recent copy of the source code and executable.

###Building HomeSafe
Now that you have a local version of the repository you can open the folder and look at it's contents.
```
$ cd homesafe/
$ ls
  HomeSafe README.md
$ cd HomeSafe/
$ ls
  app          gradle            gradlew     HomeSafe.iml
  build.gradle gradle.properties gradlew.bat settings.gradle
```

Documentation - Dev (Github, linked from website)
1. all data structures/classes need rep invariant
2. how t obtain source (link on website to github repo)
3. layout of directory
4. how to build source
5. how to test software
6. how to set up automated build + test
7. how to release new version
 * updating revision #s in code
 * creating a zip for source
8. how to resolve a bug
9. source code documentation.  
