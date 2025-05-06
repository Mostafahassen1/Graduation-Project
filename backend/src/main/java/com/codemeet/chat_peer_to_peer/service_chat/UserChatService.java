
package com.codemeet.chat_peer_to_peer.service_chat;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codemeet.chat_peer_to_peer.dto_chat.UserChatDTO;
import com.codemeet.entity.User;
import com.codemeet.repository.UserRepository;

@Service
public class UserChatService {


    private final Map<String ,String > users = new HashMap<>();
    private  final UserRepository userRepository ;


    public UserChatService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

        public void saveUser(String nickname) {
           users.put(nickname, "ONLINE");
//      // System.out.println("-----------------");
//     //  System.out.println("Current Nickname ====> : " + users.get(nickname));
//   //    System.out.println("-----------------");
        }

     public void disConnect( User user) {  // todo : this must return nickName
      // user.setStatus(Static.OFFLINE);
      // users.remove(user.getUsername()); // todo : Remove user from the online map
     }

    public List<UserChatDTO> findConnectedUsers() {
        List<UserChatDTO> connectedUsers = new ArrayList<>();

        for (Map.Entry<String, String> entry : users.entrySet()) {
            String username = entry.getKey();
            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                connectedUsers.add(new UserChatDTO(user)); // Convert to DTO
          //      System.out.println("Current user: " + user.getUsername());
            }
        }

        return connectedUsers;
    }


     public Optional<User> findUserBy(String nickname) {
        return userRepository.findByUsername(nickname);
    }


}





