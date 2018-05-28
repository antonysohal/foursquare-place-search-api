package com.antonysohal.wb.web;

import com.antonysohal.wb.domain.Location;
import com.antonysohal.wb.domain.LocationVenueRecommendations;
import com.antonysohal.wb.domain.Venue;
import com.antonysohal.wb.service.FourSquareApiService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FourSquareVenueApiController.class)
public class FourSquareVenueApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FourSquareApiService mockFourSquareApiService;

    LocationVenueRecommendations locationVenueRecommendations;

    @Before
    public void setUp() {
        List<Venue> venueList = new ArrayList<>();
        for (int i = 0; i < 10 ;i++) {
            venueList.add(createVenue("Pizza Hut " + i, String.valueOf(i), 51.509, 0.123));
        }
        locationVenueRecommendations = new LocationVenueRecommendations();
        locationVenueRecommendations.setVenues(venueList);
    }


    @After
    public void tearDown() {
        locationVenueRecommendations = null;
        reset(mockFourSquareApiService);
    }

    @Test
    public void recommendations_endpoint() throws Exception {
        locationVenueRecommendations.setLocationName("Cardiff");
        locationVenueRecommendations.setLocation(new Location(51.481583, -3.179090));

        String nameSearch = "cardiff";

        when(mockFourSquareApiService.recommendations(nameSearch, 10)).thenReturn(locationVenueRecommendations);

        mockMvc.perform(
                get("/venues/searchAndRecommend")
                        .param("search", nameSearch)
                        .param("limit", "10")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)

                 )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.locationName", is("Cardiff")))
                .andExpect(jsonPath("$.venues", hasSize(10)));

        verify(mockFourSquareApiService, times(1)).recommendations(nameSearch,10);
    }

    Venue createVenue(String venueName, String id, double lat, double lng) {
        Venue venue = new Venue(venueName);
        venue.setId(id);
        venue.setLocation(new Location(lat, lng));
        return venue;
    }

}