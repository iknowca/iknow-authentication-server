package xyz.iknow.authenticaionserver.domain.account.service;

import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;

public interface AccountService {
    Boolean validateEamil(String email);

    void createAccount(LocalAccountDTO request);

    AccountDTO getMyInfo(Account account);

    AccountDTO updateMyInfo(Account account, AccountDTO request);

    void logout(Account account);

    void withdrawAccount(Account account);

    AccountDTO changePassword(Account account, LocalAccountDTO request);
}
