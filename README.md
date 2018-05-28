

## Prerequisite

You must have the following installed:
1. [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - the programming language used
2. [maven](https://maven.apache.org/) - build tool to compile, test and package application
3. [curl](https://curl.haxx.se/) - to run test
4. [jq](https://stedolan.github.io/jq/download/) - to view json in bash window

### Environment Variables
You must have the following environment variables to run application. 
Substitute for real values and run the following command.
```
export FOURSQUARE_CLIENT_ID='<ADD_CLIENT_ID>'
export FOURSQUARE_CLIENT_SECRET='<ADD_CLIENT_SECRET>'

```

## How to run

To run unit tests, integration tests and package 
```bash
mvn package

```

To run
```bash
./target/foursquare-place-search-api-1.0-SNAPSHOT.jar

```

## API Endpoint

### Search for place and get recommend venues near by

Returns a list of recommended venues sorted by distance near a place return by a name search.

```
GET http://localhost:8080/venues/searchAndRecommend

```

**Parameters**

Name | Required | Description | Example
---- | -------- | ----------- | -------
search | yes | A string naming a place. The resulting response will contain the correct formatted place name. If no place is found the response be a 500 server error. | "walthamstow" "newport uk"
limit | no - default to 50 | Number of venues to return. Defaults to 50

Try these

```bash
   curl -X GET "http://localhost:8080/venues/searchAndRecommend?search=walthamstow&limit=3" | jq .
```

The response will contain the correct format of the place searched with a lat/long and a list of venues.


Expected Response
```json
{
  "locationName": "Walthamstow, Greater London, United Kingdom",
  "location": {
    "lat": 51.59067,
    "lng": -0.02077
  },
  "venues": [
    {
      "id": "4e95a40c6c25df8da7b84eeb",
      "name": "The Chequers",
      "location": {
        "address": "145 High St",
        "lat": 51.58335534383981,
        "lng": -0.02665196958597967,
        "cc": "GB",
        "country": "United Kingdom",
        "labeledLatLngs": [
          {
            "label": "display",
            "lat": 51.58335534383981,
            "lng": -0.02665196958597967
          }
        ],
        "formattedAddress": [
          "145 High St",
          "Walthamstow",
          "Greater London",
          "E17 7BX",
          "United Kingdom"
        ],
        "city": "Walthamstow",
        "postalCode": "E17 7BX",
        "state": "Greater London"
      },
      "hereNow": {
        "count": 0,
        "summary": "Nobody here",
        "groups": []
      },
      "stats": {
        "tipCount": 0,
        "usersCount": 0,
        "checkinsCount": 0,
        "visitsCount": 0
      },
      "contact": {},
      "verified": false,
      "categories": [
        {
          "id": "4bf58dd8d48988d11b941735",
          "name": "Pub",
          "pluralName": "Pubs",
          "shortName": "Pub",
          "icon": {
            "prefix": "https://ss3.4sqi.net/img/categories_v2/nightlife/pub_",
            "suffix": ".png"
          },
          "primary": true
        }
      ],
      "photos": {
        "count": 0,
        "groups": []
      },
      "beenHere": {
        "count": 0,
        "lastCheckinExpiredAt": 0,
        "marked": false,
        "unconfirmedCount": 0
      }
    }
    ... ommmited
  ]
}
```

### References 
* Four Square API - https://developer.foursquare.com/docs/api/venues/explore
* Spring Boot 2 Reference - https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/
* JsonPath Reference - https://github.com/json-path/JsonPath
* Json to Pojo - http://www.jsonschema2pojo.org/
* Spring Webclient Testing - https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/