package xyz.iknow.authenticaionserver.domain.account.exception;

import lombok.Getter;

public class AccountException extends RuntimeException {
    ACCOUNT_ERROR error;

    public enum ACCOUNT_ERROR {
        INVALID_PLATFORM(400, "유효하지 않은 플랫폼입니다."),
        INVALID_ACCOUNT(400, "유효하지 않은 계정입니다."),
        SOCIAL_ACCOUNT_DID_NOT_UPDATE_PASSWORD(400, "소셜 계정은 비밀번호를 수정할 수 없습니다."),
        INVALID_UPDATE_REQUEST(400, "유효하지 않은 수정 요청입니다."),
        EMAIL_DUPLICATION(400, "이미 사용중인 이메일입니다."),
        INVALID_EMAIL(400, "유효하지 않은 이메일입니다.");
        @Getter
        private final int status;
        @Getter
        private final String message;

        ACCOUNT_ERROR(int status, String s) {
            this.status = status;
            this.message = s;
        }
    }

    public AccountException(ACCOUNT_ERROR error) {
        super(error.name());
        this.error = error;
    }


}
