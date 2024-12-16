package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
	public static int[] getHashSize(String algo) {
		switch (algo) {
		case "SHA-1": {

			return new int[] { 160 };
		}
		case "SHA-2": {
			return new int[] { 224, 256, 384, 512 };
		}
		case "SHA-3": {
			return new int[] { 224, 256, 384, 512 };
		}
		default:
			return new int[] { 128 };
		}
	}

	public static String hash(String data){
		try {
			String algo = "SHA-2";
			int hashSize = 256;
			MessageDigest md = MessageDigest.getInstance(getIntance(algo, hashSize));
			byte[] bytes = data.getBytes("UTF-8");
			byte[] digest = md.digest(bytes);
			BigInteger re = new BigInteger(1, digest);
			return re.toString(16);
		} catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	public static String hashFile(String filePath, String algo, int hashSize) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance(getIntance(algo, hashSize));
		try (FileInputStream fis = new FileInputStream(filePath)) {
			byte[] dataBytes = new byte[1024];
			int bytesRead;

			while ((bytesRead = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, bytesRead);
			}
		}
		byte[] digest = md.digest();
		BigInteger bigInteger = new BigInteger(1, digest);
		return bigInteger.toString(16);
	}
	public static String getIntance(String algo, int hashSize) {
    	switch (algo) {
    	case "SHA-2": {
    		
    		return "SHA-"+hashSize;
    	}
		case "SHA-3": {
			
			return "SHA3-"+hashSize;
		}
		
		default:
			return algo;
		}
    }

	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(hash("123123122"));;
	}

}