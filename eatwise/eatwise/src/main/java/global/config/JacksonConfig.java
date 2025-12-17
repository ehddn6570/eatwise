// 이 파일은 WebConfig.java에 통합되었습니다. 사용하지 않습니다.
// package global.config;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.SerializationFeature;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// @Configuration
// public class JacksonConfig {
//
//     @Bean
//     public ObjectMapper objectMapper() {
//         ObjectMapper objectMapper = new ObjectMapper();
//
//         // Java 8 Date/Time 모듈 등록
//         objectMapper.registerModule(new JavaTimeModule());
//
//         // Timestamp를 숫자가 아닌 문자열로 직렬화
//         objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//         return objectMapper;
//     }
// }

