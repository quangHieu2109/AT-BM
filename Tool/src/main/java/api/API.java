package api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Order;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class API {
    private static Gson gson =  new Gson();
    private static final String URl = "http://localhost:8080";
    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json");
    public static List<Order> orders;
    public static Response sendSignature(Map<Long,String> oSign) throws IOException {
        JSONArray data = new JSONArray();
        for (Long id : oSign.keySet()) {
            String signature = oSign.get(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            jsonObject.put("signature",signature);
            data.put(jsonObject);
        }
        RequestBody body = new FormBody.Builder()
                .add("orderSignatures", data.toString())
                .build();

        Request request = new Request.Builder()
                .url(URl+"/orderSwing")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response login(String username,String password) throws IOException{
        return null;
    }
    public static List<Order> getOrders(String username) throws ConnectException {
        if (username==null) {
            return null;
        }
        Request request = new Request.Builder()
                .url(URl+"/orderSwing?username="+username)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String json = response.body().string();
            Type listOrderType = new TypeToken<List<Order>>(){}.getType();
            orders = gson.fromJson(json,listOrderType);
            return orders;
        } catch (IOException e) {
            throw new ConnectException();
        }
    }

    public static void main(String[] args) throws Exception {
        sendSignature(null);
    }
}
