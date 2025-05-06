package com.codemeet.controller;

import io.agora.media.RtcTokenBuilder2;
import io.agora.rtm.RtmTokenBuilder2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agora")
public class AgoraTokenController {

    @Value("${agora.app-id}")
    private String appId;
    
    @Value("${agora.app-certificate}")
    private String appCertificate;
    
    @GetMapping("/rtm-token")
    public Map<String, String> getRtmToken(
        @RequestParam String uid,
        @RequestParam String channelName,
        @RequestParam Integer tokenExpire
    ) {
        return Map.ofEntries(Map.entry("rtmToken", new RtmTokenBuilder2()
            .buildToken(
                appId,
                appCertificate,
                uid,
                tokenExpire
            )));
    }
    
    @GetMapping("/rtc-token")
    public Map<String, String> getRtcToken(
        @RequestParam Integer uid,
        @RequestParam String channelName,
        @RequestParam Integer tokenExpire,
        @RequestParam Integer privilegeExpire
    ) {
        return Map.ofEntries(Map.entry("rtcToken", new RtcTokenBuilder2()
            .buildTokenWithUid(
                appId,
                appCertificate,
                channelName,
                uid,
                RtcTokenBuilder2.Role.ROLE_PUBLISHER,
                tokenExpire,
                privilegeExpire
            )));
    }
}
