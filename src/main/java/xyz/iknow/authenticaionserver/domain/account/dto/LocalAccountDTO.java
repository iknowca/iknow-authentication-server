package xyz.iknow.authenticaionserver.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class LocalAccountDTO extends AccountDTO {
    private String email;
    private String password;

    public LocalAccountDTO(LocalAccount account) {
        super(account);
        this.email = account.getEmail();
    }
}
