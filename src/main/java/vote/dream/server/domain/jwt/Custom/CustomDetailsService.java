package vote.dream.server.domain.jwt.Custom;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vote.dream.server.domain.user.entity.User;
import vote.dream.server.domain.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // user은 loginId로 찾고, password는 jwtUtil에서 검증
    @Override
    public UserDetails loadUserByUsername(String loginIn) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByLoginId(loginIn);

        if (user.isPresent()) {
            User newUser = user.get();

            return new CustomDetails(newUser);
        }
        throw new  UsernameNotFoundException("User not found with loginId: " + loginIn);
    }
}
