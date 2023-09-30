package org.miowing.mioverify.util;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.miowing.mioverify.pojo.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.security.*;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO make all methods not static
 * Main util.
 */
@Component
public class Util implements InitializingBean {
    @Autowired
    private DataUtil dataUtil;
    @Autowired
    private KeyPair keyPair;
    private static final Base64.Encoder base64Encoder = Base64.getEncoder();
    private PublicKey publicKey;
    private PrivateKey privateKey;
    @Getter
    @Autowired
    private ServerMeta serverMeta;
    @Override
    public void afterPropertiesSet() throws Exception {
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        serverMeta.setSignaturePublicKey(getPublicKeyStr());
    }
    public String getPublicKeyStr() {
        try {
            return KeysUtil.writePublicKeyToString(publicKey);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getTextureURL(String hash) {
        return getServerURL() + "/texture/hash/" + hash;
    }
    public String getServerURL() {
        return dataUtil.isUseHttps() ? "https://" : "http://" + dataUtil.getServerDomain() + ":" + dataUtil.getPort();
    }
    public String signature(String value) {
        try {
            Signature signature = Signature.getInstance(dataUtil.getSignAlgorithm());
            signature.initSign(privateKey);
            signature.update(value.getBytes());
            return base64ByteArray(signature.sign());
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }
    public static String base64ByteArray(byte[] b) {
        return new String(base64Encoder.encode(b));
    }
    public static String base64Str(String str) {
        return new String(base64Encoder.encode(str.getBytes()));
    }
    public UserShow userToShow(User user) {
        List<UserShow.Property> props = new LinkedList<>();
        if (user.getPreferredLang() != null) {
            props.add(
                    new UserShow.Property()
                            .setName("preferredLanguage")
                            .setValue(user.getPreferredLang())
            );
        }
        return new UserShow()
                .setId(user.getId())
                .setProperties(props);
    }
    public ProfileShow profileToShow(Profile profile, boolean sign) {
        List<ProfileShow.Property> props = new LinkedList<>();
        String upValue = getByAllowSC(profile.getSkinUpAllow(), profile.getCapeUpAllow());
        if (!StrUtil.isEmpty(upValue)) {
            props.add(new ProfileShow.Property()
                    .setName("uploadableTextures")
                    .setValue(upValue)
                    .setSignature(sign ? signature(upValue) : null));
        }
        if (!(profile.getSkinHash() == null && profile.getCapeHash() == null)) {
            TexturesShow texturesShow = new TexturesShow()
                    .setTimestamp(System.currentTimeMillis())
                    .setProfileId(profile.getId())
                    .setProfileName(profile.getName())
                    .setSkin(
                            profile.getSkinHash() == null ? null :
                                    new TexturesShow.Texture()
                                            .setUrl(getTextureURL(profile.getSkinHash()))
                                            .setMetadata(new TexturesShow.Metadata().setModel(profile.getSkinSlim() ? "slim" : ""))
                    )
                    .setCape(
                            profile.getCapeHash() == null ? null :
                                    new TexturesShow.Texture()
                                            .setUrl(getTextureURL(profile.getCapeHash()))
                    );
            ObjectMapper mapper = new ObjectMapper();
            String textures;
            try {
                textures = base64Str(mapper.writeValueAsString(texturesShow));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            ProfileShow.Property tx = new ProfileShow.Property()
                    .setName("textures")
                    .setValue(textures)
                    .setSignature(sign ? signature(textures) : null);
            props.add(tx);
        }
        return new ProfileShow()
                .setId(profile.getId())
                .setName(profile.getName())
                .setProperties(props);
    }
    public List<ProfileShow> profileToShow(List<Profile> profiles, boolean sign) {
        return profiles.stream().map(profile -> profileToShow(profile, sign)).toList();
    }
    public static String getByAllowSC(boolean skin, boolean cape) {
        if (skin && !cape) {
            return "skin";
        }
        if (cape && !skin) {
            return "cape";
        }
        if (cape) { // ==>  cape && skin, since ALL TRUE or ALL FALSE
            return "skin,cape";
        }
        return "";
    }
}