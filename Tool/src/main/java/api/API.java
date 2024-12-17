package api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Order;
import model.OrderItem;
import model.PublicKeyItem;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class API {
    private static final String URl = "http://localhost:8080";
    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json");
    public static List<Order> orders;
    public static List<PublicKeyItem> publicKeyItems;
    private static Gson gson = new Gson();

    public static List<OrderItem> orderItems;
    public static Response sendSignature(Map<Long,String> oSign) throws IOException {
        JSONArray data = new JSONArray();
        for (Long id : oSign.keySet()) {
            String signature = oSign.get(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("signature", signature);
            data.put(jsonObject);
        }
        RequestBody body = new FormBody.Builder()
                .add("orderSignatures", data.toString())
                .build();

        Request request = new Request.Builder()
                .url(URl + "/orderSwing")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response login(String username, String password) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(URl + "/user")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response verifyOTP(String username, String password, String otp) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("type", "verifyOTP")
                .add("otp", otp)
                .build();
        Request request = new Request.Builder()
                .url(URl + "/authenticatorSwing")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response sendOTP(String username, String password) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("type", "sendOTP")
                .build();
        Request request = new Request.Builder()
                .url(URl + "/authenticatorSwing")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response reportKey(String username,String password) throws IOException{
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("type", "reportKey")
                .build();
        Request request = new Request.Builder()
                .url(URl + "/authenticatorSwing")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response savePublicKey(String username, String password, String publicKey) throws IOException {
        System.out.println(publicKey);
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("type", "savePublicKey")
                .add("publicKey", publicKey)
                .build();
        Request request = new Request.Builder()
                .url(URl + "/authenticatorSwing")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static List<Order> getOrders(String username) throws ConnectException {
        if (username == null) {
            return null;
        }
        Request request = new Request.Builder()
                .url(URl + "/orderSwing?username=" + username)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String json = response.body().string();
            Type listOrderType = new TypeToken<List<Order>>() {
            }.getType();
            orders = gson.fromJson(json, listOrderType);
            return orders;
        } catch (IOException e) {
            throw new ConnectException();
        }
    }

    public static List<PublicKeyItem> getPublicKeys(String username) throws ConnectException {
        if (username == null) {
            return null;
        }
        Request request = new Request.Builder()
                .url(URl + "/authenticatorSwing?username=" + username)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String json = response.body().string();
            Type listPublicKeyType = new TypeToken<List<PublicKeyItem>>() {
            }.getType();
            publicKeyItems = gson.fromJson(json, listPublicKeyType);
            return publicKeyItems;
        } catch (IOException e) {
            throw new ConnectException();
        }
    }
    public static List<OrderItem> getListOrderItem(long orderId) throws ConnectException{
        Request request = new Request.Builder()
                .url(URl+"/orderSwing?type=getOrderDetail&orderId="+orderId)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String json = response.body().string();
            Order order = gson.fromJson(json, Order.class);
            orderItems = order.getOrderItems();
            return orderItems;
        } catch (IOException e) {
            throw new ConnectException();
        }
    }
    public static void printOrderItems(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            System.out.println("ID: " + item.getId());
            System.out.println("Product ID: " + item.getProductId());
            System.out.println("Quantity: " + item.getQuantity());
            System.out.println("Price: " + item.getPrice());
            System.out.println("Discount: " + item.getDiscount());
            System.out.println("Name product:" +item.getProductName());
            System.out.println("----------------------------------------");
        }
    }
    public static void main(String[] args) throws Exception {
        printOrderItems( getListOrderItem(Long.parseLong("8L")));

    }
}
