package org.miowing.mioverify.controller;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.miowing.mioverify.annotation.AuthorizedCheck;
import org.miowing.mioverify.exception.AttackDefenseException;
import org.miowing.mioverify.exception.ForbiddenUploadException;
import org.miowing.mioverify.exception.ProfileMismatchException;
import org.miowing.mioverify.exception.TextureDeleteException;
import org.miowing.mioverify.pojo.Profile;
import org.miowing.mioverify.pojo.ProfileShow;
import org.miowing.mioverify.pojo.ServerMeta;
import org.miowing.mioverify.service.ProfileService;
import org.miowing.mioverify.util.DataUtil;
import org.miowing.mioverify.util.StorageUtil;
import org.miowing.mioverify.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class ApiController {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private Util util;
    @Autowired
    private DataUtil dataUtil;
    @Autowired
    private StorageUtil storageUtil;
    @PostMapping("/api/profiles/minecraft")
    public List<ProfileShow> profiles(@RequestBody String[] names) {
        if (names.length > dataUtil.getProfileBatchLimit()) {
            throw new AttackDefenseException();
        }
        return util.profileToShow(profileService.getByNames(names), true);
    }
    @AuthorizedCheck
    @PutMapping("/api/user/profile/{uuid}/{textureType}")
    public ResponseEntity<?> uploadTexture(
            @PathVariable String uuid,
            @PathVariable String textureType,
            @RequestParam(defaultValue = "") String model,
            MultipartFile file
    ) throws IOException {
        if (!MediaType.IMAGE_PNG_VALUE.equals(file.getContentType())) {
            throw new ForbiddenUploadException();
        }
        Profile profile = profileService.getById(uuid);
        if (profile == null) {
            throw new ProfileMismatchException();
        }
        if (!"png".equals(FileTypeUtil.getType(file.getInputStream()))) {
            throw new ForbiddenUploadException();
        }
        byte[] c = file.getBytes();
        String sha = DigestUtil.sha256Hex(c);
        switch (textureType) {
            case "skin" -> {
                if (!profile.getSkinUpAllow()) {
                    throw new ForbiddenUploadException();
                }
                profile.setSkinHash(sha);
                profile.setSkinSlim("slim".equals(model));
                profileService.update(profile, null);
                storageUtil.saveTexture(true, c, sha);
            }
            case "cape" -> {
                if (!profile.getCapeUpAllow()) {
                    throw new ForbiddenUploadException();
                }
                profile.setCapeHash(sha);
                profileService.update(profile, null);
                storageUtil.saveTexture(false, c, sha);
            }
            default -> throw new ForbiddenUploadException();
        }
        log.info("New texture updated:");
        log.info(sha);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @AuthorizedCheck
    @DeleteMapping("/api/user/profile/{uuid}/{textureType}")
    public ResponseEntity<?> deleteTexture(
            @PathVariable String uuid,
            @PathVariable String textureType
    ) {
        Profile profile = profileService.getById(uuid);
        if (profile == null) {
            throw new ProfileMismatchException();
        }
        switch (textureType) {
            case "skin" -> {
                String sha = profile.getSkinHash();
                LambdaUpdateWrapper<Profile> luw = new LambdaUpdateWrapper<>();
                luw.set(Profile::getSkinHash, null).eq(Profile::getId, profile.getId());
                profileService.update(luw);
                if (sha != null && profileService.getBySkinHash(sha).isEmpty()) {
                    storageUtil.deleteTexture(true, sha);
                }
            }
            case "cape" -> {
                String sha = profile.getCapeHash();
                LambdaUpdateWrapper<Profile> luw = new LambdaUpdateWrapper<>();
                luw.set(Profile::getCapeHash, null).eq(Profile::getId, profile.getId());
                profileService.update(luw);
                if (sha != null && profileService.getByCapeHash(sha).isEmpty()) {
                    storageUtil.deleteTexture(false, sha);
                }
            }
            default -> throw new TextureDeleteException();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping
    public ServerMeta getServerMeta() {
        return util.getServerMeta();
    }
}