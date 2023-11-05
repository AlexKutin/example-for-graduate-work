package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;

import java.io.IOException;

public interface AdService {
    Ad getAdById(Integer adId);

    boolean isAdPresent(Integer adId);

    AdsDTO getAllAdsByMe(Authentication authentication);

    AdsDTO getAllAds();

    ExtendedAdDTO getExtendedAdInfo(Integer adId);

    void removeAd(Integer adId, Authentication authentication) throws IOException;

    AdDTO addAd(CreateOrUpdateAdDTO createAdDTO, MultipartFile multipartFile, Authentication authentication) throws IOException;

    AdDTO updateAd(Integer adId, CreateOrUpdateAdDTO updateAdDTO, Authentication authentication);
}
