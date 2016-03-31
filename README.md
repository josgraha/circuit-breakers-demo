# Circuit Breaker Demo

## Introduction

This demo illustrates a scenario where two microservices (Weather and News) are operating normally and being consumed by an aggregator service (News Aggregator) with Hystrix circuit breakers enabled.  This scenario then creates a failure condition in the News Service by deleting the underlying news entries in the backing news store.  With this failure condition in place the Hystrix circuit breakers will open and subsequent calls to the service with the circuit breaker enabled will increase stream via the metrics endpoint.  This endpoint is consumed by the Histrix dashboard providing a nice counter of failures vs. successes with the circuit open and closed respectively.  The data is then restored and the News Aggregator circuits are automatically closed by Histrix and dashboard reflects this return to normal operation when a traffic generator is used to produce a steady data stream.

## Software

* Vagrant [http://vagrantup.com]()
* Docker [https://www.docker.com]()
* Java 8 [http://java.com]()
* Postman (optional) [https://www.getpostman.com]()

## Prerequisites

To run the Weather Service, you need an API ID from Open Weather Map. Simply sign up here:

[http://home.openweathermap.org/users/sign_up]()

### Vagrant Services

This demo requires the *services* Vagrant Box. You can start it by running the following command from ```vagrant-boxes/```:

```
vagrant up services
```

### Docker

Run a Consul instance in Docker to enable quorum based discovery
```
docker run -p 8300:8300 -p 8301:8301 -p 8301:8301/udp -p 8302:8302 -p 8302:8302/udp -p 8400:8400 -p 8500:8500 -d progrium/consul -join 192.168.33.10 -advertise 192.168.99.100
```

Run the Hystrix dashboard in Docker
```docker run -p 7979:8080 arthurtsang/docker-hystrix-dashboard```

## Boostrapping the Environment

### Starting the Microservices

Open three terminal tabs for the following folders

* ```service-local-news```
* ```service-weather```
* ```service-news-gateway```

_Note: start the gateway service last_

Setup environment variables:
```source env.sh```
Run the service
```gradlew run```

## Testing the Environment

### Testing the Components
Open the Consul Dashboard
http://192.168.99.100:8500/ui/#
Expected: Services should be registered with no failures

### Populating the News Store
From Postman invoke the `load` service to populate the News Store with "lorem ipsum" data.
``` http://localhost:8081/load ```

Expected Result:
```
OK
```

### Testing the News Service
From Postman check that the News Service returns data:
``` http://localhost:8081/news/chicago ````

Sample data:
```
{
  "headline": "lobortis et rutrum accumsan mifusce risuscurabitur",
  "news": "Cubiliamollisnulla turpis aliquet et curabitur nam ridiculus curae;morbi conubia leo sociis tellus volutpataenean.  Sagittisamet sapien condimentum bibendum dapibus erosaliquam nisietiam vivamus blandit molestienam magna.  Nequeeu vitae diam nam aenean erat potenti consequat nam anteetiam turpis adipiscing potentialiquam donec nibhproin quisque quisque ad lacinia.  Etiamlacus suscipit condimentum anteetiam maurisclass acetiam accumsan sagittis porta dignissimphasellus vitae tinciduntpellentesque egestas curae;aliquam ligula tortor quisque torquent aliquet.  Velarcu suscipit adipiscing egestasmauris erat vulputate scelerisque laoreet consectetur quam nam non molestie nullamauris turpis imperdiet laoreet nam sit.  Ametvivamustempus acsuspendisse amet consequatphasellus neque nisl fusce phasellus posuere gravida posuere dapibus.  \nInterdumornarenam dolor consectetur diam dictumst sapienmauris mollis curabitur dis parturient elementumquisque sociis suscipit tincidunt volutpatfusce pharetra.  Quaminceptos egestas lobortis ac fusce vitaesed vel potenti erosdonec maurisclass aliquam odio ante consequat.  \nUtnon ante donec nisl arcu rutrum eudonec.  Velpharetra egestaspraesent scelerisque laoreet fringilla eleifend luctus penatibus habitant ipsum id."
}
```

### Testing the Weather Service
From Postman check that the Weather Service returns data:
``` http://localhost:8082/weather/chicago ```

Sample data:
```
{
  "conditions": "Clouds",
  "description": "overcast clouds",
  "temperature": "50.0°F",
  "pressure": "1011mb",
  "humidity": "50%",
  "city": "Chicago"
}
```

### Testing the Aggregator
From Postman check that the News Aggregator Service returns data:
``` http://localhost:8080/city/chicago/news ```

Sample data:
```
{
  "city": "chicago",
  "weather": {
    "conditions": "Clouds",
    "description": "overcast clouds",
    "temperature": "50.0°F",
    "pressure": "1011mb",
    "humidity": "50%",
    "city": "Chicago"
  },
  "localNews": {
    "headline": "semper ut nisifusce",
    "news": "Lobortisiaculis aliquam risuscurabitur non.  Nostrasodales aliquam ametvivamus dapibus euismod volutpatmauris penatibus augue pretium eu porttitor.  Imperdietquis dignissimphasellus ullamcorper nec curae;morbi mollis aliquet quam lobortis ac.  Potentialiquamat congue erosdonec justo cursus parturient ligula rutrum nullam pretium erosaliquam.  Scelerisquescelerisque nisi neque urna pretium nislnulla nibh potentialiquam bibendum litora cursus ipsum velit congue.  Cumnondonec volutpatmauris laoreet hendrerit dui turpis egestas tristique venenatis taciti placerat.  \nElementumviverra quam per neque sapienmauris quis nondonec lobortis torquent.  Diamcursus erat felis nunc dictumstduis venenatis himenaeos velit sociosqu.  \nSemmagnis tellusmaecenas montes consectetur dis viverra dictum conubia senectus justo primis dignissim eget nunc amet.  Ridiculusvolutpatfusce id bibendum porta placerat facilisi turpis hac dignissimphasellus eudonec curae;morbi molestie volutpatfusce aptent eros aptent volutpataenean molestienam dictum.  Sodalesmollisnulla magnis conubia metus facilisis nisi suscipit suscipit volutpat augue eleifend inceptos egestasmauris accumsan urna hac nisl magna.  Proinporttitor parturient rutrum ipsum dictum pretium mattis platea eu nondonec metus tellusmaecenas molestie nunc.  Minisl morbi pellentesque volutpatmauris nascetur sem litora non mauris nuncdonec mi enim sem risus mattis egestas sed hendrerit.  Acsuspendissepotentialiquam justo cursus nibhproin sodales faucibus nam fermentumcurabitur tellusmaecenas laoreet non consectetur euismod."
  }
}
```

### Launching the Hystrix Dashboard

In a web browser open the dashboard.

[http://192.168.99.100:7979]()

Enter the stream url in the text input.

``` http://192.168.33.1:8080/hystrix.stream ```

Click the `Add Stream` button.

Click the `Monitor Streams` button.

### Starting the Traffic

The traffic generator invokes a randomly generated list of cities against the News Aggregator service.

From the `traffic-builder` folder run the following:

``` ./traffic ```

### Delete the News Store Entries

From Postman invoke the `delete` service to clear out the News Store entries.  This will cause the News Service to fail health checks.  Consul will remove it from the service list and the News Aggregrator circuit breaker will trip, opening the circuit for the News Service.

``` http://localhost:8081/delete ```

Expected Result:
```
OK
```

### Testing the Aggregator

Invoke the aggregator url and the results should contain the default "No News" news object.

### Monitoring Dashboard

Check the Hystrix dashboard and watch the failure count increase.

### Restore the News Store Entries

Invoke the `load` endpoint on the News Service.

### Monitoring the Dashboard and Aggregator

* The dashboard should reflect increasing success counts
* The News Aggregator results should return to normal


## Important URLs

The following URLs are used in this lesson:

- Consul Server: ```http://192.168.33.10:8500```
- Consul Agent (Local Docker): ```http://192.168.99.100:8500```
- News Gateway Service running on localhost in IDE: ```http://localhost:8080/city/{city}/news```.
- News Gateway Service Hystrix Streaming Servlet: ```http://localhost:8080/hystrix.stream```
- Local News Service running on localhost in IDE: ```http://localhost:8081/news/{city}```. Example: ```http://localhost:8081/news/chicago``` returns a fake news article for Chicago IL
- Load News Articles into Elasticsearch for Local News Service: ```http://localhost:8081/load```
- Delete Elasticsearch index for Local News Service: ```http://localhost:8081/delete```
- Elasticsearch: ```http://192.168.33.10:9200```
- Weather Service running on localhost in IDE: ```http://localhost:8082/weather/{city}```. Example: ```http://localhost:8082/weather/chicago``` returns weather data for Chicago IL

## Application Configuration

The following environment variables are set when running **News Gateway Service**

- ```ADVERTISED_HOST=192.168.33.1```
- ```ADVERTISED_PORT=8080```
- ```CONSUL_HOST=192.168.99.100``` (Refers to local Consul running in Docker)

The following environment variables are set when running **Local News Service**

- ```ADVERTISED_HOST=192.168.33.1```
- ```ADVERTISED_PORT=8081```
- ```CONSUL_HOST=192.168.99.100``` (Refers to local Consul running in Docker)
- ```ELASTICSEARCH_HOST=192.168.33.10```

Since, we're running on localhost, we need to override ports for the backing services. In ```src/main/resources/application.conf```

```
microservice {
    name = "local-news-service"
    port = 8081
}
```

The following environment variables are set when running **Weather Service**

- ```ADVERTISED_HOST=192.168.33.1```
- ```ADVERTISED_PORT=8082```
- ```CONSUL_HOST=192.168.99.100``` (Refers to local Consul running in Docker)
- ```APP_ID=<your app ID>```


Since, we're running on localhost, we need to override ports for the backing services. In ```src/main/resources/application.conf```:

```
microservice {
    name = "weather-service"
    port = 8082
}
```

## Cleanup and Shutdown

### Halting the Services
Shutdown vagrant services
Run ```vagrant halt services```

Shutdown hystrix dashboard in Docker
