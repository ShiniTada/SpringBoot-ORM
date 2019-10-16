package com.epam.esm.dto.list;

import com.epam.esm.dto.TagDto;

import java.util.List;

public class TagListDto {

	private List<TagDto> tags;

	public TagListDto(List<TagDto> tags) {
		this.tags = tags;
	}

	public List<TagDto> getTags() {
		return tags;
	}

	public void setTags(List<TagDto> tags) {
		this.tags = tags;
	}
}
