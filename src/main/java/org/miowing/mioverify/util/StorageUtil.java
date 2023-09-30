package org.miowing.mioverify.util;

import cn.hutool.core.io.FileUtil;
import org.miowing.mioverify.exception.TextureNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * TODO Promote the extensive operations
 * Storage manager of textures (temporarily implementation).
 */
@Component
public class StorageUtil {
    @Autowired
    DataUtil dataUtil;
    public void saveTexture(boolean skin, byte[] c, String sha) {
        FileUtil.writeBytes(
                c,
                dataUtil.getTexturesPath()
                        .resolve(skin ? "skin" : "cape")
                        .resolve(sha)
                        .toFile()
        );
    }
    public boolean deleteTexture(boolean skin, String sha) {
        Path t = dataUtil.getTexturesPath().resolve(skin ? "skin" : "cape");
        File f = t.resolve(sha).toFile();
        System.out.println("start delete");
        if (f.exists()) {
            return f.delete();
        }
        return false;
    }
    public InputStream getTexture(boolean skin, String sha) throws TextureNotFoundException {
        Path t = dataUtil.getTexturesPath().resolve(skin ? "skin" : "cape");
        File f = t.resolve(sha).toFile();
        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            throw new TextureNotFoundException();
        }
    }
    public InputStream getTexture(String sha) {
        Path t = dataUtil.getTexturesPath().resolve("skin");
        File f = t.resolve(sha).toFile();
        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            t = t.resolveSibling("cape");
            f = t.resolve(sha).toFile();
            try {
                return new FileInputStream(f);
            } catch (FileNotFoundException e0) {
                throw new TextureNotFoundException();
            }
        }
    }
}