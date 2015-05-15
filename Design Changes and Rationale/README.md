Design changes:
 One of the major oversights of our initial Architecture was the fact that we failed to break up the project into Android Activities.

 Because of this, we had to perform a major refactoring of our architecture. The largest of these changes are described here. For more detailed explanation of small changes, see the System Design Document [Link to document on website]

  1. The classes which were once known as Trip and Timer have been combined into HSTimerActivity. This activity manages all aspects of a single trip. We made this change because we realized that the Trip itself was what Android would consider an Activity, and the state of the Timer is very linked to this. 
  Since we used Android's built in Timer class to manage the time of the trip, we didn't need our own Timer class anymore. Note that this activity, combining the timer functionality and the former Trip class, ended up being larger than previously planned. However, we decided this was for the best, because following Android design ideas, this was all still meant to be encapsulated in the same activity.

  2. DangerActivity was added -- This activity manages a particular instance of "danger." That is, when HSTimerActivity runs out, the application context switches to this Activity to manage the user entering their password, or sending and alert to contacts -- this functionality was pulled from the former Trip class. 
  This was done because we decided that the Danger screen, with its own countdown timer and messaging, represented a distinct Activity, separate from HSTimerActivity.

  3. Numerous activites have been added to manage the addition, deletion, and editing of Contacts and Destinations, such as AddContactActivity, and EditLocationActivity. 
  We had to add these classes because each one represents a separate task which the user could undertake, and had its own functionality for displaying messages and interacting with the database. This is simply the way Android works, and we didn't think to add these classes beforehand.

  4. In order to accomodate our Google Maps API requests, we added a new class, GoogleMapsUtils, which manages asynchronous requests to Google endpoints. 
  In order to deal with the callbacks that are necessary to properly return values, we also added an interface GoogleMapsUtilsCallback, which defines callbacks for each of the methods in GoogleMapsUtils. 

  5. In order to properly interface with Android's database in Java, we added DbFactory, HomeSafeContract, and HomeSafeDbHelper. This wrapper around the API calls required for interfacing with the Android database greatly simplified the rest of our code. 

  6. Initially, we had planned to write a Location class which stored a name, address, and coordinates. It turned out that something similar was already implemented in android -- a class also called Location.
  The Android.Location class, however, did not contain a name or address, but  Thus, we created the Destination class, which wraps a name, address, and an Android.Location. The constructor for this class, in order to make things simple for clients, automatically consults the Google API to convert passed-in addresses to Locations. In order to do this, we had to perform some clever wait() and notify() calls to make sure the client doesn't have to deal with the fact that the constructor is an asychronous operation.  


Two design patterns or principals
 Singleton:
  cse403.homesafe.Data.Contacts and cse403.homesafe.Data.Destinations are both Singleton classes. which were made globally available so their data could be accessed anywhere in our app without haveing to be explicitly construted anywhere else in the code. 

 Callbacks:
  cse403.homesafe.Util.GoogleMapsUtils has methods which are API calls to the Google Maps API. These methods operate asynchronously, and so need to take a callback parameter. We implemented this by writing an interface cse403.homesafe.Util.GoogleMapsUtilsCallback, an instance of which is passed in tothe asynchronous methods. When the method terminates, it calls the passed-in object's methods.
