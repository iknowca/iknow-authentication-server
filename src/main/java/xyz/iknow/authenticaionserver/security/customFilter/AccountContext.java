package xyz.iknow.authenticaionserver.security.customFilter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;

@Slf4j
@Component
public class AccountContext {
    private static ThreadLocal<Account> accountThreadLocal = new ThreadLocal<>();

    public static void setAccountThreadLocal(Account account){
        log.info("Account set in ThreadLocal");
        accountThreadLocal.set(account);
    }

    public static Account getAccount() {
        log.info("Get my account from ThreadLocal");
        return accountThreadLocal.get();
    }

    public static void clear(){
        log.info("clear ThreadLocal account");
        accountThreadLocal.remove();
    }


}
