package dev.be.core.api.controller;

import com.dev.be.core.enums.CurrencyCode;
import dev.be.core.api.support.error.CoreException;
import dev.be.core.api.support.error.ErrorType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class HelloController implements HelloControllerDocs {

    @Override
    public ResponseEntity<String> get(@PathVariable("id")final Long id) {
        String result = "조회 결과: ID = " + id;
        return ResponseEntity.ok(result);

    }

    @Override
    public ResponseEntity<List<String>> getAllCurrencyCodes() {
        List<String> codes = Arrays.stream(CurrencyCode.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(codes);
    }

    @Override
    public ResponseEntity<?> error() {
        throw new CoreException(ErrorType.DEFAULT_ERROR, "에러 테스트용 데이터");
    }
}
