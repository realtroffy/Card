package by.fin.card.mapper;

import by.fin.card.dto.PhotoDto;
import by.fin.card.model.Photo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

  PhotoDto toDto(Photo Photo);
}
