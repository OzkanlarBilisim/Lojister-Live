package com.lojister.business.concretes;


import com.lojister.core.i18n.Translator;
import com.lojister.model.entity.CustomUserDetails;
import com.lojister.model.entity.User;
import com.lojister.repository.account.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByPhone(phone);

        if (user.isPresent()) {
            return new CustomUserDetails(user.get());
        } else {
            //todo buraya bak.
            throw new UsernameNotFoundException(Translator.toLocale("lojister.userDetails.UsernameNotFoundException"));
        }
    }
}
