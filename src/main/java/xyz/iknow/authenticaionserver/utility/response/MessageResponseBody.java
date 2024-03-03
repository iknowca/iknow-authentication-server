package xyz.iknow.authenticaionserver.utility.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MessageResponseBody {
    private String message;
    private String status="success";
}
