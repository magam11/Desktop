package sample.dataTransferObject.request;


import com.sun.istack.internal.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class AuthenticationRequest {
    @NotNull
    private String phoneNumber;
    @NotNull
    private String password;


    @Override
    public String toString() {
        return "{" +
                "\"phoneNumber\"" +":\""+ phoneNumber + '\"' +","+
                "\"password\"" +":\""+ password + "\"" +
                '}';
    }
}
