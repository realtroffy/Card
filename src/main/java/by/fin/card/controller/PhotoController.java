package by.fin.card.controller;

import by.fin.card.dto.PhotoDto;
import by.fin.card.dto.PhotoPageDto;
import by.fin.card.dto.PhotoWithoutData;
import by.fin.card.service.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/photos")
public class PhotoController {

  private final PhotoService photoService;

  @GetMapping
  public ResponseEntity<PhotoPageDto> getAllPhotos() {
    List<PhotoDto> photoDtoList = photoService.getAllPhoto();
    PhotoPageDto photoPageDto = new PhotoPageDto(photoDtoList);
    return ResponseEntity.ok(photoPageDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PhotoDto> getPhotoById(@PathVariable Long id) {
    return ResponseEntity.ok(photoService.getPhotoById(id));
  }

  @PostMapping
  public ResponseEntity<PhotoPageDto> uploadPhotos(@RequestParam List<MultipartFile> files) {
    List<PhotoDto> photoDtoList = photoService.uploadPhotos(files);
    PhotoPageDto photoPageDto = new PhotoPageDto(photoDtoList);
    return ResponseEntity.ok(photoPageDto);
  }

  @PostMapping("/edit/resources")
  public ResponseEntity<PhotoPageDto> uploadPhotoFromResources() {
    List<PhotoDto> photoDtoList = photoService.uploadPhotosFromResources();
    PhotoPageDto photoPageDto = new PhotoPageDto(photoDtoList);
    return ResponseEntity.ok(photoPageDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PhotoDto> updatePhotoName(
      @PathVariable Long id, @RequestBody PhotoWithoutData photoWithoutData) {
    return ResponseEntity.ok(photoService.updatePhotoName(id, photoWithoutData));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<PhotoDto> deletePhotoById(@PathVariable Long id) {
    return ResponseEntity.ok(photoService.deletePhoto(id));
  }
}
