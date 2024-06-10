package com.smart.controller;

import javax.validation.Valid;

import com.smart.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

import java.util.Random;


@Controller
public class HomeController {

	@Autowired
	EmailService emailService;


	@Autowired
	public PasswordEncoder passwordEncoder(){
		return  new BCryptPasswordEncoder();
	}

	@Autowired
	UserRepository userRepository;
	@RequestMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About");
		return "about";
	}
	@RequestMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("user",new User());
		return "signup";
	}
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("title", "Login Page");
		return "login";
	}
	
	@PostMapping("/do_register")
	public String register(@Valid @ModelAttribute("user") User user,BindingResult bindingResult,Model model,HttpSession session,@RequestParam(value = "agreement",defaultValue = "false") boolean agreement) {
		try {
			if(!agreement) {
				System.out.println("agreement"+agreement);
				throw new Exception("You Have not Agreed Terms and Conditions.");
			}
			if(bindingResult.hasErrors()) {
				System.out.println("errors:"+bindingResult);
				model.addAttribute("user",user);
				return "signup";
			}
			user.setEnabled(true);
			user.setImageUrl("user.png");
			user.setRole("ROLE_USER");
			user.setPassword(passwordEncoder().encode(user.getPassword()));
			User savedUser = userRepository.save(user);
			model.addAttribute("user",new User());
			session.setAttribute("message",new Message("Registered Successfully","alert-success") );
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message",new Message("Something Went Wrong"+e.getMessage(),"alert-danger") );
			return "signup";
			// TODO: handle exception
		}

	}

	@GetMapping("/forgot_password")
	public String forgotPassword(){
		return "forgotpassword";
	}

	@PostMapping("/send_otp")
	public String sendOtp(@RequestParam("email") String email,HttpSession session){
		System.out.println("email: "+email);
		User user = userRepository.getUserByName(email);
		if(user==null){
			session.setAttribute("message",new Message("Please enter registered email.","alert-danger") );
			return "forgotpassword";
		}else {

		//int otp=random.nextInt(9999);
		String numbers = "0123456789";
		Random random = new Random();
		StringBuilder otp = new StringBuilder();

		for (int i = 0; i < 5; i++) {
			otp.append(numbers.charAt(random.nextInt(numbers.length())));
		}
		String generatedOtp=otp.toString();
		System.out.println("otp:"+ generatedOtp);
		String message="OTP: "+generatedOtp;
		boolean flag = emailService.sendEmail(message, "OTP from SMC", email);
		if(flag){
			session.setAttribute("otp",generatedOtp);
			session.setAttribute("email",email);
			session.setAttribute("message",new Message("OTP has been sent to your Email.","alert-success") );
			return "verifyotp";
		}else{
			session.setAttribute("message",new Message("Please enter your valid email.","alert-danger") );
			return "forgotpassword";
		}
		}
	}

	@PostMapping("/verify_otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session){

		int generatedOtp=Integer.parseInt((String)session.getAttribute("otp"));
		System.out.println("old otp"+ otp);
		System.out.println("new otp"+ generatedOtp);

		if(generatedOtp==otp){
			return "changepasswordform";
		}else {
			session.setAttribute("message",new Message("Please Enter Correct Otp.","alert-danger") );
			return "verifyotp";
		}
	}

	@PostMapping("/change_password")
	public String changePassword(@RequestParam("password") String password,HttpSession session){
		String email = (String) session.getAttribute("email");
		User user = userRepository.getUserByName(email);
		user.setPassword(passwordEncoder().encode(password));
		userRepository.save(user);
		session.setAttribute("message",new Message("Password Changed Successfully","alert-success") );
		return "login";
	}
}














