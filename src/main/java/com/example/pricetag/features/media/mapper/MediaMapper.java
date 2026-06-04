package com.example.pricetag.features.media.mapper;

import com.example.pricetag.features.media.dto.response.MediaResponseDto;
import com.example.pricetag.features.media.entity.MediaAttachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MediaMapper {
    @Mapping(source = "mediaAsset.publicId",
            target = "publicId")
    @Mapping(source = "mediaAsset.url",
            target = "url")
    @Mapping(source = "mediaAsset.id",
            target = "mediaAssetId")
    @Mapping(source = "id",
            target = "attachmentId")
    @Mapping(source = "mediaAsset.altText",
            target = "altText")
    MediaResponseDto mapMediaAttachmentToMediaResponseDto(MediaAttachment mediaAttachment);
}
