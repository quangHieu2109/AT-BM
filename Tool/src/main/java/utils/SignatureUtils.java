package utils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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

    public SignatureUtils() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(MAIN_ALGO, PROVIDER); // Sử dụng Bouncy Castle

            // Sử dụng provider mặc định cho SecureRandom
            secureRandom = SecureRandom.getInstance("SHA1PRNG"); // Không chỉ định provider, sử dụng mặc định
            generator.initialize(KEY_SIZE, secureRandom); // Độ dài khóa DSA
            signature = Signature.getInstance("SHA1withRSA", PROVIDER); // Sử dụng Bouncy Castle
            keyPair = generator.genKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void genKey(){

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

    }

    public String sign(String mes){
       try {
           byte[] data = mes.getBytes();
           signature = Signature.getInstance(MAIN_ALGO, PROVIDER);
           signature.initSign(privateKey);
           signature.update(data);
           byte[] signData = signature.sign();
           return Base64.getEncoder().encodeToString(signData);
       } catch (Exception e) {
           throw new RuntimeException(e);

       }
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
        signature = Signature.getInstance(MAIN_ALGO, PROVIDER);
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

    public boolean verify(String mes, String sign){
      try {
          byte[] data = mes.getBytes();
          byte[] signData = Base64.getDecoder().decode(sign);
          signature = Signature.getInstance(MAIN_ALGO, PROVIDER);
          signature.initVerify(publicKey);
          signature.update(data);
          return signature.verify(signData);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
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

            // Tạo X509EncodedKeySpec từ mảng byte
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyData);

            // Tạo PublicKey từ KeyFactory
            publicKey = keyFactory.generatePublic(keySpec);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }
    // Load PrivateKey String khi người dùng nhập vào (chuỗi hoặc file chuyển về chuỗi)
    public boolean loadPrivateKey(String privateKeyBase64)  {

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
//        signatureUtils.genKey();

        System.out.println(signatureUtils.loadPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1GkeeO9HHxBa23kzWnCrjuqV20/MrYbYrl/wbm40r0yhcdpvGFIoJNtPc8tnlz7qDPWC1sWpZaqk6a2FWdX4wdlvE2ClLyAEBgo1hQBclHWgtQt/PqcHcjhRRaV9ffhsn5IsnKMrrDz3NqDCxT3qUHTTZZUJ6lSGClsx9TLuAa7ZLyrftW2AD17V916MrWf5ArardJDABCvO7/2WYuYrn1MR0e/T4btyzTtE8bSIaDAu2AUkpWx6WyZG89p1gARSLM/Uh92ClKsWuHbPqcmqhti/EPFQ3mbGV5BawFkpvfbCmtNezhrx0ZlZU/ADopoctImhH10iJgWH9ye5tF1xmQIDAQAB"));
        System.out.println(signatureUtils.loadPrivateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDUaR5470cfEFrbeTNacKuO6pXbT8ythtiuX/BubjSvTKFx2m8YUigk209zy2eXPuoM9YLWxallqqTprYVZ1fjB2W8TYKUvIAQGCjWFAFyUdaC1C38+pwdyOFFFpX19+GyfkiycoyusPPc2oMLFPepQdNNllQnqVIYKWzH1Mu4BrtkvKt+1bYAPXtX3XoytZ/kCtqt0kMAEK87v/ZZi5iufUxHR79Phu3LNO0TxtIhoMC7YBSSlbHpbJkbz2nWABFIsz9SH3YKUqxa4ds+pyaqG2L8Q8VDeZsZXkFrAWSm99sKa017OGvHRmVlT8AOimhy0iaEfXSImBYf3J7m0XXGZAgMBAAECggEAJyH+tTUQG07+DjU0GtNrJ/dcCH2ZLdcMEIQoXY/8MNejXsBpe0eXkcK9zo2I1jqHEEAjDMJ5xPs9SrfnO3fKTpxqdF130UjKtPoohgpdBBTvuKsXlElde9OYVYZ5qj2cdYGOqoT9RMwul2fiukf/5TbAw5RDsWJFF59Zgt0RMEnaKtH3XSz/n71+z01tyd93faISK0IFIuakhBvFnVdeQ5PcTwCma3mTNxdj8T+5ijGXR+R68NSfb4nZ/UPHntKhwErATyHG2hp3B2VynjXiRc24xe7AOZWe9Sw5q4rjAEHVWBxzJJNqMOcD0cq5xA3u2+zw/1Ej8Resf6ZIBG/76QKBgQD40vfzzt0jkdCwlkHcdzjRnJU67CRJnM1mGcv4TGTb2r4CoRLmbV6bPQA2BVrREX3DAWsFbrB9GrAgb/P2GSMqNuoNd4puxxdGCripml+l5aC8XeA6wjf90srHKMVHjx6RLMFNQL/MRtP9n0EXCnzMX+LwmzRSe+REJEWrBckqWwKBgQDaiVCvnmHz/Qs6xuv763COYLZJMfSd/Z1vmwhSHdzfaqNlCwRrRJ1TX1MfXOacoAivOyZWkTGdlRMv0hm0T40jqHSAKjCaMAOn7iVnqxKnN8HbhkErpRHk1BY2mjzFk+DaKUs0QqBV623cbkd3B9DsNw56USSg+LcBmeqWfY8OGwKBgQCDNMvm1kgpv3QqCbGFDaD6dvUB8w9XecWddzDlJ3NvszLKtCEBPN7MQShhVAWFhRGpyI+kd6+86FvDXwVn2e1/DyHwI/7tVzJgjrq9RFf3ZNSTP3VDxrI7t/GRf2A30bIun0j28ZOxpg+XB2kNO0x/gHhso4q9i/LcM7xA6HYhTQKBgDfJefl6+xJn9Gfqw+8paTBPa/k/cWyrg3csInGkAfBerptqtpQTjf3shbGp64zsJYtmFCFhK0NI/P7mKX2oGSWTKTpWitWxkB1cY9wVBcv+JdS+bwCPQQQjSBBkS0M8vteyVupeX8PPN9pfpIq0Shci7uU2tZ0KIN2dxw7nuo8lAoGAUHBR7YXxkpcNzWV7j6XQ7aiAhHzi30Bnar8XbvLLFlYxJpxuvLJ4Egnr0epd7vbg4W1h5JFUkE1mMQzrDLGQXqZ7/aGdX/AljVbrLGELsoylSmU1j16fsicb/AKuyuw+QPJkMjLrRQAGPVkPdAPLZzkyf4u3pBYC18okCafvxr8="));
        System.out.println(signatureUtils.authenticate());
        System.out.println("Private: "+ signatureUtils.getPrivateKeyBase64());
        System.out.println("\n");
        System.out.println("Public: "+ signatureUtils.getPublicKeyBase64());
    }

}