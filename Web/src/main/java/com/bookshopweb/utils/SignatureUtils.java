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
//        System.out.println(signatureUtils.getPrivateKeyBase64());
//        System.out.println("\n");
//
//        System.out.println(signatureUtils.getPublicKeyBase64());
        System.out.println(signatureUtils.loadPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAitnQrchz1jZP28opOQka5JnWeKHtIOnZY/3AQXJUO3Jl695ZQ27oDpXVivU1gHcT3GMZJQ78XwFAl9ObBceU91U3JIAQ4mvUbfCcDRe+cdkNtC4FHqgKYouI0aXztYrzc3Eu5HS8FyHg2GwVx/SEaiBATSBIoUK1cJAkUS3tWdJVQy9F3eU9EctpGO28m9h6fAsD0bO6u6F993jnFAY9ZA1+HnVGkeQDwjGJC4pFc73TILs4seyF8hoqKocZjnS7r8iXZueDuIZEs/LNoEwxN5K2PqVnJYfN8YUH63pF2dUMfaGADUZbRTR43UrZnbTdV/MGGz+CnVc/alPHMysHjwIDAQAB"));

        System.out.println(signatureUtils.loadPrivateKey("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCK2dCtyHPWNk/byik5CRrkmdZ4oe0g6dlj/cBBclQ7cmXr3llDbugOldWK9TWAdxPcYxklDvxfAUCX05sFx5T3VTckgBDia9Rt8JwNF75x2Q20LgUeqApii4jRpfO1ivNzcS7kdLwXIeDYbBXH9IRqIEBNIEihQrVwkCRRLe1Z0lVDL0Xd5T0Ry2kY7byb2Hp8CwPRs7q7oX33eOcUBj1kDX4edUaR5APCMYkLikVzvdMguzix7IXyGioqhxmOdLuvyJdm54O4hkSz8s2gTDE3krY+pWclh83xhQfrekXZ1Qx9oYANRltFNHjdStmdtN1X8wYbP4KdVz9qU8czKwePAgMBAAECggEADAT/8KiQsniVdXrCEnBh1hZ6ajXmNOUEDwj+dWE5aRiljwNP6dZLcJvIK6pqB7qxyvBUfbtBiH3aGBUsGcDQTzrJYQz+/oKuwGzwLUf4EfCOpEwe4FZT6STtYpdBigkYZaQPbEet/TikYfJf+Ppcjg1IU6+EP1rAIcQwiKB3p+s8aNpiX6qEXK7pXxk4eqdNEuqEa80KFaRXIG/oMfx7aU4YvMbe3CxOfeq5qHZtE0f4b3jb/tU4mY9n91oTtqXoEQkiHGzNpF0FUD7ekL5C1rjiL/0Ekl78P/+YiwupSid+rFD0/w5ggMc10K9aQZ/lwiH+AaGjZ3oCoFDUdFqp+QKBgQDCFIgrU39X/bmI+hjD+xqw7Pyi6DCStsaTt7J/s6oVoKkm4T75XFS6eN4s06jWMVjKbfKlB3brffp9bcxs1dasTdHfcSQ+xB0Vmn6mzkKPT91Fte3N37akfPB1qO6hCgUeM4/dUnvsK7vZpiI8fD9X+3h0Qe07EXRULM+5C8OE1wKBgQC3Jm8vFcsOPuNqx9t+6OAnkukD34qjHGnjWPnDygLw3RUlHmjdfGm8LcwaYWBiPTIbxg+en1tA5+2yrK2KWya16RaT2Z+1QzqXnmm2u627diRzUKP1kLbGCAZYCQCTriLobJNuKKUiv78BN00uuBX/SW6wKNy6XlwDkgOK/4gECQKBgQCxqAfJ2UHtcuVeXMlExNv27siH+xFTi5oU6AeuW/EqoQmV/IQf0QQCOdfuosWfZEaJ7sE5ja6u/H/KRzLkwY4j5YxjA/eQuQaCmwyqdo55DztWr6WCWfGfenrGyZ++rCRpM/vSNcoYMyRepHQceAOn7xqhGSQmeODKmMaA9iLAuwKBgQCY17ZJsQI37TuUTR1cF7OY0v0qgk8MOBYV6JbnUZqKr7WJuIsC62TB6eY+zazFzxQWIX+/fHWHAQZY6coZgGAZx19UG3Uwq94l0cme+ZjOrJVWtqFduVsOgHvq0/hrRhNTq+iNMdY93Y7k0OXUa0PQO9PnpvIthbl8MUpLoVw9IQKBgHdUtACumZz9ZpORP50ydN1ZhF3Dxn3nlGFI08vWy+3Wxzds9DaDsskAyNDIonlO/ve8QEue6LZr+5wm3jJlN7RZJj9jFwtubiN39L6HD0UslP1VhTryApQA+pG7MXvWnCjZAHZ9y+TvXJmQLF5yQDvs+KWDV9SXpt7vZtEO/dae"));
        System.out.println(signatureUtils.authenticate());
    }

}