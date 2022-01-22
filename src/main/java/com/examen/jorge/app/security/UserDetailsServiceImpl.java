package com.examen.jorge.app.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.examen.jorge.backend.data.entity.User;
import com.examen.jorge.backend.repositories.UserRepository;

/**
 * Implements the {@link UserDetailsService}.
 * 
 * This implementation searches for {@link User} entities by the e-mail address
 * supplied in the login screen.
 */
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 *
	 * Recovers the {@link User} from the database using the e-mail address supplied
	 * in the login screen. If the user is found, returns a
	 * {@link org.springframework.security.core.userdetails.User}.
	 *
	 * @param username User's e-mail address
	 * 
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (null == user) {
			throw new UsernameNotFoundException("Ningún usuario presente con nombre de usuario: " + username);
		} else {
			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getHashedPassword(),
					getAuthorities(user));
		}
	}
	
	private static List<GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

    }
}