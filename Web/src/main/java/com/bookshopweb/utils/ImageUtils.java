
package com.bookshopweb.utils;

import com.bookshopweb.api.APISaveImage;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;


public class ImageUtils {
    private static Path IMAGE_DIR;
    //thiết lập đường dẫn cho thư mục chứa hình ảnh (IMAGE_DIR).
//Đường dẫn này được xây dựng từ đường dẫn thực tế của ứng dụng web và đường dẫn hình ảnh từ project
    public static void setServletContext(ServletContext servletContext) {

        IMAGE_DIR = Paths.get(servletContext.getRealPath("/"),"img/");
    }
    //Nếu Optional trống (Optional.empty()), sẽ không lấy được giá trị của optional
//và không gặp lỗi khi cố gắng lấy giá trị null.
// Điều này giúp tránh lỗi NullPointerException khi làm việc với các giá trị có thể là null.
    public static Optional<String> upload(HttpServletRequest request) {

        Optional<String> imageName = Optional.empty();
        try {
            //  Lấy phần của yêu cầu HTTP có tên là “image”. Phần này chứa dữ liệu của file hình ảnh được tải lên.
            Part filePart = request.getPart("image");
            //Kiểm tra xem phần “image” có dữ liệu và có phải là hình ảnh không.
            if (filePart.getSize() != 0 && filePart.getContentType().startsWith("image")) {
                //Nếu thư mục chứa hình ảnh (IMAGE_DIR)
                // không tồn tại, tạo thư mục đó.
                if (!Files.exists(IMAGE_DIR)) {
                    Files.createDirectories(IMAGE_DIR);
                }
                //Tạo một file tạm thời trong thư mục IMAGE_DIR với tiền tố là “img-” và phần mở rộng là “.jpg”.
                Path targetLocation = Files.createTempFile(IMAGE_DIR, "img-", ".jpg");
                //Mở một luồng đầu vào từ phần “image” và sao chép nội dung của luồng này vào file tạm thời
                // Nếu file tạm thời đã tồn tại, nó sẽ bị ghi đè.
                try (InputStream fileContent = filePart.getInputStream()) {
                    Files.copy(fileContent, targetLocation, StandardCopyOption.REPLACE_EXISTING);
                }
                System.out.println("File copied to: " + targetLocation.toString());

                //  // Lấy tên của file tạm thời và đặt nó vào đối tượng Optional.
                imageName = Optional.of(targetLocation.getFileName().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error during image upload: " + e.getMessage());
        }
        return imageName;
    }
    public static String uploadImage(HttpServletRequest request) throws ServletException, IOException {

        String imageName = "";
        Part filePart = request.getPart("image");
        imageName = APISaveImage.uploadImageAndGetLink(filePart.getInputStream(), "");

        return imageName;
    }
    public static void delete(@NotNull String imageName) {
        Path imagePath = IMAGE_DIR.resolve(imageName).normalize();
        try {
            boolean result = Files.deleteIfExists(imagePath);
            if (result) {
                System.out.println("File is deleted: " + imageName);
            } else {
                System.out.println("Sorry, unable to delete the file: " + imageName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}