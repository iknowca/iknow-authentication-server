package xyz.iknow.authenticaionserver.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    final private AccountRepository accountRepository;

    @Override
    public Boolean validateEamil(String email) {
         Boolean result = accountRepository.existsByEmail(email);
        return result;
    }
}
