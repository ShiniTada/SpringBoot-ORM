package com.epam.esm.dto.list;


import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.jsonview.Views;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class GiftCertificateListDto {

	@JsonView(Views.WithoutTags.class)
	private List<GiftCertificateDto> giftCertificates;

	public GiftCertificateListDto(List<GiftCertificateDto> giftCertificates) {
		this.giftCertificates = giftCertificates;
	}

	public List<GiftCertificateDto> getGiftCertificates() {
		return giftCertificates;
	}

	public void setGiftCertificates(List<GiftCertificateDto> giftCertificates) {
		this.giftCertificates = giftCertificates;
	}

}
