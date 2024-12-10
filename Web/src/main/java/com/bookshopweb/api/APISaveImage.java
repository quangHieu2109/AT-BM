package com.bookshopweb.api;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class APISaveImage {
	private final String EMAIL = "lavekax254@gearstag.com";
	private final String PASSWORD = "hoctapdh0001";
	

	public static String uploadImageAndGetLink(InputStream imageFile, String nameFile) {
	    try {
	        // Tạo URL đến API Imgur
	        URL url = new URL("https://api.imgur.com/3/upload");
	        
	        // Mở kết nối HTTP
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	        // Cấu hình request
	        connection.setDoOutput(true); // Cho phép gửi dữ liệu lên server
	        connection.setDoInput(true); // Cho phép nhận dữ liệu từ server
	        connection.setRequestMethod("POST"); // Phương thức POST
	        connection.setRequestProperty("Authorization", "Client-ID " + "dd8ce706a76465e"); // Header xác thực
	        String boundary = "---------------------------" + System.currentTimeMillis();
	        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary); // Loại nội dung là multipart/form-data

	        // Lấy output stream từ kết nối
	        OutputStream outputStream = connection.getOutputStream();
	        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));

	        // Gửi file ảnh
	        writer.append("--").append(boundary).append("\r\n");
	        writer.append("Content-Disposition: form-data; name=\"image\"; filename=\"").append(nameFile)
	                .append("\"\r\n");
	        writer.append("Content-Type: ").append("image/jpeg").append("\r\n");
	        writer.append("\r\n");
	        writer.flush();

	        // Đọc dữ liệu từ InputStream của file ảnh và ghi vào output stream
	        InputStream inputStream = imageFile;
	        byte[] buffer = new byte[409600];
	        int bytesRead;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }
	        outputStream.flush();
	        inputStream.close();

	        // Kết thúc phần gửi file
	        writer.append("\r\n");
	        writer.append("--").append(boundary).append("--\r\n");
	        writer.close();
	        outputStream.close();

	        // Lấy mã phản hồi từ server
	        int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            // Đọc phản hồi và trích xuất liên kết từ JSON
	            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            StringBuilder response = new StringBuilder();
	            String line;
	            while ((line = reader.readLine()) != null) {
	                response.append(line);
	            }
	            reader.close();
	            JSONObject json = new JSONObject(response.toString()).getJSONObject("data");

	            return json.getString("link");
	        }
	    } catch (Exception e) {
	        // Xử lý ngoại lệ
	    }
	    return null;
	}

}
