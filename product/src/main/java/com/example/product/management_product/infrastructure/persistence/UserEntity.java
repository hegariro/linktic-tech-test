package com.example.product.management_product.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(length = 36)
    private String id;
    private String nickname;
    private String name;
    private String passwd;

    public UserEntity(String id, String nickname, String name, String passwd) {
        this.id = id;
        this.nickname = nickname;
        this.name = name;
        this.passwd = passwd;
    }
}
