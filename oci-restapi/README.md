OpticalConnectivityIntent over ONOS Rest API
============================================

This file contains a new codec (OpticalConnectivityIntentCodec.java), and specific edited
java files in "onos-rest", and "onos-core-common" maven projects for the codec integration 
to ONOS 1.3 (Drake). With this codec, it is possible to create End-to-End optical routes using the ONOS 
Rest API instead of the CLI.

### POST Json template:

```
{
  "type": "OpticalConnectivityIntent",
  "appId": "org.onosproject.<App>",
  "src": {
    "port": "<Port-number>", //Has to be an OchPort number
    "device": "<Device-Id>" //Example: of:0000ffffffffff01
  },
  "dst": {
    "port": "<Port-number>", //Has to be an OchPort number
    "device": "<Device-Id>" //Example: of:0000ffffffffff02
  },
  "isBidirectional": {
    "isBidirectional": <Boolean>
  }
}

```
### How to implement it:

* Replace the edited java files with the ones installed in your ONOS distribution.
* Copy the OpticalConnectivityIntentCodec.java file into the org.onosproject.codec.impl package.
* Rebuild "onos-rest" and "onos-core-common" using Maven.
