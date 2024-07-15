package by.fin.card.service;

import by.fin.card.dto.PhotoDto;
import by.fin.card.dto.PhotoWithoutData;
import by.fin.card.model.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {

  List<PhotoDto> getAllPhoto();

  PhotoDto getPhotoById(Long id);

  Photo getPhoto(Long id);

  List<PhotoDto> uploadPhotos(List<MultipartFile> files);

  List<PhotoDto> uploadPhotosFromResources();

  PhotoDto updatePhotoName(Long id, PhotoWithoutData photoWithoutData);

  PhotoDto deletePhoto(Long id);
}
