package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;

public interface AdService {
    Ad getAdById(Integer adId);

    boolean isAdPresent(Integer adId);

    AdsDTO getAllAdsByMe(Authentication authentication);

    AdsDTO getAllAds();

    ExtendedAdDTO getExtendedAdInfo(Integer adId);

    void removeAd(Integer adId, Authentication authentication);

    AdDTO addAd(CreateOrUpdateAdDTO createAdDTO, Authentication authentication);

    AdDTO updateAd(Integer adId, CreateOrUpdateAdDTO updateAdDTO, Authentication authentication);
}
