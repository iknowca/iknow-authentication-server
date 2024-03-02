package xyz.iknow.authenticaionserver.domain.account.entity.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;

@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
public class AccountDTO {
    private Long id;
    private String nickname;
    private AccountDetailsDTO accountDtails;
    public AccountDTO(Account account) {
        this.id = account.getId();
        this.nickname = account.getNickname();
    }
}
