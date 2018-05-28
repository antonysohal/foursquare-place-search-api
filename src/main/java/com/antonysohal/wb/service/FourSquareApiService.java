package com.antonysohal.wb.service;

import com.antonysohal.wb.domain.Location;
import com.antonysohal.wb.domain.LocationVenueRecommendations;
import com.antonysohal.wb.domain.Venue;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Four Square API Service
 */
@Service
public class FourSquareApiService implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(FourSquareApiService.class);

    @Autowired
    RestTemplate restTemplate;

    @Value("${foursquare.api.endpoint}")
    String fourSquareApiEndpoint;

    @Value("${foursquare.api.client.id}")
    String fourSquareClientId;

    @Value("${foursquare.api.client.secret}")
    String fourSquareClientSecret;

    @Value("${foursquare.api.version}")
    String fourSquareVersion;

    final static String VERSION_PARM = "v";

    final static String CLIENT_SECRET = "client_secret";

    final static String CLIENT_ID = "client_id";

    final static String NEAR = "near";

    final static String LIMIT = "limit";

    final static String SORT_BY_DISTANCE = "sortByDistance";

    final static String VENUE_RECOMMENDATIONS = "/venues/explore?v={v}&client_secret={client_secret}&client_id={client_id}&limit={limit}&near={near}&sortByDistance=1";

    /**
     * Returns a list of recommended Venues for a given <tt>nameSearch</tt> using the
     * <a href="https://developer.foursquare.com/docs/api/venues/explore">Four Square Venue Recommendations API</a>
     * @param nameSearch the name to search
     * @param limit number of results to return
     * @return a list of venues from the Four Square Venue Recommendations API
     */
    public LocationVenueRecommendations recommendations(String nameSearch, int limit) {
        Assert.hasText(nameSearch, "nameSearch must contain a value");
        LOG.debug("recommendations with parameter nameSearch='{}' and limit='{}'", nameSearch, limit);

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put(NEAR, nameSearch);
        uriVariables.put(LIMIT, limit);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(VENUE_RECOMMENDATIONS, String.class, uriVariables);

        String jsonResponse = responseEntity.getBody();

        LocationVenueRecommendations venueRecommendations = new LocationVenueRecommendations();
        venueRecommendations.setLocationName(JsonPath.parse(jsonResponse).read("$.response.geocode.displayString"));
        venueRecommendations.setLocation(JsonPath.parse(jsonResponse).read("$.response.geocode.center", Location.class));
        venueRecommendations.setVenues(JsonPath.parse(jsonResponse).read("$.response.groups[*].items[*].venue", new TypeRef<List<Venue>>() {}));
        return venueRecommendations;
    }


    /**
     * Sets up the RestTemplate to use the Four Square API Endpoint as default uri
     * Also check if the credential provided are valid
     * @throws Exception if the {@link #checkValidCredentials() checkValidCredentials()} return <tt>false</tt>
     */
    public void afterPropertiesSet() throws Exception {
        LOG.info("Setting Four Square API endpoint to {}", fourSquareApiEndpoint);

        Map<String, String> defaultUriVariables = new HashMap<>();
        defaultUriVariables.put(VERSION_PARM, fourSquareVersion);
        defaultUriVariables.put(CLIENT_ID, fourSquareClientId);
        defaultUriVariables.put(CLIENT_SECRET, fourSquareClientSecret);
        defaultUriVariables.put(LIMIT, "50");

        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory(fourSquareApiEndpoint);
        defaultUriBuilderFactory.setDefaultUriVariables(defaultUriVariables);
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);

        if (!checkValidCredentials()) {
            LOG.error("Invalid credentials for FourSquare API. Please check");
            throw new Exception("Invalid credentials for FourSquare API. Please check");
        }
    }

    /**
     * To test Four Square credentials are working run a quick explore for venue.
     * Alternatively, could have used /user/self endpoint
     * @return <ttt>true</ttt> if the endpoint using credential return a 200 http code, otherwise <tt>false</tt>
     */
    boolean checkValidCredentials() {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put(NEAR, "London, UK");
        uriVariables.put(LIMIT, 1);
        return restTemplate.getForEntity(VENUE_RECOMMENDATIONS, String.class, uriVariables).getStatusCode() == HttpStatus.OK;
    }

}

