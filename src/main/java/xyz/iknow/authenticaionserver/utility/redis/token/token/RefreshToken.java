package xyz.iknow.authenticaionserver.utility.redis.token.token;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@RedisHash("refresh_token")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RefreshToken {

    @Id
    private Long id;
    private String jwt;
    @TimeToLive
    private Long expiration;
}