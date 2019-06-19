package sample.dataTransferObject.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ImageData {
    private String picName;
    private double picSize;
    private String createdAt;
    private Date deletedAt;


}

