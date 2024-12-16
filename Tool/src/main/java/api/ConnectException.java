package api;

import java.io.IOException;

public class ConnectException extends IOException {
    public ConnectException(){

    }

    @Override
    public String getMessage() {
        return "Không thể kết nối đến server";
    }
}
