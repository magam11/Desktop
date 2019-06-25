package sample.dataTransferObject.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ImageManagerRequest {

   private String picName;
   private String  actionType;

    @Override
    public String toString() {
        return "{" +
                "\"picName\"" +":\""+ picName + '\"' +","+
                "\"actionType\"" +":\""+ actionType + "\"" +
                '}';
    }
}
