package com.example.main_server.user;


import com.example.main_server.common.entity.Organization;
import com.example.main_server.common.entity.User;
import com.example.main_server.common.repository.OrganizationRepository;
import com.example.main_server.common.repository.UserRepository;
import com.example.main_server.user.dto.UserRegisterRequest;
import com.example.main_server.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
//    private final PasswordEncoder passwordEncoder;

    public UserResponse register(UserRegisterRequest request) {
        // 사번 중복 검사
        if (userRepository.findByEmployeeNumber(request.employeeNumber()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 사번입니다.");
        }

        // Teams 이메일 중복 검사
        if (userRepository.findByTeamsEmail(request.teamsEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 Teams 이메일입니다.");
        }

        // Primary 이메일 중복 검사 (null이 아닌 경우만)
        if (request.primaryEmail() != null &&
                userRepository.findByPrimaryEmail(request.primaryEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 Primary 이메일입니다.");
        }

        // 조직 존재 여부 확인
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 조직입니다."));

        // 사용자 생성
        User user = createUser(request, organization);

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    private User createUser(UserRegisterRequest request, Organization organization) {
        User user = new User();
        user.setName(request.name());
        user.setTeamsEmail(request.teamsEmail());
        user.setEmployeeNumber(request.employeeNumber());
        user.setPrimaryEmail(request.primaryEmail());
        user.setSlackEmail(request.slackEmail());
        user.setCareerLevel(request.careerLevel());
        user.setIsManager(request.isManager());
        user.setOrganization(organization);

        // 비밀번호 암호화
        // user.setPassword(passwordEncoder.encode(request.password()));

        return user;
    }

}
