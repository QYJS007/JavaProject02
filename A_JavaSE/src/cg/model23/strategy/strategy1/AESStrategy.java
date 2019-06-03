package cg.model23.strategy.strategy1;

public class AESStrategy implements IEncryptStrategy {
    @Override
    public String encryptStr(String params) {
        return "经过AES加密过的数据";
    }
}