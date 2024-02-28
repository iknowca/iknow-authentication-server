package xyz.iknow.authenticaionserver.domain.account.service;

import org.springframework.http.ResponseEntity;
import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.UpdateAccountForm;

import java.util.Map;

public interface AccountService {
    Boolean validateEamil(String email);

    ResponseEntity<Map> createAccount(AccountDTO request);

    ResponseEntity<AccountDTO> getMyInfo();

    ResponseEntity<Map> updateMyInfo(UpdateAccountForm request);

    ResponseEntity<Map> logout();

    ResponseEntity<Map> withdrawAccount();
}
