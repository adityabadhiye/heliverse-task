package com.example.heliversetask;

import com.google.gson.*;
import lombok.extern.slf4j.*;
import org.springframework.web.reactive.function.client.*;

import java.text.*;

@org.springframework.stereotype.Service
@Slf4j
public class Service {
    WebClient client = WebClient.create();
    public Integer getInstagramFollowers(String username){
        int count = 0;
        try {
            String resp = client.get()
                    .uri("https://i.instagram.com/api/v1/users/web_profile_info/?username="+username)
                    .header("X-IG-App-ID","936619743392459")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            count = JsonParser.parseString(resp != null ? resp : "").getAsJsonObject()
                    .getAsJsonObject("data")
                    .getAsJsonObject(   "user")
                    .getAsJsonObject("edge_followed_by")
                    .get("count").getAsInt();
        }catch (WebClientResponseException|JsonSyntaxException e){
            return -1;
        }
        return count;
    }

    public Integer getTwitchFollowers(String username){
        int count=0;
        try {
            String resp1 = client.get()
                    .uri("https://api.twitch.tv/helix/users?login="+username)
                    .header("Client-Id", "razf9inw3gsxsg5l76r3dr1vq0w08g")
                    .headers(h -> h.setBearerAuth("7gktkdyw1aw9fnra1hbz429un2d52v"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            String id = JsonParser.parseString(resp1).getAsJsonObject()
                    .getAsJsonArray("data").get(0)
                    .getAsJsonObject()
                    .get("id").getAsString();

            String resp2 = client.get()
                    .uri("https://api.twitch.tv/helix/channels/followers?broadcaster_id=" + id)
                    .header("Client-Id", "razf9inw3gsxsg5l76r3dr1vq0w08g")
                    .headers(h -> h.setBearerAuth("7gktkdyw1aw9fnra1hbz429un2d52v"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            count = JsonParser.parseString(resp2).getAsJsonObject()
                    .get("total").getAsInt();
        }catch (Exception e){
            return -1;
        }
        return count;
    }

    public Integer getTwitterFollowers(String username){
//        didn't have twitter api key
        return -1;
    }

    public Integer getYoutubeSubscribers(String username){
        int count=0;
        try {
            String resp1 = client.get()
                    .uri(MessageFormat.format("https://youtube.googleapis.com/youtube/v3/search?part=id&maxResults=1&q={0}&type=channel&key={1}",username,"AIzaSyCTi-JNWiUM6HsVihQzVZwbOUyfoUBb3tI"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            String id = JsonParser.parseString(resp1).getAsJsonObject()
                    .getAsJsonArray("items").get(0)
                    .getAsJsonObject()
                    .getAsJsonObject("id")
                    .get("channelId").getAsString();

            String resp2 = client.get()
                    .uri("https://youtube.googleapis.com/youtube/v3/channels?part=statistics&id={0}&key={1}",id,"AIzaSyCTi-JNWiUM6HsVihQzVZwbOUyfoUBb3tI")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            count = JsonParser.parseString(resp2).getAsJsonObject()
                    .getAsJsonArray("items").get(0)
                    .getAsJsonObject()
                    .getAsJsonObject("statistics")
                    .get("subscriberCount").getAsInt();
        }catch (Exception e){
            return -1;
        }
        return count;
    }
}
