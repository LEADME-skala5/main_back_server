package com.sk.skala.skore.user;


import com.sk.skala.skore.user.dto.UserRegisterRequest;
import com.sk.skala.skore.user.dto.UserResponse;
import com.sk.skala.skore.user.entity.Department;
import com.sk.skala.skore.user.entity.Division;
import com.sk.skala.skore.user.entity.Organization;
import com.sk.skala.skore.user.entity.User;
import com.sk.skala.skore.user.exception.UserException;
import com.sk.skala.skore.user.exception.UserExceptionType;
import com.sk.skala.skore.user.repository.DepartmentRepository;
import com.sk.skala.skore.user.repository.DivisionRepository;
import com.sk.skala.skore.user.repository.OrganizationRepository;
import com.sk.skala.skore.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 관련 기능을 제공하는 서비스 클래스입니다.
 * <p>
 * 사용자 등록, 로그인, 조회 등의 기능을 제공하며, 사용자 정보를 관리합니다. 조직, 부서, 본부 정보와 사용자 정보를 연결하여 관리합니다.
 * <p>
 * 주요 기능: - 신규 사용자 등록 및 유효성 검증 - 사용자 로그인 처리 - 조직별 소속 구성원 조회 - ID로 특정 사용자 정보 조회
 */


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final DivisionRepository divisionRepository;

    public UserResponse register(UserRegisterRequest request) {
        // 사번 중복 검사
        if (userRepository.findByEmployeeNumber(request.employeeNumber()).isPresent()) {
            throw new UserException(UserExceptionType.DUPLICATE_EMPLOYEE_NUMBER);
        }

        // Teams 이메일 중복 검사
        if (userRepository.findByTeamsEmail(request.teamsEmail()).isPresent()) {
            throw new UserException(UserExceptionType.DUPLICATE_TEAMS_EMAIL);
        }

        // 조직 존재 여부 확인
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new UserException(UserExceptionType.ORGANIZATION_NOT_FOUND));

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new UserException(UserExceptionType.DEPARTMENT_NOT_FOUND));

        Division division = divisionRepository.findById(request.divisionId())
                .orElseThrow(() -> new UserException(UserExceptionType.DIVISION_NOT_FOUND));

        User user = createUser(request, organization, department, division);

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    private User createUser(UserRegisterRequest request, Organization organization, Department department,
                            Division division) {
        return User.createUser(
                request.name(),
                request.employeeNumber(),
                request.password(),
                request.teamsEmail(),
                request.slackEmail(),
                request.localPath(),
                organization,
                department,
                division,
                request.isManager(),
                request.careerLevel()
        );
    }

    public User login(String employeeNumber, String password) {
        return userRepository.findByEmployeeNumber(employeeNumber)
                .map(user -> {
                    if (user.getPassword() == null || !user.getPassword().equals(password)) {
                        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
                    }
                    return user;
                })
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }

    public List<User> getOrganizationMember(Long organizationId) {
        return userRepository.findAllByOrganizationId(organizationId);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }
}
