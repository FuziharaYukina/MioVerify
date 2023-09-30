package org.miowing.mioverify.controller;

import cn.hutool.core.io.IoUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.miowing.mioverify.util.DataUtil;
import org.miowing.mioverify.util.StorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/texture")
public class TextureController {
    @Autowired
    private DataUtil dataUtil;
    @Autowired
    private StorageUtil storageUtil;
    @GetMapping("/skin/default")
    public void defaultSkin(HttpServletResponse resp) throws IOException {
        File skin = dataUtil.getDefSkinLoc().toFile();
        resp.setContentType(MediaType.IMAGE_PNG_VALUE);
        IoUtil.copy(new FileInputStream(skin), resp.getOutputStream());
    }
    @GetMapping("/hash/{hash}")
    public void texture(@PathVariable String hash, HttpServletResponse resp) throws IOException {
        resp.setContentType(MediaType.IMAGE_PNG_VALUE);
        IoUtil.copy(storageUtil.getTexture(hash), resp.getOutputStream());
    }
}