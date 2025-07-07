package com.example.main_server.auth.user;


import com.example.main_server.auth.user.dto.UserRegisterRequest;
import com.example.main_server.auth.user.dto.UserResponse;
import com.example.main_server.auth.user.entity.User;
import com.example.main_server.common.entity.Department;
import com.example.main_server.common.entity.Division;
import com.example.main_server.common.entity.Organization;
import com.example.main_server.common.repository.DepartmentRepository;
import com.example.main_server.common.repository.DivisionRepository;
import com.example.main_server.common.repository.OrganizationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final DivisionRepository divisionRepository;

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

        // 조직 존재 여부 확인
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 조직입니다."));

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서입니다."));

        Division division = divisionRepository.findById(request.divisionId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 본부입니다."));

        User user = createUser(request, organization, department, division);

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    private User createUser(UserRegisterRequest request, Organization organization, Department department,
                            Division division) {
        User user = new User();
        user.setName(request.name());
        user.setEmployeeNumber(request.employeeNumber());
        user.setPassword(request.password());
        user.setTeamsEmail(request.teamsEmail());
        user.setSlackEmail(request.slackEmail());
        user.setLocalPath(request.localPath());
        user.setDepartment(department);
        user.setDivision(division);
        user.setOrganization(organization);
        user.setIsManager(request.isManager());
        user.setCareerLevel(request.careerLevel());
        // 비밀번호 암호화
        // user.setPassword(passwordEncoder.encode(request.password()));

        return user;
    }

    public User login(String employeeNumber, String password) {
        return userRepository.findByEmployeeNumber(employeeNumber)
                .map(user -> {
                    if (user.getPassword() == null || !user.getPassword().equals(password)) {
                        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
                    }
                    return user;
                })
                .orElseThrow(() -> new IllegalArgumentException("등록된 사번이 없습니다."));
    }

    public List<User> getOrganizationMember(Long organizationId) {
        return userRepository.findAllByOrganizationId(organizationId);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + id));
    }
}
