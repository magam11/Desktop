package sample.controller.dialogController;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Constant;

import java.awt.*;
import java.net.URI;

public class ShareDialogController {
    @FXML
    public ImageView pinterestImageView;
    @FXML
    public ImageView twitterImageView;
    @FXML
    public ImageView linkedinImageView;
    @FXML
    public ImageView faceBookImageView;
    public TextField sharedImageLink;

    public void initialize(){
        pinterestImageView.setImage(new Image(this.getClass().getResourceAsStream("/image/pinterest.png")));
        twitterImageView.setImage(new Image(this.getClass().getResourceAsStream("/image/twitter.png")));
        linkedinImageView.setImage(new Image(this.getClass().getResourceAsStream("/image/linkedin.png")));
        faceBookImageView.setImage(new Image(this.getClass().getResourceAsStream("/image/faceBook.png")));
    }

    @FXML
    public void shareOnPinterest(MouseEvent mouseEvent) {
        System.out.println("pinterest share");
    }

    @FXML
    public void shareOnTwitter(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://www.twitter.com/intent/tweet?text=" + sharedImageLink.getText()));
            Desktop.getDesktop().browse(new URI("https://twitter.com/intent/tweet?original_referer=" + sharedImageLink.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((Stage)linkedinImageView.getScene().getWindow()).close();
    }

    @FXML
    public void shareOnLinkedin(MouseEvent mouseEvent) {
        try {
//            Desktop.getDesktop().browse(new URI("https://www.linkedin.com/shareArticle?mini=true&amp;url=" + sharedImageLink.getText()));
            Desktop.getDesktop().browse(new URI("https://www.linkedin.com/sharing/share-offsite/?url=" + sharedImageLink.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((Stage)linkedinImageView.getScene().getWindow()).close();
    }

    @FXML
    public void shareOnFaceBook(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://www.facebook.com/sharer/sharer.php?u=" + sharedImageLink.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((Stage)linkedinImageView.getScene().getWindow()).close();

    }

    @FXML
    public void closeShareDialog(MouseEvent mouseEvent) {
        ((Stage)linkedinImageView.getScene().getWindow()).close();
    }

    @FXML
    public void copyLink(MouseEvent mouseEvent) {
        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(sharedImageLink.getText());
        systemClipboard.setContent(clipboardContent);
    }
}
