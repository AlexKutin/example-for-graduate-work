package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.FileService;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AdsController {
    private final Logger logger = LoggerFactory.getLogger(AdsController.class);

    private final AdService adService;
    private final FileService fileService;

    @GetMapping("/ads")
    public ResponseEntity<AdsDTO> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @PostMapping(value = "/ads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDTO> addAd(@Valid @RequestPart("properties") CreateOrUpdateAdDTO createAdDTO,
                                       @RequestPart("image") MultipartFile multipartFile,
                                       Authentication authentication) throws IOException {
        AdDTO adDTO = adService.addAd(createAdDTO, multipartFile, authentication);
        return ResponseEntity.ok(adDTO);
    }

    @GetMapping("/ads/{id}")
    public ResponseEntity<ExtendedAdDTO> extendedAd(@PathVariable(name = "id") int adId) {
        return ResponseEntity.ok(adService.getExtendedAdInfo(adId));
    }

    @DeleteMapping("/ads/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable(name = "id") int id, Authentication authentication) throws IOException {
        adService.removeAd(id, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/ads/{id}")
    public ResponseEntity<AdDTO> updateAds(@PathVariable(name = "id") int adId,
                                           @Valid @RequestBody CreateOrUpdateAdDTO updateAdDTO,
                                           Authentication authentication) {
        return ResponseEntity.ok(adService.updateAd(adId, updateAdDTO, authentication));
    }

    @GetMapping("/ads/me")
    public ResponseEntity<AdsDTO> getAdsMe(Authentication authentication) {
        return ResponseEntity.ok(adService.getAllAdsByMe(authentication));
    }

    @PatchMapping(value = "/ads/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateImage(@PathVariable(name = "id") int adId,
                                            @RequestPart("image") MultipartFile multipartFile,
                                            Authentication authentication) throws IOException {
        Ad ad = adService.getAdById(adId);
        fileService.updateAdPhoto(ad, multipartFile, authentication);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler
    ResponseEntity<?> handleAdNotFound(AdNotFoundException e) {
        logger.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

}
