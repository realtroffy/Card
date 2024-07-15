package by.fin.card.service.impl;

import by.fin.card.dto.PhotoDto;
import by.fin.card.dto.PhotoWithoutData;
import by.fin.card.exception.CollectFilesFromDirectoryException;
import by.fin.card.exception.InvalidFileExtensionException;
import by.fin.card.exception.ReadByteException;
import by.fin.card.mapper.PhotoMapper;
import by.fin.card.model.Photo;
import by.fin.card.repository.PhotoRepository;
import by.fin.card.service.PhotoEditingService;
import by.fin.card.service.PhotoService;
import com.google.common.io.Files;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class PhotoServiceImpl implements PhotoService {

  public static final String NO_SUCH_PHOTO_EXCEPTION_MESSAGE = "Photo was not found by id = ";
  public static final String READ_BYTE_EXCEPTION_MESSAGE = "Can't read byte from file";
  public static final String INVALID_FILE_EXTENSION_EXCEPTION_MESSAGE =
      "Please upload file with valid extension - jpg, jpeg, png";
  public static final String COLLECT_FILE_FROM_DIRECTORY_EXCEPTION_MESSAGE =
      "Exception while collecting files from directory";
  public static final String PHOTO_PATH_DIRECTORY_BEFORE_EDITING =
      "src/main/resources/beforeEditing/";
  public static final String PHOTO_PATH_AFTER_EDITING = "src/main/resources/afterEditing/";
  private static final List<String> VALID_EXTENSIONS = List.of("jpg", "jpeg", "png");

  private final PhotoRepository photoRepository;
  private final PhotoEditingService photoEditingService;
  private final PhotoMapper photoMapper;

  @Transactional(readOnly = true)
  @Override
  public List<PhotoDto> getAllPhoto() {
    List<PhotoDto> photoDtoList = new ArrayList<>();
    photoRepository.findAll().forEach(photo -> photoDtoList.add(photoMapper.toDto(photo)));
    return photoDtoList;
  }

  @Override
  @Transactional(readOnly = true)
  public PhotoDto getPhotoById(Long id) {
    return photoMapper.toDto(getPhoto(id));
  }

  @Override
  public Photo getPhoto(Long id) {
    return photoRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException(NO_SUCH_PHOTO_EXCEPTION_MESSAGE + id));
  }

  @Override
  @Transactional
  public List<PhotoDto> uploadPhotos(List<MultipartFile> files) {
    List<PhotoDto> photoDtoList = new ArrayList<>();
    for (MultipartFile file : files) {
      if (!isValidExtension(file)) {
        log.error(INVALID_FILE_EXTENSION_EXCEPTION_MESSAGE + ": {}", file.getName());
        throw new InvalidFileExtensionException(INVALID_FILE_EXTENSION_EXCEPTION_MESSAGE);
      }
      Photo photo = new Photo();
      photo.setName(file.getOriginalFilename());
      try {
        byte[] photoDataAfterRemovingBackGround =
            photoEditingService.removeBackground(file.getBytes());
        photo.setData(photoDataAfterRemovingBackGround);
        Files.write(
            photoDataAfterRemovingBackGround,
            new File(PHOTO_PATH_AFTER_EDITING + file.getOriginalFilename()));
      } catch (IOException e) {
        log.error(READ_BYTE_EXCEPTION_MESSAGE);
        throw new ReadByteException(READ_BYTE_EXCEPTION_MESSAGE);
      }
      photoDtoList.add(photoMapper.toDto(photoRepository.save(photo)));
    }
    return photoDtoList;
  }

  @Override
  @Transactional
  public List<PhotoDto> uploadPhotosFromResources() {
    Set<File> files = getFilesFromDirectory();
    List<PhotoDto> photoDtoList = new ArrayList<>();
    for (File file : files) {
      if (!isValidExtension(file)) {
        log.error(INVALID_FILE_EXTENSION_EXCEPTION_MESSAGE + ": {}", file.getName());
        throw new InvalidFileExtensionException(INVALID_FILE_EXTENSION_EXCEPTION_MESSAGE);
      }
      Photo photo = new Photo();
      photo.setName(file.getName());
      try {
        byte[] photoDataAfterRemovingBackGround =
            photoEditingService.removeBackground(Files.toByteArray(file));
        photo.setData(photoDataAfterRemovingBackGround);
        Files.write(
            photoDataAfterRemovingBackGround, new File(PHOTO_PATH_AFTER_EDITING + file.getName()));
      } catch (IOException e) {
        log.error(READ_BYTE_EXCEPTION_MESSAGE);
        throw new ReadByteException(READ_BYTE_EXCEPTION_MESSAGE);
      }
      photoDtoList.add(photoMapper.toDto(photoRepository.save(photo)));
    }
    return photoDtoList;
  }

  private boolean isValidExtension(File file) {
    String uploadFileExtension = getExtensionByGuava(file.getName());
    for (String validExtension : VALID_EXTENSIONS) {
      if (uploadFileExtension.equals(validExtension)) {
        return true;
      }
    }
    return false;
  }

  private boolean isValidExtension(MultipartFile file) {
    String uploadFileExtension = getExtensionByGuava(file.getOriginalFilename());
    for (String validExtension : VALID_EXTENSIONS) {
      if (uploadFileExtension.equals(validExtension)) {
        return true;
      }
    }
    return false;
  }

  private String getExtensionByGuava(String filename) {
    return Files.getFileExtension(filename);
  }

  @Override
  @Transactional
  public PhotoDto updatePhotoName(Long id, PhotoWithoutData photoWithoutData) {
    Photo photo = getPhoto(id);
    photo.setName(photoWithoutData.getName());
    Photo savedPhoto = photoRepository.save(photo);
    return photoMapper.toDto(savedPhoto);
  }

  @Override
  @Transactional
  public PhotoDto deletePhoto(Long id) {
    Photo photo = getPhoto(id);
    photoRepository.deleteById(id);
    return photoMapper.toDto(photo);
  }

  private Set<File> getFilesFromDirectory() {
    Set<File> files;
    try (Stream<Path> stream =
        java.nio.file.Files.list(Paths.get(PhotoServiceImpl.PHOTO_PATH_DIRECTORY_BEFORE_EDITING))) {
      files =
          stream
              .filter(path -> !java.nio.file.Files.isDirectory(path))
              .map(Path::toFile)
              .collect(Collectors.toSet());
    } catch (IOException exception) {
      log.error(
          COLLECT_FILE_FROM_DIRECTORY_EXCEPTION_MESSAGE + ": {}",
          PHOTO_PATH_DIRECTORY_BEFORE_EDITING);
      throw new CollectFilesFromDirectoryException(COLLECT_FILE_FROM_DIRECTORY_EXCEPTION_MESSAGE);
    }
    return files;
  }
}
