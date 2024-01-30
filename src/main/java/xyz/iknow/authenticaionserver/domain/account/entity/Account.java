package xyz.iknow.authenticaionserver.domain.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account", uniqueConstraints = {
        @UniqueConstraint(
                name = "EMAIL_UNIQUE",
                columnNames = {"email"}
        )
})
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private String email;
    @Getter
    @JsonIgnore
    private String password;
}
