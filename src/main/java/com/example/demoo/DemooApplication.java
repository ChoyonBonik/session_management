package com.example.demoo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demoo.repository.LoginRepository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@SpringBootApplication
public class DemooApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemooApplication.class, args);
	}

	
//	@Entity
//	@Table(name = "users")
//	public class UserEntity {
//
//	    @Id
//	    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	    private Long id;
//
//	    private String username;
//	    private String password;
//	    private boolean enabled;
//		public Long getId() {
//			return id;
//		}
//		public void setId(Long id) {
//			this.id = id;
//		}
//		public String getUsername() {
//			return username;
//		}
//		public void setUsername(String username) {
//			this.username = username;
//		}
//		public String getPassword() {
//			return password;
//		}
//		public void setPassword(String password) {
//			this.password = password;
//		}
//		public boolean isEnabled() {
//			return enabled;
//		}
//		public void setEnabled(boolean enabled) {
//			this.enabled = enabled;
//		}
//		public UserEntity(Long id, String username, String password, boolean enabled) {
//			super();
//			this.id = id;
//			this.username = username;
//			this.password = password;
//			this.enabled = enabled;
//		}
//	    
//	    
//
//	    // Getters and setters
//	}
//	
//	public interface UserRepository extends JpaRepository<UserEntity, Long> {
//
//	    UserEntity findByUsername(String username);
//	}
//
//	@Configuration
//	@EnableWebSecurity
//	public class SecurityConfig extends WebSecurityConfigurerAdapter {
//		
//		@Autowired
//	    private DataSource dataSource;
//
//	    @Override
//	    protected void configure(HttpSecurity http) throws Exception {
//	        http
//	            .authorizeRequests()
//	                .antMatchers("/public/**").permitAll()
//	                .anyRequest().authenticated()
//	                .and()
//	            .formLogin()
//	                .loginPage("/login")
//	                .permitAll()
//	                .and()
//	            .logout()
//	                .logoutUrl("/logout")
//	                .permitAll();
//	    }
//
//	    @Override
//	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	    	auth
//            .jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
//                .authoritiesByUsernameQuery("SELECT username, 'ROLE_USER' FROM users WHERE username = ?")
//                .passwordEncoder(passwordEncoder());
//	    }
//	    
//	    @Bean
//	    public BCryptPasswordEncoder passwordEncoder() {
//	        return new BCryptPasswordEncoder();
//	    }
//	}
//	@Configuration
//	public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
//
//	    @Bean
//	    public CookieSerializer cookieSerializer() {
//	        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//	        serializer.setCookieName("SESSIONID"); // Customize the cookie name
//	        serializer.setCookieMaxAge(60 * 60 * 24); // Set the session timeout in seconds
//	        return serializer;
//	    }
//	}
//	
//	@Service
//	public class UserService {
//
//	    private final UserRepository userRepository;
//	    private final PasswordEncoder passwordEncoder;
//
//	    @Autowired
//	    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//	        this.userRepository = userRepository;
//	        this.passwordEncoder = passwordEncoder;
//	    }
//
//	    public void saveUser(UserEntity user) {
//	        // Encrypt the password before saving
//	        user.setPassword(passwordEncoder.encode(user.getPassword()));
//	        userRepository.save(user);
//	    }
//	}
	

	  @Controller
	  public class MyController {
		  
		    @Autowired
		    private UserService userService;

	      @Autowired
	      private LoginRepository loginRepository;

	      @GetMapping("/showSessionId")
	      public String showSessionId(HttpServletRequest request, Model model) {
	          HttpSession session = request.getSession();
	         
	          String sessionId = session.getId();
	          

	          
	          //String username = "cbonik";
	          //String password = "1234";

	          saveSessionIdToDatabase(sessionId);

	          System.out.println("Session ID: " + sessionId);

	          model.addAttribute("sessionId", sessionId);
	          


	          return "index";
	      }
	      
	      @PostMapping("/login")
	      public String login(@RequestParam Long id, @RequestParam String username, @RequestParam String password, HttpServletRequest request) {
	          HttpSession session = request.getSession();
	          String sessionId = session.getId();
	          
	          
	          if (userService.validateLogin(id, username, password)) {
	              // Save login information to the database
	              saveLoginInformationToDatabase(id, username, password, sessionId);

	              // Redirect to the showSessionId page upon successful login
	              return "redirect:/showSessionId";
	          } else {
	              // Redirect to a login error page or show an error message
	              return "redirect:/loginError";
	          }


//	          saveLoginInformationToDatabase(username, password, sessionId);
//
//	          return "redirect:/showSessionId";
	      }

	      private void saveSessionIdToDatabase(String sessionId) {
	          LoginEntity loginEntity = new LoginEntity();
	          loginEntity.setSessionId(sessionId);

	          loginRepository.save(loginEntity);
	      }
	      
	      private void saveLoginInformationToDatabase(Long id, String username, String password, String sessionId) {
	          LoginEntity loginEntity = new LoginEntity();
	          loginEntity.setId(id);
	          loginEntity.setUsername(username);
	          loginEntity.setPassword(password);
	          loginEntity.setSessionId(sessionId);

	          loginRepository.save(loginEntity);
	          //sessionRepository.save(loginEntity);
	      }
	  }
	  
	  @Service
	  public class UserService {

	      @Autowired
	      private LoginRepository loginRepository;

	      public boolean validateLogin(Long id, String username, String password) {
	          // Retrieve user from the database based on the provided username
	          Optional<LoginEntity> optionalLoginEntity = loginRepository.findById(id);

	          // Check if the user exists
	          if (optionalLoginEntity.isPresent()) {
	              // Compare the provided password with the stored password
	              LoginEntity loginEntity = optionalLoginEntity.get();
	              if (loginEntity.getPassword().equals(password)) {
	                  // Passwords match, login successful
	                  return true;
	              }
	          }

	          // Username or password is incorrect
	          return false;
	      }
	  }
	
	
	
	

	
	
}
