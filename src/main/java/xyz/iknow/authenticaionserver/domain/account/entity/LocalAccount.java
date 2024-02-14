package xyz.iknow.authenticaionserver.domain.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("ouath")
public class LocalAccount extends Account {
    @JsonIgnore
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String email;
}
