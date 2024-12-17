package utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESUtils {
    private static SecretKey key;
    private static int ivSize = 16;
    private static byte[] iv = {-78, -89, 6, -74, -77, 108, 6, 1, -5, -55, 84, 70, 18, 95, -30, -41};

    public static SecretKey genKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        key = keyGen.generateKey();
        return key;
    }
    public static SecretKey getKey(){
        return key;
    }
    public static byte[] generateIV() {
        iv = new byte[ivSize];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }

    public static void loadkey(SecretKey newKey) {
        key = newKey;
    }

    public static byte[] encrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
        return cipher.doFinal(data);

    }

    public static byte[] decrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
        return (cipher.doFinal(data));
    }

    public static void encryptFile(String src, String des) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] data = Files.readAllBytes(new File(src).toPath());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
        byte[] newData = cipher.doFinal(data);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(des)));
        bos.write(newData);
        bos.flush();
        bos.close();


    }

    public static void decryptFile(String src, String des) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] data = Files.readAllBytes(new File(src).toPath());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
        byte[] newData = cipher.doFinal(data);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(des)));
        bos.write(newData);
        bos.flush();
        bos.close();


    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException {
//        genKey();
//        generateIV();
//        byte[] data = encrypt("DES (Data Encryption Standard) lÃ  má»™t thuáº­t toÃ¡n mÃ£ hÃ³a Ä‘á»‘i xá»©ng Ä‘Æ°á»£c phÃ¡t triá»ƒn vÃ o nhá»¯ng nÄƒm 1970 vÃ  trá»Ÿ thÃ nh tiÃªu chuáº©n quá»‘c gia cho mÃ£ hÃ³a dá»¯ liá»‡u á»Ÿ Má»¹. DÆ°á»›i Ä‘Ã¢y lÃ  nhá»¯ng Ä‘iá»ƒm chÃ­nh vá»� DES:\r\n"
//                + "\r\n"
//                + "NguyÃªn lÃ½ hoáº¡t Ä‘á»™ng\r\n"
//                + "KhÃ³a bÃ­ máº­t: DES sá»­ dá»¥ng má»™t khÃ³a bÃ­ máº­t dÃ i 56 bit Ä‘á»ƒ mÃ£ hÃ³a vÃ  giáº£i mÃ£ dá»¯ liá»‡u.\r\n"
//                + "Chia khá»‘i: Dá»¯ liá»‡u Ä‘Æ°á»£c chia thÃ nh cÃ¡c khá»‘i 64 bit.\r\n"
//                + "VÃ²ng mÃ£ hÃ³a: Thuáº­t toÃ¡n thá»±c hiá»‡n 16 vÃ²ng mÃ£ hÃ³a, má»—i vÃ²ng bao gá»“m cÃ¡c phÃ©p toÃ¡n nhÆ°:\r\n"
//                + "Sá»­ dá»¥ng hÃ m F (gá»“m phÃ©p XOR, permute vÃ  cÃ¡c hÃ m S-box) Ä‘á»ƒ biáº¿n Ä‘á»•i dá»¯ liá»‡u.\r\n"
//                + "Thay Ä‘á»•i vá»‹ trÃ­ cÃ¡c bit qua cÃ¡c phÃ©p hoÃ¡n vá»‹.\r\n"
//                + "Ä�áº·c Ä‘iá»ƒm\r\n"
//                + "MÃ£ hÃ³a Ä‘á»‘i xá»©ng: CÃ¹ng má»™t khÃ³a Ä‘Æ°á»£c sá»­ dá»¥ng cho cáº£ mÃ£ hÃ³a vÃ  giáº£i mÃ£.\r\n"
//                + "Báº£o máº­t: Máº·c dÃ¹ DES tá»«ng Ä‘Æ°á»£c coi lÃ  an toÃ n, nhÆ°ng vá»›i sá»± phÃ¡t triá»ƒn cá»§a cÃ´ng nghá»‡ tÃ­nh toÃ¡n, nÃ³ Ä‘Ã£ trá»Ÿ nÃªn kÃ©m an toÃ n do dá»… bá»‹ táº¥n cÃ´ng brute-force.\r\n"
//                + "Thay tháº¿: DES Ä‘Ã£ Ä‘Æ°á»£c thay tháº¿ bá»Ÿi AES (Advanced Encryption Standard) vÃ o Ä‘áº§u nhá»¯ng nÄƒm 2000.\r\n"
//                + "á»¨ng dá»¥ng\r\n"
//                + "DES tá»«ng Ä‘Æ°á»£c sá»­ dá»¥ng rá»™ng rÃ£i trong cÃ¡c á»©ng dá»¥ng thÆ°Æ¡ng máº¡i, nhÆ°ng hiá»‡n nay chá»§ yáº¿u lÃ  trong cÃ¡c há»‡ thá»‘ng cÅ© hoáº·c cho má»¥c Ä‘Ã­ch há»�c thuáº­t.\r\n"
//                + "Náº¿u báº¡n cáº§n thÃ´ng tin chi tiáº¿t hÆ¡n vá»� cÃ¡ch thá»©c hoáº¡t Ä‘á»™ng hoáº·c má»™t vÃ­ dá»¥ cá»¥ thá»ƒ, hÃ£y cho tÃ´i biáº¿t!");
//        System.out.println(new String(data));
//        System.out.println(decrypt(data));
//        String src = "D:\\ATBMHT\\LAB 4.DOCX";
//        String des = "D:\\ATBMHT\\encr_LAB 4.DOCX";
//        String decr = "D:\\ATBMHT\\decr_LAB 4.DOCX";
//        encryptFile(src, des);
//        decryptFile(des, decr);
    }
}
