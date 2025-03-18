
package com.codemeet.chat_peer_to_peer.controller_chat;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codemeet.chat_peer_to_peer.dto_chat.UserChatDTO;
import com.codemeet.chat_peer_to_peer.service_chat.UserChatService;
import com.codemeet.entity.User;
import com.codemeet.service.UserService;

@RestController
public class UserChatController {


     private final UserChatService userChatService;
      private final UserService userService;

    public UserChatController(UserChatService userChatService, UserService userService) {
        this.userChatService = userChatService;
        this.userService = userService;
    }

       @MessageMapping("/user.addUser")
       @SendTo("/topic/public")
       public UserChatDTO saveUser(@Payload String nickname ){
        //  System.out.println("user saved before user service");
       //  System.out.println("Nickname received ==> : " + nickname);
          userChatService.saveUser(nickname); // storage in map
       //   System.out.println("user saved after user service");

        return new UserChatDTO( userService.getUserEntityByUsername(nickname) );  // todo ==> this will return dto

       }


       @MessageMapping("/user.disconnectUser")  // when go out chat room ==> rest
       @SendTo("/topic/public")
       public User disconnect( @Payload User user) {
         // userService.disConnect(user);                    // todo remove from map
        //   System.out.println("user disconnected");
        return user; // todo ==> this will return dto
       }

       @GetMapping("/users")
       public List<UserChatDTO> getConnectUsers(){

        //   System.out.println("return list of active user");

        return userChatService.findConnectedUsers();

       }

}




