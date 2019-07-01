package sample.dataTransferObject.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ImageData {
    private Collection<String> picNames;
    private int page;
    private boolean imageStatus;

    @Override
    public String toString() {
        String picNames = "";
        for (String picName : this.picNames) {
            picNames +="\""+ picName +"\""+ ",";
        }
        picNames = picNames.substring(0, picNames.length() - 1);
        return "{" +
                "\"page\"" + ":\"" + page + '\"' + "," +
                "\"imageStatus\"" + ":" + imageStatus  + "," +
                "\"picNames\"" + ":[" + picNames + "]" +
                '}';
    }
}
