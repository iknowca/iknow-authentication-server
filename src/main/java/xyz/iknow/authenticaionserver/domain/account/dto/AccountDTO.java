package xyz.iknow.authenticaionserver.domain.account.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import xyz.iknow.authenticaionserver.domain.account.dto.oauth.OauthAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;

@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AccountDTO.class, name = ""),
        @JsonSubTypes.Type(value = OauthAccountDTO.class, name = "oauth"),
        @JsonSubTypes.Type(value = LocalAccountDTO.class, name = "local")
})
public class AccountDTO {
    private String type;
    private Long id;
    private String nickname;
    private AccountDetailsDTO accountDetails;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.nickname = account.getNickname();
    }
}
