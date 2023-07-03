package kr.org.booklog.account.entity;

import kr.org.booklog.config.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(columnDefinition = "varchar(30)", nullable = false)
    private String name;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(30)", nullable = false)
    private String nickname;

    @Column(columnDefinition = "varchar(30)", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30)", nullable = false)
    private OAuthType join_type;
}
