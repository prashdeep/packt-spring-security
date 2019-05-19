    package com.classpath.userregistration.controller;

    import com.classpath.userregistration.model.User;
    import com.classpath.userregistration.service.EmailService;
    import com.classpath.userregistration.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Controller;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.ModelAttribute;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;
    import javax.servlet.http.HttpServletRequest;
    import javax.validation.Valid;
    import java.time.LocalDateTime;
    import java.time.temporal.ChronoUnit;
    import java.util.Optional;
    import java.util.UUID;

    @Controller
    public class UserRegistrationController {

        private UserService userService;

        @Autowired
        private EmailService emailService;

        @Value("${app.token.expiry.time}")
        private long confirmationTokenExpiryTime;

        @Autowired
        public UserRegistrationController(UserService userService){
            this.userService = userService;
        }

        @GetMapping("/register")
        public ModelAndView displayRegistrationPage(ModelAndView modelAndView,
                                                    User user, HttpServletRequest request){
            modelAndView.addObject(user);
            modelAndView.setViewName("register");
            return modelAndView;
        }

        @PostMapping("/register")
        public ModelAndView registerUser(ModelAndView modelAndView,
                                           @Valid User user,
                                             BindingResult bindingResult,
                                             HttpServletRequest request,
                                             RedirectAttributes redir){
            if (bindingResult.hasErrors()){
                modelAndView.setViewName("register");
                return modelAndView;
            }
            Optional<User> returnedUser = this.userService.
                    findByEmailAddress(user.getEmail());
            if(returnedUser.isPresent()){
                modelAndView.addObject("userExists",
                        "User already exists");
                modelAndView.setViewName("register");
                return modelAndView;
            }
            userService.saveUser(user);

            String url = request.getScheme() + "://" + request.getServerName()+"/confirm?token=" + user.getToken();
            String message = "To confirm your e-mail address, please click the link below:\n"
                    + url;

            this.emailService.sendConfirmationTokenEmail(user.getEmail(), message);
            redir.addFlashAttribute("confirmMessage",
                    "User is successfully registered. " +
                            "Please follow the instructions sent to your email - (" +user.getEmail()+ ") for activation.");

            modelAndView.setViewName("invalid-token");
            return modelAndView;
        }
        @GetMapping("/registration-success")
        public ModelAndView displayRegistrationSuccess(@ModelAttribute("confirmMessage")String message,  ModelAndView modelAndView) {
            modelAndView.setViewName("registration-success");
            return modelAndView;
        }

        @GetMapping("/confirm")
        public ModelAndView confirmUser(ModelAndView modelAndView,
                                        @RequestParam("token") String token,
                                        RedirectAttributes redir){
            // Find the user associated with the reset token
            Optional<User> optionalUser= userService.findByConfirmationToken(token);
            if (!optionalUser.isPresent()) {
                //redirect user to reset password page.
                modelAndView.setViewName("invalid-token");
                redir.addFlashAttribute("errorMessage", "Invalid Token");
                return new ModelAndView("redirect:invalid-token");
            }
            User user = optionalUser.get();
            //check if the token generation time exceeds configured timed
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime userRegisteredTime = user.getTokenCreatedTime();
            long minutes = ChronoUnit.MINUTES.between(userRegisteredTime, currentTime);
            if (minutes > confirmationTokenExpiryTime) {
                //redirect to reset password page.
                modelAndView.setViewName("invalid-token");
                redir.addFlashAttribute("errorMessage", "Token expired");
                //redirect to forgot password
                return new ModelAndView("redirect:invalid-token");
            }
            // Set user to enabled
            user.setEnabled(true);
            // Save user
            userService.saveUser(user);
            redir.addFlashAttribute("confirmMessage",
                    "You are now successfully registered for the application.");
            modelAndView.setViewName("redirect:/registration-success");
            return modelAndView;
        }

        @GetMapping("/invalid-token")
        public ModelAndView displayInvalidToken(ModelAndView modelAndView){
            modelAndView.setViewName("invalid-token");
            return modelAndView;
        }

        @PostMapping("/invalid-token")
        public ModelAndView handlePasswordResetLink(ModelAndView modelAndView,
                                                    @ModelAttribute("email") String emailAddress,
                                                    HttpServletRequest request){
            Optional<User> optional = this.userService.findByEmailAddress(emailAddress);
            if(!optional.isPresent()){
                modelAndView.addObject("errorMessage",
                        "Invalid Email address");

                modelAndView.setViewName("invalid-token");
                return modelAndView;
            };
            User user = optional.get();
            String confirmationToken = UUID.randomUUID().toString();
            LocalDateTime currentTime = LocalDateTime.now();
            user.setEnabled(false);
            user.setToken(confirmationToken);
            user.setTokenCreatedTime(currentTime);
            this.userService.saveUser(user);
            String url = request.getScheme() + "://" + request.getServerName()+"/confirm?token=" + user.getToken();
            String message = "To confirm your e-mail address, please click the link below:\n"
                    + url;
            this.emailService.sendConfirmationTokenEmail(user.getEmail(), message);
            modelAndView.setViewName("redirect:/registration-success");
            modelAndView.addObject("confirmMessage",
                    "An email is send to your registered email address with the instructions to reset the password.");
            return modelAndView;
        }
    }