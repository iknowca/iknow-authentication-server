package xyz.iknow.authenticaionserver.domain.account.entity;

import lombok.*;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class AccountDTO {
    private Long id;
    private String email;
    private String password;
}
