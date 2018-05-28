package com.antonysohal.wb.service;

import com.antonysohal.wb.domain.LocationVenueRecommendations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FourSquareApiServiceTest {

    @Autowired
    FourSquareApiService fourSquareApiService;

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

    @Test
    public void recommendations_newport() throws Exception {
        LocationVenueRecommendations locationVenueRecommendations = fourSquareApiService.recommendations("newport", 5);
        assertThat(locationVenueRecommendations.getLocationName()).containsIgnoringCase("newport");
        assertThat(locationVenueRecommendations.getLocationName()).containsIgnoringCase("United States");
        assertThat(locationVenueRecommendations.getLocation()).isNotNull().hasFieldOrProperty("lat").hasFieldOrProperty("lng");
        assertThat(locationVenueRecommendations.getVenues()).size().isEqualTo(5);
    }

    @Test
    public void recommendations_london_uk() throws Exception {
        LocationVenueRecommendations locationVenueRecommendations = fourSquareApiService.recommendations("newport uk", 5);
        assertThat(locationVenueRecommendations.getLocationName()).containsIgnoringCase("newport");
        assertThat(locationVenueRecommendations.getLocationName()).containsIgnoringCase("United Kingdom");
        assertThat(locationVenueRecommendations.getLocation()).isNotNull().hasFieldOrProperty("lat").hasFieldOrProperty("lng");
        assertThat(locationVenueRecommendations.getVenues()).size().isEqualTo(5);
    }

    @Test(expected = HttpClientErrorException.class)
    public void recommendations_with_nonexisting_place() {
        fourSquareApiService.recommendations("fdfksjdk", 5);
    }

    @Test
    public void checkValidCredentials() {
        fourSquareApiService.checkValidCredentials();
    }

}