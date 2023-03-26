package ai.kalico.api.service.youtubej.cipher;


import ai.kalico.api.service.youtubej.YoutubeException;

public interface CipherFactory {

    Cipher createCipher(String jsUrl) throws YoutubeException;

    void addInitialFunctionPattern(int priority, String regex);

    void addFunctionEquivalent(String regex, CipherFunction function);
}
