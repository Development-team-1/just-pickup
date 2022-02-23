package com.justpickup.userservice.domain.user.dto;

import com.justpickup.userservice.domain.user.entity.AuthType;
import com.justpickup.userservice.domain.user.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthAttributeDto {
    private Map<String, Object> attributes; // OAuth2 반환하는 유저정보 MAP
    private String nameAttributeKey;
    private String name;
    private String email;
    private AuthType authType;


    public OAuthAttributeDto(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    public static OAuthAttributeDto of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        // 여기서 네이버와 카카오 등 구분 (ofNaver, ofKakao)
        if("naver".equals(registrationId))
            return ofNaver(userNameAttributeName , attributes);

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributeDto ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttributeDto.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .nameAttributeKey("id")
                .attributes(response)
                .authType(AuthType.NAVER)
                .build();
    }


    private static OAuthAttributeDto ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributeDto.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .authType(AuthType.GOOGLE)
                .build();
    }

    public Customer toEntity(OAuthAttributeDto attributeDto){
        return new Customer(email,"temp",name,null, attributeDto.getAuthType());
    }


}
