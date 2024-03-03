package xyz.iknow.authenticaionserver.utility.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DTOResponseBody<T> {
    private T data;
    private String message;
}
