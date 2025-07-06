package com.codemeet.controller;

import io.agora.media.RtcTokenBuilder2;
import io.agora.rtm.RtmTokenBuilder2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agora")
public class AgoraTokenController {

    @Value("${agora.app-id}")
    private String appId;
    
    @Value("${agora.app-certificate}")
    private String appCertificate;
    
    @GetMapping("/rtm-token")
    public String getRtmToken(
        @RequestParam String uid,
        @RequestParam String channelName,
        @RequestParam Integer tokenExpire
    ) {
        System.out.println("Generating rtc token for %s and channel name %s".formatted(uid, channelName));
        return new RtmTokenBuilder2()
            .buildToken(
                appId,
                appCertificate,
                uid,
                tokenExpire
            );
    }
    
    @GetMapping("/rtc-token")
    public String getRtcToken(
        @RequestParam String uid,
        @RequestParam String channelName,
        @RequestParam Integer tokenExpire,
        @RequestParam Integer privilegeExpire
    ) {
        System.out.println("Generating rtc token for %s and channel name %s".formatted(uid, channelName));
        return new RtcTokenBuilder2()
            .buildTokenWithUserAccount(
                appId,
                appCertificate,
                channelName,
                uid,
                RtcTokenBuilder2.Role.ROLE_PUBLISHER,
                tokenExpire,
                privilegeExpire
            );
    }
}
