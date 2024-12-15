package api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.*;
import model.Order;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class API {
    private static Gson gson =  new Gson();
    private static final String URl = "http://localhost:8080";
    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json");
    public static List<Order> orders;
    public static boolean sendSignature(Map<Long,String> oSign) throws IOException {
        oSign = new HashMap<>();
        oSign.put(1L,"123");
        oSign.put(0L,"123");

        JSONObject obj = new JSONObject();
        JSONArray data = new JSONArray();
        for (Long id : oSign.keySet()) {
            String signature = oSign.get(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            jsonObject.put("signature",signature);
            data.put(jsonObject);
        }
        obj.put("orderSignatures",data);
        System.out.println(obj);
        RequestBody body = RequestBody.create(JSON, obj.toString());
        Request request = new Request.Builder()
                .url(URl+"/orderSwing")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        System.out.println(json);
        return false;
    }
    public static List<Order> getOrders() throws Exception{
        Request request = new Request.Builder()
                .url(URl+"/orderSwing?username=user1")
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        Type listOrderType = new TypeToken<List<Order>>(){}.getType();
        orders = gson.fromJson(json,listOrderType);
        return orders;
    }

    public static void main(String[] args) throws Exception {
        sendSignature(null);
    }
}
