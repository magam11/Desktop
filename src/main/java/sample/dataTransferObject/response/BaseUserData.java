package sample.dataTransferObject.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BaseUserData {
    private List<ImageData> picturesData;
    private int totoalPageCount;
    private String fruction;
    private String phoneNumber;
}
