package xyz.iknow.authenticaionserver.domain.account.service;

import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;

public interface AccountService {
    Boolean validateEamil(String email);

    void createAccount(LocalAccountDTO request);

    AccountDTO getMyInfo(Long accountId);

    AccountDTO updateMyInfo(Long accountId, AccountDTO request);

    void logout(Long accountId);

    void withdrawAccount(Long accountId);

    AccountDTO changePassword(Long accountId, LocalAccountDTO request);

    String login(LocalAccountDTO request);
}
