package ru.skypro.homework.service;

import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.AdUser;

import java.util.List;
import java.util.stream.Collectors;

public class AdMapper {

    public static AdDTO toAdDTO(Ad ad) {
        AdDTO adDTO = new AdDTO();

        adDTO.setPk(ad.getAdId());
        adDTO.setAuthor(ad.getAuthor().getUserId());
        adDTO.setTitle(ad.getTitle());
        adDTO.setPrice(adDTO.getPrice());
        adDTO.setImage(ad.getImage());

        return adDTO;
    }

    public static Ad fromDTO(CreateOrUpdateAdDTO createOrUpdateAdDTO, AdUser authorAd) {
        Ad ad = new Ad();

        ad.setAuthor(authorAd);
        ad.setTitle(createOrUpdateAdDTO.getTitle());
        ad.setDescription(createOrUpdateAdDTO.getDescription());
        ad.setPrice(createOrUpdateAdDTO.getPrice());

        return ad;
    }

    public static ExtendedAdDTO toExtendedAdDTO(Ad ad) {
        ExtendedAdDTO extendedAdDTO = new ExtendedAdDTO();

        extendedAdDTO.setPk(ad.getAdId());
        extendedAdDTO.setAuthorFirstName(ad.getAuthor().getFirstName());
        extendedAdDTO.setAuthorLastName(ad.getAuthor().getLastName());
        extendedAdDTO.setTitle(ad.getTitle());
        extendedAdDTO.setDescription(ad.getDescription());
        extendedAdDTO.setEmail(ad.getAuthor().getUsername());
        extendedAdDTO.setImage(ad.getImage());
        extendedAdDTO.setPhone(ad.getAuthor().getPhone());
        extendedAdDTO.setPrice(ad.getPrice());

        return extendedAdDTO;
    }


    public static AdsDTO toAdsDTO(List<Ad> adList) {
        AdsDTO adsDTO = new AdsDTO();
        adsDTO.setCount(adList.size());
        adsDTO.setResults(adList.stream().map(AdMapper::toAdDTO).collect(Collectors.toList()));

        return adsDTO;
    }
}
