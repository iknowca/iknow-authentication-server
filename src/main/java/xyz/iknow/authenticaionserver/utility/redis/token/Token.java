package xyz.iknow.authenticaionserver.utility.redis.token;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@RedisHash("Token")
@NoArgsConstructor
@AllArgsConstructor
@lombok.Data
@Builder
public class Token implements Serializable {
    @Id
    private Long id;
    private String prefix;
    private String jwt;
    @TimeToLive
    private Long expiration;
}
