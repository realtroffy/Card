package by.fin.card.service.impl;

import by.fin.card.service.PhotoEditingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PhotoEditingServiceImpl implements PhotoEditingService {

  public static final String REMOVE_BACK_GROUND_API_KEY_HEADER = "X-Api-Key";

  @Value("${removebackground.service.key}")
  private String removeBackGroundApiKey;

  @Value("${removebackground.service.url}")
  private String removeBackGroundUrl;

  @Value("${removebackground.service.request.body.type}")
  private String removeBackGroundBodyType;

  @Override
  public byte[] removeBackground(byte[] photoData) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add(REMOVE_BACK_GROUND_API_KEY_HEADER, removeBackGroundApiKey);
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add(removeBackGroundBodyType, Base64.getEncoder().encodeToString(photoData));
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
    ResponseEntity<byte[]> response =
        restTemplate.exchange(removeBackGroundUrl, HttpMethod.POST, request, byte[].class);
    return response.getBody();
  }
}
