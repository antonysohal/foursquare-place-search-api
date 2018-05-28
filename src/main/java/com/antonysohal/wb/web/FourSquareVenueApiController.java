package com.antonysohal.wb.web;

import com.antonysohal.wb.domain.LocationVenueRecommendations;
import com.antonysohal.wb.service.FourSquareApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(
    value = "/venues",
    produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE}
)
public class FourSquareVenueApiController {

    private static final Logger LOG = LoggerFactory.getLogger(FourSquareVenueApiController.class);

    @Autowired
    FourSquareApiService fourSquareApiService;

    @GetMapping(value = "/searchAndRecommend")
    @PostMapping(value = "/searchAndRecommend")
    public ResponseEntity<LocationVenueRecommendations> searchAndRecommend(@RequestParam String search,
                                                                           @RequestParam(required = false, defaultValue = "50") int limit) {
        return new ResponseEntity<>(fourSquareApiService.recommendations(search,  limit), HttpStatus.OK);
    }
}
