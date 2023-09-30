package org.miowing.mioverify.config;

import lombok.extern.slf4j.Slf4j;
import org.miowing.mioverify.util.KeysUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.*;

@Configuration
@Slf4j
public class KeyInitializer {
    @Value("${mioverify.security.public-key-loc}")
    private Path publicKeyLoc;
    @Value("${mioverify.security.private-key-loc}")
    private Path privateKeyLoc;
    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException {
        try {
            PublicKey publicKey = KeysUtil.readPublicKeyFromPem(publicKeyLoc);
            PrivateKey privateKey = KeysUtil.readPrivateKeyFromPem(privateKeyLoc);
            String x = "test";
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);
            signature.update(x.getBytes());
            byte[] x0 = signature.sign();
            Signature verifySign = Signature.getInstance("SHA1withRSA");
            verifySign.initVerify(publicKey);
            verifySign.update(x.getBytes());
            verifySign.verify(x0);
            log.info("Key pair of signature initialized.");
            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = gen.generateKeyPair();
            log.info("No key pair provided. Automatically generated.");
            try {
                if (Files.notExists(publicKeyLoc.getParent())) {
                    Files.createDirectories(publicKeyLoc.getParent());
                }
                if (Files.notExists(privateKeyLoc.getParent())) {
                    Files.createDirectories(privateKeyLoc.getParent());
                }
                if (Files.notExists(publicKeyLoc)) {
                    Files.write(publicKeyLoc, new byte[]{}, StandardOpenOption.CREATE);
                }
                if (Files.notExists(privateKeyLoc)) {
                    Files.write(publicKeyLoc, new byte[]{}, StandardOpenOption.CREATE);
                }
                KeysUtil.writePublicKeyToPem(keyPair.getPublic(), publicKeyLoc);
                KeysUtil.writePrivateKeyToPem(keyPair.getPrivate(), privateKeyLoc);
            } catch (Exception e0) {
                log.warn("Auto-generated keys can not be written to pem.");
            }
            return keyPair;
        }
    }
}