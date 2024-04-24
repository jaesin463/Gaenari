package com.gaenari.backend.domain.member.dto.requestDto;

import com.gaenari.backend.domain.member.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequestDto {
    @Email(message = "올바른 형식의 이메일 주소를 입력해 주십시오.")
    @NotEmpty(message = "이메일은 필수 정보입니다.")
    @Schema(description = "이메일", example = "user1@ssafy.com")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*\\d).{8,20}$",
            message = "비밀번호는 영문 대소문자, 숫자, 특수문자(!, @, #, $, %, ^, &, *)를 조합하여 3~20자 이내여야 합니다.")
    @Schema(description = "비밀번호", example = "Ssafy123!@")
    private String passsword;

    @NotEmpty(message = "비밀번호 확인란을 반드시 입력해 주셔야 합니다.")
    @Schema(description = "비밀번호 확인", example = "Ssafy123!@")
    private String passwordConfirm;

    @Pattern(regexp = "^[a-zA-Z0-9_]{3,10}$",
            message = "닉네임은 영문자, 숫자 및 언더바(_)를 포함할 수 있으며 3~10자 이내여야 합니다.")
    @Schema(description = "닉네임", example = "Gaenary_2024")
    private String nickName;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "생년월일 형식은 YYYY-MM-DD여야 합니다.")
    @Schema(description = "생년월일", example = "2024-02-24")
    private String birth;

    @NotEmpty(message = "성별을 반드시 입력해 주셔야 합니다.")
    @Schema(description = "성별", example = "Male")
    private Gender gender;

    @Schema(description = "신장", example = "160")
    private int height;

    @Schema(description = "체중", example = "60")
    private int weight;

    @NotEmpty(message = "반려견은 반드시 선택해 주셔야 합니다.")
    @Schema(description = "반려견 종류&이름", example = "id : 1, name : 초코")
    private MyPetDto myPet;
}