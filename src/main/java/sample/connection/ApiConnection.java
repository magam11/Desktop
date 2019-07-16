package sample.connection;


import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.Constant;
import sample.dataTransferObject.request.AuthenticationRequest;
import sample.dataTransferObject.request.ImageData;
import sample.dataTransferObject.request.ImageManagerRequest;
import sample.dataTransferObject.response.AuthenticationResponse;
import sample.dataTransferObject.response.BaseUserData;
import sample.service.LoginService;
import sample.service.serviceImpl.*;
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
    private Storage storage = Storage.getInstance();


    @Connection(uri = "user/login", method = "POST")
    public void loginConnection(String uri, AuthenticationRequest authenticationRequest, boolean remember) {
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
                        if (remember) {
                            Storage.getInstance().setToken(userInfo.getString("token"));
                            Storage.getInstance().setCurrentToken(userInfo.getString("token"));
                        } else {
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

    @Connection(uri = "user/data/page/{pageNumber}", method = "GET", pathVariable = "pageNumber")
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

    @Connection(uri = "user/data/{pageNumber}", method = "GET", pathVariable = "pageNumber")
    public void getPage(String uri, int pageNumber, String token) {
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
                MainStageServiceImpl.getInstance().loadPage(response, pageNumber);
            }
        });
    }


    @Connection(uri = "/image/", method = "PUT", requestBody = ImageManagerRequest.class)
    public void updateImageStatus(String uri, ImageManagerRequest jsonBody) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonBody.toString());
        Request request = new Request.Builder()
                .url(Constant.SERVER_ADDRESS + uri)
                .put(body) //PUT
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                /**
                 * TODO  something
                 * */
                System.out.println("/image/ ---- failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                MainStageServiceImpl.getInstance().removeImageFromCellByIndex(Integer.parseInt(fraction.getText().split("/")[0]) - 1- MainStageServiceImpl.getInstance().currentPageIndex.get()*50);

                Platform.runLater(() -> {
                    DeleteDialogServiceImpl.getInstance().closeDeleteDialog();

                });

            }
        });
    }


    @Connection(uri = "/image/ next|previous ", method = "GET", requestParam = "picName")
    public void getNextPreviousImage(String uri, String imageName, String next_previous) {
        Request request = new Request.Builder()
                .url(Constant.SERVER_ADDRESS + uri + "?picName=" + imageName)
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                /**
                 * TODO  something
                 * */
                System.out.println("/image/next---- failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Platform.runLater(() -> {
                    SliderServiceImpl.getInstance().showpPreviousNextImageData(response, next_previous);
                });

            }
        });
    }

    @Connection(uri = "/image/many", method = "PUT", requestBody = ImageData.class)
    public void updateImagesStatusBatch(ImageData imageData) {
        System.out.println("imageData " + imageData);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, imageData.toString());
        Request request = new Request.Builder()
                .url(Constant.SERVER_ADDRESS + Constant.IPAGE_UPDATE_MANY)
                .put(body)
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO something
                System.out.println("/image/many -failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Platform.runLater(() -> {
                    if (response.code() == 200) {
                        JSONObject responseJson = null;
                        try {
                            responseJson = new JSONObject(new String(response.body().bytes()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        List<sample.dataTransferObject.response.ImageData> data = new ArrayList<>();
                        JSONArray picturesData = responseJson.getJSONArray("picturesData");
                        JSONObject imageDataJson = null;
                        if (picturesData != null) {
                            for (int i = 0; i < picturesData.length(); i++) {
                                imageDataJson = picturesData.getJSONObject(i);
                                data.add(sample.dataTransferObject.response.ImageData.builder()
                                        .createdAt(imageDataJson.getString("createdAt"))
                                        .picName(imageDataJson.getString("picName"))
                                        .picSize(imageDataJson.getDouble("picSize"))
                                        .build());
                            }
                        }
                        BaseUserData baseUserData = BaseUserData.builder()
                                .fruction(responseJson.getString("fruction"))
                                .totoalPageCount(responseJson.getInt("totoalPageCount"))
                                .picturesData(data)
                                .phoneNumber(responseJson.getString("phoneNumber"))
                                .build();

                        MainStageServiceImpl.getInstance().loadMainStageData(baseUserData, imageData.getPage(), "general");
                    } else {
                        //TODO something
                    }
                });

            }
        });
    }


    @Connection(uri = "/image/recoverMany", requestBody = ImageData.class, method = "PUT")
    public void recoverImagesFromRecycle(ImageData imageData) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, imageData.toString());
        Request request = new Request.Builder()
                .url(Constant.SERVER_ADDRESS + Constant.RECOVER_MANY)
                .put(body)
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    updateRecyclePageData(response, imageData);
                } else {
                    //TODO something
                }
            }
        });
    }





    @Connection(uri = "/image/page/{page}", requestParam = {"fromDate", "toDate"}, method = "GET", pathVariable = "page")
    public void getDataByInterval(String fromDate, String toDate, int page) {

        fromDate = fromDate == null ? "" : fromDate;
        toDate = toDate == null ? "" : toDate;
        Request request = new Request.Builder()
                .url(Constant.SERVER_ADDRESS + String.format("image/page/%s?fromDate=%s&toDate=%s", page, fromDate, toDate))
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("image/page/{page}?fromDate=%s&toDate=%s -failure");
                //TODO something

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    JSONObject responseJson = null;
                    List<sample.dataTransferObject.response.ImageData> imageData = new ArrayList<>();
                    try {
                        responseJson = new JSONObject(new String(response.body().bytes()));
                        JSONArray picturesData = responseJson.getJSONArray("picturesData");
                        JSONObject imageDataJson = null;
                        if (picturesData != null) {
                            for (int i = 0; i < picturesData.length(); i++) {
                                imageDataJson = picturesData.getJSONObject(i);
                                imageData.add(sample.dataTransferObject.response.ImageData.builder()
                                        .createdAt(imageDataJson.getString("createdAt"))
                                        .picName(imageDataJson.getString("picName"))
                                        .picSize(imageDataJson.getDouble("picSize"))
                                        .build());
                            }
                        }
                        JSONObject finalResponseJson = responseJson;
                        Platform.runLater(() -> {
                            MainStageServiceImpl.getInstance().loadMainStageData(BaseUserData.builder()
                                    .fruction(finalResponseJson.getString("fruction"))
                                    .totoalPageCount(finalResponseJson.getInt("totoalPageCount"))
                                    .picturesData(imageData)
//                                .phoneNumber(responseJson.getString("phoneNumber"))
                                    .build(), page, "filter_search");
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
//                    TODO something
                    System.out.println("image/page/{page}?fromDate=%s&toDate=%s else blok");
                    System.out.println(response.code());
                }

            }
        });

    }


    @Connection(uri="/image/filter/page/{page}",requestParam = {"year","month"},method = "GET")
    public void getFilterData(int page, String requestYear, String requestMonth) {
        Request request = new Request.Builder()
                .url(Constant.SERVER_ADDRESS + String.format("image/filter/page/%s?year=%s&month=%s", page, requestYear, requestMonth))
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }






    @Connection(uri = "image/deleted/page/{page}", method = "GET", pathVariable = "page")
    public void getDeletedImagePage(int page) {
        Request request = new Request.Builder()
                .url(Constant.SERVER_ADDRESS + "image/deleted/page/" + page)
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
                .build();
        OkHttpClient client1 = new OkHttpClient();
        client1.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //TODO something
                System.out.println("/image/deleted/page/{page} -failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    try {
                        JSONObject responseJson = new JSONObject(new String(response.body().bytes()));
                        List<sample.dataTransferObject.response.ImageData> imageData = new ArrayList<>();
                        JSONArray picturesData = responseJson.getJSONArray("picturesData");
                        JSONObject imageDataJson = null;
                        if (picturesData != null) {
                            for (int i = 0; i < picturesData.length(); i++) {
                                imageDataJson = picturesData.getJSONObject(i);
                                imageData.add(sample.dataTransferObject.response.ImageData.builder()
                                        .deletedAt(imageDataJson.getString("deletedAt"))
                                        .picName(imageDataJson.getString("picName"))
                                        .picSize(imageDataJson.getDouble("picSize"))
                                        .build());
                            }
                        }
                        Platform.runLater(() -> {
                            RecycleBinServiceImpl.getInstance().loadDataInRecycleBin(BaseUserData.builder()
                                    .totalElementCount(responseJson.getLong("totalElementCount"))
                                    .totoalPageCount(responseJson.getInt("totoalPageCount"))
                                    .picturesData(imageData)
                                    .build(), page);
                        });


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
//                  TODO something
                    System.out.println("image/deleted/page/{page} else block");
                }

            }
        });
    }

    @Connection(uri = "/image/", method = "PUT", requestBody = ImageManagerRequest.class)
    public void recoverImageStatus(ImageManagerRequest jsonBody) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonBody.toString());
        Request request = new Request.Builder()
                .url(Constant.SERVER_ADDRESS + Constant.IMAGE_URI)
                .put(body) //PUT
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("recoverImageStatus falilure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("recoverImageStatus onResponse");

            }
        });
    }

    @Connection(uri = "/image/{picture}", method = "DELETE", pathVariable = "picture")
    public void deleteImage(String picName) {
        Request request = new Request.Builder()
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
                .url(Constant.SERVER_ADDRESS + Constant.IMAGE_URI + "picture/" + picName)
                .delete()
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("deleteImage -failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("deleteImage -onresponse " + response.code());

            }
        });
    }


    @Connection(uri = "user/data/page/{pageNumber}", method = "GET", pathVariable = "pageNumber")
    public void loadBaseData(String pageNumber) {
        Request request = new Request.Builder()
                .url(Constant.SERVER_ADDRESS + Constant.BASE_DATA_URI + pageNumber)
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
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
                if (response.code() == 200) {
                    JSONObject responseJson = null;
                    List<sample.dataTransferObject.response.ImageData> imageData = new ArrayList<>();
                    try {
                        responseJson = new JSONObject(new String(response.body().bytes()));
                        JSONArray picturesData = responseJson.getJSONArray("picturesData");
                        JSONObject imageDataJson = null;
                        if (picturesData != null) {
                            for (int i = 0; i < picturesData.length(); i++) {
                                imageDataJson = picturesData.getJSONObject(i);
                                imageData.add(sample.dataTransferObject.response.ImageData.builder()
                                        .picSize(imageDataJson.getDouble("picSize"))
                                        .createdAt(imageDataJson.getString("createdAt"))
                                        .picName(imageDataJson.getString("picName"))
                                        .build());
                            }
                        }
                        JSONObject finalResponseJson = responseJson;
                        Platform.runLater(() -> {
                            MainStageServiceImpl.getInstance().loadMainStageData(BaseUserData.builder()
                                    .totoalPageCount(finalResponseJson.getInt("totoalPageCount"))
                                    .fruction(finalResponseJson.getString("fruction"))
                                    .picturesData(imageData)
//                                .phoneNumber(responseJson.getString("phoneNumber"))
                                    .build(), Integer.parseInt(pageNumber), "filter_search");
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
//                    TODO something
                    System.out.println("image/page/{page}?fromDate=%s&toDate=%s else blok");
                    System.out.println(response.code());
                }
            }
        });
    }


    @Connection(uri = "/image/many", method = "DELETE", requestBody = ImageData.class)
    public void deleteSelectedImageFromRecycleBin(ImageData imageData) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, imageData.toString());
        Request request = new Request.Builder()
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
                .url(Constant.SERVER_ADDRESS + Constant.IPAGE_UPDATE_MANY)
                .delete(body)
                .addHeader(Constant.AUTHORIZATION, storage.getCurrentToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("failure /image/many -deletovy");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    updateRecyclePageData(response, imageData);
                }else {
                    System.out.println("else blok ~~ /image/many ");
                }
            }
        });

    }

    private void updateRecyclePageData(Response response, ImageData imageData) {
        JSONObject responseJson = null;
        try {
            responseJson = new JSONObject(new String(response.body().bytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<sample.dataTransferObject.response.ImageData> data = new ArrayList<>();
        JSONObject imageDataJson = null;
        JSONArray picturesData = responseJson.getJSONArray("picturesData");
        if (picturesData != null) {
            for (int i = 0; i < picturesData.length(); i++) {
                imageDataJson = picturesData.getJSONObject(i);
                data.add(sample.dataTransferObject.response.ImageData.builder()
                        .deletedAt(imageDataJson.getString("deletedAt"))
                        .createdAt(imageDataJson.getString("createdAt"))
                        .picSize(imageDataJson.getDouble("picSize"))
                        .picName(imageDataJson.getString("picName"))
                        .build());
            }
        }
        BaseUserData baseUserData = BaseUserData.builder()
                .totalElementCount(responseJson.getInt("totalElementCount"))
                .totoalPageCount(responseJson.getInt("totoalPageCount"))
                .picturesData(data)
                .build();
        Platform.runLater(() -> {
            RecycleBinServiceImpl.getInstance().loadDataInRecycleBin(baseUserData, imageData.getPage());
        });
    }


}