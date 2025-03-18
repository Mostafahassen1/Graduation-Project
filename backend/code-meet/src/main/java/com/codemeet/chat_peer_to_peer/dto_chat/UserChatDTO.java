
package com.codemeet.chat_peer_to_peer.dto_chat;

import com.codemeet.entity.User;

public class UserChatDTO {

    private String nickName;
    private String fullName;

    public UserChatDTO() {
    }

    public UserChatDTO(User user) {
        this.nickName = user.getUsername();
        this.fullName = user.getFirstName() + " " + user.getLastName();
    }

    public String getNickName() {
        return nickName;
    }

    public String getFullName() {
        return fullName;
    }
}







