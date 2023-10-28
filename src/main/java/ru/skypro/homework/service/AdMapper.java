package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdMapper {

    public AdDTO toAdDTO(Ad ad) {
        AdDTO adDTO = new AdDTO();

        adDTO.setPk(ad.getAdId());
        adDTO.setAuthor(ad.getAuthor().getUserId());
        adDTO.setTitle(ad.getTitle());
        adDTO.setPrice(ad.getPrice());
        adDTO.setImage(ad.getImage());

        return adDTO;
    }

    public ExtendedAdDTO toExtendedAdDTO(Ad ad) {
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

    public AdsDTO toAdsDTO(List<Ad> adList) {
        AdsDTO adsDTO = new AdsDTO();
        adsDTO.setCount(adList.size());
        adsDTO.setResults(adList.stream().map(this::toAdDTO).collect(Collectors.toList()));

        return adsDTO;
    }
}
