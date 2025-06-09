package com.example.main_server.user;


import com.example.main_server.common.entity.Department;
import com.example.main_server.common.entity.Division;
import com.example.main_server.common.entity.Organization;
import com.example.main_server.common.entity.User;
import com.example.main_server.common.repository.DepartmentRepository;
import com.example.main_server.common.repository.DivisionRepository;
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
    private final DepartmentRepository departmentRepository;
    private final DivisionRepository divisionRepository;
    private final OrganizationRepository organizationRepository;
//    private final PasswordEncoder passwordEncoder;

    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.findByEmployeeNumber(request.getEmployeeNumber()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 사번입니다.");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("부문 없음"));
        Division division = divisionRepository.findById(request.getDivisionId())
                .orElseThrow(() -> new IllegalArgumentException("본부 없음"));
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new IllegalArgumentException("조직 없음"));

        User user = new User();
        user.setName(request.getName());
        user.setTeamsEmail(request.getTeamsEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmployeeNumber(request.getEmployeeNumber());
        user.setPrimaryEmail(request.getPrimaryEmail());
        user.setSlackEmail(request.getSlackEmail());
        user.setCareerLevel(request.getCareerLevel());
        user.setIsManager(request.getIsManager());
        user.setDepartment(department);
        user.setDivision(division);
        user.setOrganization(organization);

        userRepository.save(user);
        return new UserResponse(user);
    }
}
