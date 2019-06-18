package sample.connection;


import okhttp3.*;
import org.json.JSONObject;
import sample.Constant;
import sample.dataTransferObject.request.AuthenticationRequest;

import java.io.IOException;

public class ApiConnection {

    private static ApiConnection apiConnection = new ApiConnection();
    private String serverAddress;

    private ApiConnection() {
        serverAddress = Constant.SERVER_ADDRESS;
    }

    public static ApiConnection getInstance() {
        return apiConnection;
    }

    //local variabeles
    private OkHttpClient client = new OkHttpClient();


    @Connection(uri = "user/login")
    public void loginConnection(String uri, AuthenticationRequest authenticationRequest) {
        MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, authenticationRequest.toString());
        Request request = new Request.Builder()
                .url(serverAddress + uri)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject responseJson = new JSONObject(new String(response.body().bytes()));
                    String message = responseJson.getString("message");
                    boolean success = responseJson.getBoolean("success");
                    System.out.println(success +" "+ message);

//                    ResponseBody body1 = response.body();
//                    byte[] bytes = body1.bytes();
//                    System.out.println("ssss "+new String(bytes));


                }else {
                    System.out.println("no  sccess");
                }
            }
        });
    }
}
