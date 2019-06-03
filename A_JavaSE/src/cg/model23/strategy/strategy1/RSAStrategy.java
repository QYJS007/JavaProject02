package cg.model23.strategy.strategy1;

public class RSAStrategy implements IEncryptStrategy {
    @Override
    public String encryptStr(String params) {
        return "经过RSA加密过数据";
    }
}