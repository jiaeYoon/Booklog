package kr.org.booklog.config.auth.dto;

import kr.org.booklog.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private final Long id;
    private final String name;
    private String nickname;
    private final String email;

    public SessionUser(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
