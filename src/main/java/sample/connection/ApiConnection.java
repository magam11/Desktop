package sample.connection;


import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.Constant;
import sample.dataTransferObject.request.AuthenticationRequest;
import sample.dataTransferObject.response.AuthenticationResponse;
import sample.dataTransferObject.response.BaseUserData;
import sample.dataTransferObject.response.ImageData;
import sample.service.LoginService;
import sample.service.serviceImpl.LoginServiceImpl;
import sample.storage.Storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiConnection {

    private static ApiConnection apiConnection = new ApiConnection();
    private String serverAddress;

    private ApiConnection() {
        serverAddress = Constant.SERVER_ADDRESS;
    }

    public static ApiConnection getInstance() {
        return apiConnection;
    }

    //services
    private LoginService loginService = LoginServiceImpl.getInstance();


    //local variabeles
    private OkHttpClient client = new OkHttpClient();


    @Connection(uri = "user/login")
    public void loginConnection(String uri, AuthenticationRequest authenticationRequest,boolean remember) {
        MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, authenticationRequest.toString());
        Request request = new Request.Builder()
                .url(serverAddress + uri)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                /**
                 * TODO  something
                 * */
                System.out.println("failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject responseJson = new JSONObject(new String(response.body().bytes()));
                    if (responseJson.getBoolean("success")) {//login is seccessful
                        JSONObject userInfo = responseJson.getJSONObject("userInfo");
                        if(remember){
                            Storage.getInstance().setToken(userInfo.getString("token"));
                            Storage.getInstance().setCurrentToken(userInfo.getString("token"));
                        }else {
                            Storage.getInstance().setCurrentToken(userInfo.getString("token"));
                        }
                        loginService.authenticationSuccessful(AuthenticationResponse.builder()
                                .name(userInfo.getString("name"))
                                .token(userInfo.getString("token"))
                                .build());
                    } else {                            // invalid phone number or password (show message from back-end)
                        loginService.authenticationFailure(responseJson.getString("message"));
                        return;
                    }
                } else {
                    /**
                     * TODO  something
                     * */
                    System.out.println("no  success");
                }
            }
        });
    }

    @Connection(uri = "user/data/{pageNumber}", pathVariable = "pageNumber")
    public void baseData(String uri, int pageNumber, String token) {
        Request request = new Request.Builder()
                .url(Constant.SERVER_ADDRESS + uri + pageNumber)
                .addHeader(Constant.AUTHORIZATION, token)
                .build();
        OkHttpClient client1 = new OkHttpClient();
        client1.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                /**
                 * TODO  something
                 * */
                System.out.println("user/data/{pageNumber} ---- failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                loginService.onRespoinseOfDataApiAnalysis(response);
            }
        });
    }


    @Connection(uri = "/image/imageName/{imageName}",method = "PUT",pathVariable = "imageName")
    public void updateImageStatus(String uri,String imageName, boolean imageStatus){


    }
}