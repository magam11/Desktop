package sample.service;

public interface DeleteDialogService {

    void openConfirmationDialog(String picName, String stageName, int indexOfImage);

    void updateImageStatus(String imageName,String stageName,boolean imageStatus);

    void closeDeleteDialog();

    void openConfirmationDialogForBatch();

}
