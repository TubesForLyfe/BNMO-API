package bnmo.bnmoapi.api.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin
@RequestMapping("api/saldo-conversion")
public class SaldoConversion {

    @Value("${apilayer.url}")
    private String apilayer_url;

    @Value("${apilayer.apikey}")
    private String apikey;

    @GetMapping("/{to_IDR_currency}")
    @Cacheable("currency_conversion")
    public float convertSaldo(@PathVariable("to_IDR_currency") String to_IDR) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("apikey", apikey);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        String url = apilayer_url + "/exchangerates_data/convert?to=IDR&from=" + to_IDR + "&amount=1";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        try {
            JSONObject json = new JSONObject(response.getBody());
            return (float) json.getDouble("result");
        } catch (Exception e) {
            return 0;
        }
    }
}