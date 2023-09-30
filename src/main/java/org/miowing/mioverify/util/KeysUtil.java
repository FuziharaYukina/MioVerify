package org.miowing.mioverify.util;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Operations of PEM files.
 */
public class KeysUtil {
    public static String writePublicKeyToString(PublicKey publicKey) throws IOException {
        PemObject po = new PemObject("PUBLIC KEY", publicKey.getEncoded());
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(po);
        pemWriter.close();
        return stringWriter.toString();
    }
    public static void writePublicKeyToPem(PublicKey publicKey, Path keyFilePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(keyFilePath.toFile()); PemWriter pemWriter = new PemWriter(fileWriter)) {
            pemWriter.writeObject(new PemObject("RSA PUBLIC KEY", publicKey.getEncoded()));
        }
    }
    public static PublicKey readPublicKeyFromPem(Path keyFilePath)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try (FileReader fileReader = new FileReader(keyFilePath.toFile()); PemReader pemReader = new PemReader(fileReader)) {
            PemObject pemObject = pemReader.readPemObject();
            X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(pemObject.getContent());
            return KeyFactory.getInstance("RSA").generatePublic(encPubKeySpec);
        }
    }
    public static void writePrivateKeyToPem(PrivateKey privateKey, Path keyFilePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(keyFilePath.toFile()); PemWriter pemWriter = new PemWriter(fileWriter)) {
            pemWriter.writeObject(new PemObject("RSA PRIVATE KEY", privateKey.getEncoded()));
        }
    }
    public static PrivateKey readPrivateKeyFromPem(Path keyFilePath)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try (FileReader fileReader = new FileReader(keyFilePath.toFile()); PemReader pemReader = new PemReader(fileReader)) {
            PemObject pemObject = pemReader.readPemObject();
            PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(pemObject.getContent());
            return KeyFactory.getInstance("RSA").generatePrivate(encPriKeySpec);
        }
    }
}