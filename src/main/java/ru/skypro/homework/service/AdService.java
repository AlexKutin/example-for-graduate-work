package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;

public interface AdService {
    Ad getAdById(Integer adId);

    AdsDTO getAllAdsByMe(Authentication authentication);

    AdsDTO getAllAds();

    ExtendedAdDTO getExtendedAdInfo(Integer adId);

    void removeAd(Integer adId);

    AdDTO updateAd(Integer adId, CreateOrUpdateAdDTO updateAdDTO);

    AdDTO addAd(CreateOrUpdateAdDTO createAdDTO, Authentication authentication);
}
