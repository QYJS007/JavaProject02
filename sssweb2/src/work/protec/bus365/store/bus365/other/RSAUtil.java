package work.protec.bus365.store.bus365.other;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import utils.AlgorithmUtils;


/**
 * RSA 工具类。提供加密，解密，生成密钥对等方法。
 * 需要到http://www.bouncycastle.org下载bcprov-jdk14-123.jar。
 * 
 */
public class RSAUtil {
	
	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";
	 /**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;
	
	
	/**
	 * 生成密钥对
	 * @author 
	 * @date 2015年7月24日 上午08:00:00
	 */
	public static KeyPair generateKeyPair() throws Exception {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
			final int KEY_SIZE = 1024;// 没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
			keyPairGen.initialize(KEY_SIZE, new SecureRandom());
			KeyPair keyPair = keyPairGen.generateKeyPair();
			saveKeyPair(keyPair);
			return keyPair;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * 从本地文件读取密钥对
	 * @author 
	 * @date 2015年7月24日 上午08:00:00
	 */
	public static KeyPair getKeyPair()throws Exception{
		try{
			 InputStream fis = RSAUtil.class.getResourceAsStream("RSAKey.txt");
			 ObjectInputStream oos = new ObjectInputStream(fis);
			 KeyPair kp= (KeyPair) oos.readObject();
			 oos.close();
			 fis.close();
			 return kp;
		}catch(Exception e){
			throw new Exception("ERROR.UTIL.GET_KEYPARI_ERROR",e);
		}
		
	}
	
	/**
	 * 保存密钥对到本地文件
	 * @author 
	 * @date 2015年7月24日 上午08:00:00
	 * @params kp 要保存的密钥对
	 */
	public static void saveKeyPair(KeyPair kp)throws Exception{
		
		 FileOutputStream fos = new FileOutputStream("F:\\_workspace\\sss\\src\\RSAKey.txt");
		 ObjectOutputStream oos = new ObjectOutputStream(fos);
		 //生成密钥
		 oos.writeObject(kp);
		 oos.close();
		 fos.close();
	}
	
	/**
	 * 生成公钥
	 * @author 
	 * @date 2015年7月24日 上午08:00:00
	 * @param modulus *
	 * @param publicExponent *
	 * @return RSAPublicKey *
	 * @throws Exception
	 */
	public static RSAPublicKey generateRSAPublicKey(byte[] modulus,
			byte[] publicExponent) throws Exception {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance("RSA",
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} catch (NoSuchAlgorithmException ex) {
			throw new Exception(ex.getMessage());
		}

		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(
				modulus), new BigInteger(publicExponent));
		try {
			return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
		} catch (InvalidKeySpecException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * 生成私钥
	 * @author 
	 * @date 2015年7月24日 上午08:00:00
	 * @param modulus *
	 * @param privateExponent *
	 * @return RSAPrivateKey *
	 * @throws Exception
	 */
	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus,
			byte[] privateExponent) throws Exception {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance("RSA",
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} catch (NoSuchAlgorithmException ex) {
			throw new Exception(ex.getMessage());
		}

		RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(
				modulus), new BigInteger(privateExponent));
		try {
			return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
		} catch (InvalidKeySpecException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * 加密
	 * @author 
	 * @date 2015年7月24日 上午08:00:00
	 * @param key  加密的密钥 *
	 * @param data 待加密的明文数据 *
	 * @return 加密后的数据 *
	 * @throws Exception
	 */
	public static byte[] encrypt(PublicKey pk, byte[] data) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA",
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, pk);
			int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
			// 加密块大小为127
			// byte,加密后为128个byte;因此共有2个加密块，第一个127
			// byte第二个为1个byte
			int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
			int leavedSize = data.length % blockSize;
			int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
					: data.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (data.length - i * blockSize > 0) {
				if (data.length - i * blockSize > blockSize)
					cipher.doFinal(data, i * blockSize, blockSize, raw, i
							* outputSize);
				else
					cipher.doFinal(data, i * blockSize, data.length - i
							* blockSize, raw, i * outputSize);
				// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到
				// ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了
				// OutputSize所以只好用dofinal方法。

				i++;
			}
			return raw;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 解密
	 * @author 
	 * @date 2015年7月24日 上午08:00:00
	 * @param key 解密的密钥 *
	 * @param raw 已经加密的数据 *
	 * @return 解密后的明文 
	 * @throws Exception
	 */
	public static byte[] decrypt(PrivateKey pk, byte[] raw) throws Exception  {
		try {
			Cipher cipher = Cipher.getInstance("RSA",
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, pk);
			int blockSize = cipher.getBlockSize();
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
			int j = 0;

			while (raw.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
				j++;
			}
			return bout.toByteArray();
		} catch (Exception e) {
			throw new Exception("ERROR.UTIL.SRA_DECRYPT_ERROR",e);
		}
	}
	
	/**
	 * 根据modulus,public exponent得到PublicKey
	 * @author 
	 * @date 2015年7月24日 上午08:00:00
	 * @param modulus 十进制的modulus串
	 * @param publicExponent 十进制的publicExponent串
	 * @return PublicKey类型的公钥
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String modulus, String publicExponent) throws Exception {
		//将字符串转换为BigInteger类型
		BigInteger modulusBigInt = new BigInteger(modulus, 16);
		BigInteger publicExponentBigInt = new BigInteger(publicExponent, 16);

		KeySpec publicKeySpec = new RSAPublicKeySpec(modulusBigInt, publicExponentBigInt);
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = factory.generatePublic(publicKeySpec);
		return publicKey;
	}
	
	/**
	 * 根据modulus,private exponent得到PrivateKey
	 * @author 
	 * @date 2015年7月24日 上午08:00:00
	 * @param modulus 十进制的modulus串
	 * @param privateExponent 十进制的privateExponent串
	 * @return PrivateKey类型的公钥
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String modulus, String privateExponent) throws Exception {
		//将字符串转换为BigInteger类型
		BigInteger modulusBigInt = new BigInteger(modulus, 16);
		BigInteger privateExponentBigInt = new BigInteger(privateExponent, 16);

		KeySpec privateKeySpec = new RSAPrivateKeySpec(modulusBigInt, privateExponentBigInt);
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = factory.generatePrivate(privateKeySpec);
		return privateKey;
	}
	
	/**
	 * <p>
	 * 私钥加密
	 * </p>
	 * 
	 * @param data 源数据
	 * @param privateKey 私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPrivateKey(String datastr, String privateKey)
			throws Exception {
		byte[] data = datastr.getBytes();
		byte[] keyBytes = AlgorithmUtils.decodeBase64(privateKey);
//        byte[] keyBytes = Base64Utils.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return AlgorithmUtils.encodeBase64(encryptedData);
//        return Base64.encode(encryptedData);
	}
	
	/**
	 * <P>
	 * 私钥解密
	 * </p>
	 * 
	 * @param encryptedData 已加密数据
	 * @param privateKey 私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String encrypted, String privateKey)
			throws Exception {
//		byte[] encryptedData = Base64.decode(encrypted);
		byte[] encryptedData = AlgorithmUtils.decodeBase64(encrypted);
//        byte[] keyBytes = Base64Utils.decode(privateKey);
		byte[] keyBytes = AlgorithmUtils.decodeBase64(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return new String(decryptedData);
	}
}