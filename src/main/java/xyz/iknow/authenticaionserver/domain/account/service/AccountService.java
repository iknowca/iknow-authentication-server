package xyz.iknow.authenticaionserver.domain.account.service;

import org.springframework.http.ResponseEntity;
import xyz.iknow.authenticaionserver.domain.account.entity.AccountDTO;

import java.util.Map;

public interface AccountService {
    Boolean validateEamil(String email);

    ResponseEntity<Map> createAccount(AccountDTO request);

    ResponseEntity<Map> refresh(String refreshToken);

    ResponseEntity<AccountDTO> getMyInfo();
}
