package xyz.iknow.authenticaionserver.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatform;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;
import xyz.iknow.authenticaionserver.domain.account.repository.oauth.OauthPlatformRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBInitializer {
    private final OauthPlatformRepository oauthPlatformRepository;

    @PostConstruct
    public void init() {
        log.info("DBInitializer init");
        initOauthFlatformTypes();
        log.info("DBInitializer init end");
    }

    private void initOauthFlatformTypes() {
        log.info("initOauthFlatformTypes");
        try {
            Set<OauthPlatformType> platformTypes = oauthPlatformRepository.findAll().stream()
                    .map(OauthPlatform::getPlatformType)
                    .collect(Collectors.toSet());
            for(OauthPlatformType platformType: OauthPlatformType.values()) {
                if(!platformTypes.contains(platformType)) {
                    OauthPlatform oauthPlatform = new OauthPlatform(platformType);
                    oauthPlatformRepository.save(oauthPlatform);
                }
            }
        } catch (Exception e) {
            log.error("initOauthFlatform error", e);
        }
    }
}
