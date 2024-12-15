package api;

import com.squareup.okhttp.*;
import model.Address;
import model.Order;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class API {
    private static final String URl = "";
    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json");
    public static List<Order> orders;
    public static boolean sendSignature(Map<Long,String> oSign) throws IOException {
        oSign = new HashMap<>();
        oSign.put(1L,"123");
        oSign.put(12L,"123");
        JSONArray data = new JSONArray();
        for (Long id : oSign.keySet()) {
            String signature = oSign.get(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            jsonObject.put("signature",signature);
            data.put(jsonObject);
        }
        RequestBody body = RequestBody.create(JSON, data.toString());
        Request request = new Request.Builder()
                .url(URl)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        return false;
    }
    public static List<Order> getOrders(){
        //fake order
        Order o1 = new Order(0,0,"12/10/2001",100.70,new Address(0,"Ho Chi Minh","Thu Duc","Binh Chieu", "123"),null);
        Order o2 = new Order(1,0,"12/10/2001",100.70,new Address(0,"Ho Chi Minh","Thu Duc","Binh Chieu", "123"),null);
        orders = List.of(o1,o2);
        return orders;
    }
}
