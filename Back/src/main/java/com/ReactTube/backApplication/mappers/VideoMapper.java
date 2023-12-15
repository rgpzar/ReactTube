package com.ReactTube.backApplication.mappers;

import com.ReactTube.backApplication.dto.UserInputDto;
import com.ReactTube.backApplication.dto.VideoInputDto;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.models.Video;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VideoMapper {
        VideoMapper INSTANCE = Mappers.getMapper( VideoMapper.class );

        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateVideoFromVideoInputDto(VideoInputDto videoInputDto, @MappingTarget Video video);
}
