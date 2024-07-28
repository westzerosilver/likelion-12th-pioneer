package com.likelion12th.pioneer_2ne1.service;


import com.likelion12th.pioneer_2ne1.dto.GoogleResponse;
import com.likelion12th.pioneer_2ne1.dto.OAuth2Response;
import com.likelion12th.pioneer_2ne1.dto.Oauth2MemberDto;
import com.likelion12th.pioneer_2ne1.entity.Member;
import com.likelion12th.pioneer_2ne1.oauth2.CustomOAuth2User;
import com.likelion12th.pioneer_2ne1.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository userRepository;

    public CustomOAuth2UserService(MemberRepository userRepository) {

        this.userRepository = userRepository;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        Member existData = userRepository.findByEmail(username);

        if (existData == null) {

            Member userEntity = new Member();
            userEntity.setEmail(username);
//            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setName(oAuth2Response.getName());
            userEntity.setRole("ROLE_USER");

            userRepository.save(userEntity);

            Oauth2MemberDto userDTO = new Oauth2MemberDto();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
        else {

            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            userRepository.save(existData);

            Oauth2MemberDto userDTO = new Oauth2MemberDto();
            userDTO.setUsername(existData.getEmail());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);
        }
    }
}