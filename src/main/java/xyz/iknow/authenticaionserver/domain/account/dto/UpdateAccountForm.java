package xyz.iknow.authenticaionserver.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UpdateAccountForm {
    private String email;
    private String password;
    private String nickname;
}
