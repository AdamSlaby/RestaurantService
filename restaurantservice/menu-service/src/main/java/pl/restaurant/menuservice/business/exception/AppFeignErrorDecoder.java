package pl.restaurant.menuservice.business.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.restaurant.menuservice.api.request.ExceptionMessage;

import java.io.IOException;
import java.io.InputStream;

@Component
public class AppFeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        ExceptionMessage message = null;
        try (InputStream stream = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(stream, ExceptionMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        if (HttpStatus.valueOf(response.status()).is4xxClientError()) {
            throw new SupplyErrorException(message.getMessage());
        }
        return defaultErrorDecoder.decode(s, response);
    }
}
