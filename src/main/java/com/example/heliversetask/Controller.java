package com.example.heliversetask;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Controller {
    @Autowired
    Service service;

    @GetMapping(path = "/getFollowerCount")
    public ResponseEntity<Map<String, Integer>> getFollowerCount(
            @RequestParam(value = "insta", required = false) String insta,
            @RequestParam(value = "twitch", required = false) String twitch,
            @RequestParam(value = "twitter", required = false) String twitter,
            @RequestParam(value = "youtube", required = false) String youtube
    ){
        Map<String, Integer> resp = new HashMap<>();
        if(insta != null)
            resp.put("instagram", service.getInstagramFollowers(insta));
        if(twitch != null)
            resp.put("twitch", service.getTwitchFollowers(twitch));
        if(youtube != null)
            resp.put("youtube", service.getYoutubeSubscribers(youtube));
        if(twitter != null)
            resp.put("twitter", service.getTwitterFollowers(twitter));
        return ResponseEntity.ok(resp);
    }
}
