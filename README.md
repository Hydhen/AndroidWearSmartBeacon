# AndroidWearSmartBeacon

This is a project made in collaboration with LCL (French bank) and SmartBeacon (company which builds devices related to iBeacons technologie).

This project consists of :
* a sub-project composed of:
  * a smartwatch application (PLV_alert_wear)
  *  a smartphone application (PLV_alert_mobile)
* a sub-project composed of:
  * a smartwatch application (Account_alert_wear)
  * a smartphone application (Account_alert_mobile)
* a smartphone project (BeaconSetter)

### PLV_alert scenario:

When the user goes next to a LCL advertising, the smartwatch service (PLV_alert_wear) detects the beacon and pops up a question in a notification.
The question is "Do you want more information about this advert ?" If the user clicks on "yes", it receives an e-mail.

### Account_alert scenario:

When the user goes next to a LCL bank, it receives a notification displaying its current account's state, and its account within the next 5 and 10 days (according to previsions).
It also displays the accounts of the whole family.

### BeaconSetter:

A configuration application which defines the server's url, the current username, the beacons to detect, etc...
