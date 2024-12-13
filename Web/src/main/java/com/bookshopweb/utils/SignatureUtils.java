package com.bookshopweb.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.util.UUID;

public class SignatureUtils {
    private KeyPair keyPair;
    private SecureRandom secureRandom;
    private Signature signature;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    public static final int KEY_SIZE = 2048;
    public static final String MAIN_ALGO = "RSA", PROVIDER = "BC";

    static {
        Security.addProvider(new BouncyCastleProvider()); // Thêm provider Bouncy Castle
    }

    public void genKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(MAIN_ALGO, PROVIDER); // Sử dụng Bouncy Castle

        // Sử dụng provider mặc định cho SecureRandom
        secureRandom = SecureRandom.getInstance("SHA1PRNG"); // Không chỉ định provider, sử dụng mặc định
        generator.initialize(KEY_SIZE, secureRandom); // Độ dài khóa DSA
        signature = Signature.getInstance("SHA1withRSA", PROVIDER); // Sử dụng Bouncy Castle
        keyPair = generator.genKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

    }

    public String sign(String mes) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException {
        byte[] data = mes.getBytes();
        signature = Signature.getInstance(privateKey.getAlgorithm(), PROVIDER);
        signature.initSign(privateKey);
        signature.update(data);
        byte[] signData = signature.sign();
        return Base64.getEncoder().encodeToString(signData);
    }

    public String signFile(String src) throws InvalidKeyException, SignatureException, IOException, NoSuchAlgorithmException, NoSuchProviderException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        signature = Signature.getInstance(privateKey.getAlgorithm(), PROVIDER);
        signature.initSign(privateKey);
        byte[] data = new byte[2048];
        int length;
        while ((length = bis.read(data)) != -1) {
            signature.update(data, 0, length);

        }
        byte[] signData = signature.sign();
        return Base64.getEncoder().encodeToString(signData);

    }

    public boolean verifyFile(String src, String sign) throws InvalidKeyException, SignatureException, IOException, NoSuchAlgorithmException, NoSuchProviderException {
        signature = Signature.getInstance(privateKey.getAlgorithm(), PROVIDER);
        signature.initVerify(publicKey);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        byte[] data = new byte[2048];
        int length;
        while ((length = bis.read(data)) != -1) {
            signature.update(data, 0, length);

        }
        byte[] signData = Base64.getDecoder().decode(sign);
        return signature.verify(signData);
    }

    public boolean verify(String mes, String sign) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException {
        byte[] data = mes.getBytes();
        byte[] signData = Base64.getDecoder().decode(sign);
        signature = Signature.getInstance(privateKey.getAlgorithm(), PROVIDER);
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(signData);
    }


    public boolean saveKey(String desFile) throws IOException {
        if (publicKey == null || privateKey == null) {
            return false;
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(desFile));
        String keyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        printWriter.write(keyString);
        printWriter.close();
        return true;

    }
    // Load publicKey String từ database
    public boolean loadPublicKey(String publicKeybase64) {
        try {

            byte[] keyData = Base64.getDecoder().decode(publicKeybase64);
            KeyFactory keyFactory = KeyFactory.getInstance(MAIN_ALGO);

            // Tạo PKCS8EncodedKeySpec từ mảng byte
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyData);

            // Tạo PublicKey từ KeyFactory
            publicKey = keyFactory.generatePublic(keySpec);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }
    // Load PrivateKey String khi người dùng nhập vào (chuỗi hoặc file chuyển về chuỗi)
    public boolean loadPrivateKey(String privateKeyBase64) throws IOException {
        try {
            byte[] keyData = Base64.getDecoder().decode(privateKeyBase64);

            KeyFactory keyFactory = KeyFactory.getInstance(MAIN_ALGO);

            // Tạo PKCS8EncodedKeySpec từ mảng byte
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyData);

            // Tạo PrivateKey từ KeyFactory
            privateKey = keyFactory.generatePrivate(keySpec);
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    // Sử dụng khi người dùng load privateKey thành công và kiểm tra privateKey đó có đúng dới pubicKey lưu trong database không
    public boolean authenticate() throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        if(publicKey == null || privateKey == null){
            return false;
        }else{
            String inputTest = UUID.randomUUID().toString();
            String signTest = sign(inputTest);
            return verify(inputTest, signTest);
        }
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKeyBase64() {
        if (publicKey != null) return Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return null;
    }

    public String getPrivateKeyBase64() {
        if (publicKey != null) return Base64.getEncoder().encodeToString(privateKey.getEncoded());
        return null;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException, InvalidKeyException, IOException, InvalidKeySpecException, ClassNotFoundException {
        SignatureUtils signatureUtils = new SignatureUtils();
        signatureUtils.genKey();
        String sign = signatureUtils.sign("123123");
//        System.out.println(signatureUltils.verify("123123", sign));
//        System.out.println(signatureUltils.getPrivateKeyBase64());
        System.out.println(signatureUtils.authenticate());
        System.out.println(signatureUtils.loadPrivateKey("MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC7q/0xr/ubEbZJt9DVEzvEihk99SUHKkfAM1o3aPB3itb2rAoxjO9i6azgXqwVEzdDLR/4k2dxEmsxet8FnjP3sBnP1OFyMXaw+tpLWyUu8UdsXV7FPy2QRWml2jcVRZ6JKnd0PR/IYslu6DiO8OvMK33q3BW3eOwZhI7VznE8vyHy7xwglE5KuRDo/qhGN5lIultvfo/xfn8UJ8zO/w+qUmgFUYjvXqznUZ6/0InpXCdv7xR4GQejPl/DEw01VtPH7XW+F8KHJ4ScbiCTR8MjTKeB9bBRL3/qfHMb3+Yo/1WYAIU3NUF9k0zPdGnbCWfr4F0RX/ny7rU8mHXWaRrHAgMBAAECggEAFuPj5n8R/7h51OT8LjkB1beIW6O+Z/DSvpEk5FRC3DnNfVm/hoqrmhNpWBaEA7AL0gA7gYytFcAK3gagFCXXLdNHWq+a7dw1n2GBZaaQ0vhv3zpGOGW7z3h/6dBDXq9WpmFNH5ri+QR/kCz31Qtynz0Axzgf1/iCR5yA9XgQHLn30Snr64M6b6ogzUV4gcC5PIKxBrpLsIDVNjwmy6Oi6P6A2/cX9gdfy8RxCjyskucxfDe22OwLsFcviFGqrLJpgfEWwYEAHEDki10hPW/8zcprfUdiPgBhwqz9jx+SO0aQA/fUl5nEbelnKJ6zzB5VkVLzH18mBS01v1NkxHgISQKBgQDiauFPqoYUlJlQUu9gp1mK8+GlAg3zbK+uO1lipY1sYV8lmUweHMfc9TYsXa6dSPjaYgvQt6kzUvON8ubnvu8M3J6SRb9w5FcGGTn6cFG/i0u8+WJf7YA0GRr9e81UljxWKuYhF8Vt8tFVUNAx1ZeLjD4M5N0XWtClpi2gOeDgKQKBgQDUMSjfvjB+sutb2f6BVx8qTHVBLs19UNxnakHBZDa9ishROA95zo8htCEW7WQMUFSOfHu02ORibsj5YZSJDULcSQNhgWqJvMmVQj/VzBEuLfE25KeBeRL8GnCl9wTdvG8k8ZVqyVLHHDWP/GFNmLZTldqBKdQTFlMZMbkWRsHBbwKBgQC+2o8gA0LQWE4/uhkT5R0Clv9Og0PVPHl+KYL4N0rV3+I/JFBYVbRXWc+XaESHSnqSQvFot1gF7ldlASqIkJaaxeFYtThhZejBOER6CXGvPzDRk3nMGLvnrn3kzBXRxm4HyFOB87K1MmgoPV2xlDURKV8oqUaAqEmWSFTPadpXuQKBgQCxc3ciPfRiUJCBXPn5Annertg0LcbNcQ0RMwxQhdU9h81kFsTicbrDTdeKS/aqUZeVHUd7AxNjuhA6db95KezrOego58jH7WiLeT7XNfR8MRGTfX8TyMP2rsncEYsxQD+Wnq39AuZF+zJT+pUJBpgbqKt17xwa5kWSQUHu/jdU4wKBgQCh+q5wmv1/A/NDDkYC8ZDAUTn9zzxJFggX9W1tzHs08i2deGji9XWfCkDW3T0zZHUOcI7uLvNsVmjsO2sDG/r3gH2W/nwcXtRzDh9sIs2EjrJsmnKkiAgCqhSxeDaHufPXu9IP00JbJN4xJAJAng6VZGO1VFk2sZFHCq+NOHiQJg=="));
        System.out.println(signatureUtils.authenticate());
    }

}