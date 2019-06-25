package sample.dataTransferObject.request;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class AuthenticationRequest {
    private String phoneNumber;
    private String password;


    @Override
    public String toString() {
        return "{" +
                "\"phoneNumber\"" +":\""+ phoneNumber + '\"' +","+
                "\"password\"" +":\""+ password + "\"" +
                '}';
    }
}
