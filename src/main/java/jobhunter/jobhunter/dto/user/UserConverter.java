package jobhunter.jobhunter.dto.user;

import jobhunter.jobhunter.domain.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class UserConverter {
    @Autowired
    private ModelMapper modelMapper;
    public User toUser(UserRequest userRequest) {
//        User user = new User();
//        user.setUsername(userRequest.getUsername());
//        user.setPassword(userRequest.getPassword());
//        user.setEmail(userRequest.getEmail());
//        user.setAge(userRequest.getAge());
//        user.setGender(userRequest.getGender());
//        user.setAddress(userRequest.getAddress());
        return modelMapper.map(userRequest, User.class);
    }

    public UserResponse toUserResponse(User user) {
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return userResponse;
    }

}
