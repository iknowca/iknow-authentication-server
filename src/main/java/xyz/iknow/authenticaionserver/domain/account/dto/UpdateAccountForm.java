package xyz.iknow.authenticaionserver.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateAccountForm {
    private String email;
    private String password;
    private String nickname;
}
