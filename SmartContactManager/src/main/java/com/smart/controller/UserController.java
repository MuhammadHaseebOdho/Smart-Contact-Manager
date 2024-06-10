package com.smart.controller;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller()
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;


    @ModelAttribute
    public  void commonData(Model model, Principal principal){
        String name=principal.getName();
        User user=userRepository.getUserByName(name);
        model.addAttribute("user",user);
    }
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        String name = principal.getName();
        User user = userRepository.getUserByName(name);
        model.addAttribute("user",user);
        model.addAttribute("title","Dashboard");
        return "user/dashboard";
    }

    @GetMapping("/addcontactform")
    public String addContactForm(Model model){
        model.addAttribute("title","Add Contact");
        model.addAttribute("contact",new Contact());
        return "user/addcontact";
    }
    @PostMapping("/addcontact")
    public String addContact(@ModelAttribute("contact") Contact contact, Principal principal, @RequestParam("profileImage") MultipartFile multipartFile, HttpSession session){
        try {
            if(multipartFile.isEmpty()){
                System.out.println("Did not uploaded file");
                contact.setImageUrl("default.png");
            }else{
                contact.setImageUrl(multipartFile.getOriginalFilename());
                File file = new ClassPathResource("/static/img").getFile();
                Path path = Paths.get(file + File.separator + multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            }

            User user = userRepository.getUserByName(principal.getName());
            contact.setUser(user);
            user.getContacts().add(contact);
            User save = userRepository.save(user);
            session.setAttribute("message",new Message("Contact Added Successfully","alert-success"));

        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message",new Message("Something Went Wrong","alert-danger"));

        }

        return "user/addcontact";
    }


    @GetMapping("/viewcontacts/{page}")
    public String viewContacts(@PathVariable("page") int page,Model model,Principal principal){
        String name = principal.getName();
        User user = userRepository.getUserByName(name);
        Pageable pageable= PageRequest.of(page,5);
        Page<Contact> contacts = contactRepository.getContactByUserId(user.getId(),pageable);
        contacts.getTotalPages();
        model.addAttribute("totalPages",contacts.getTotalPages());
        model.addAttribute("currentPage",page);
        model.addAttribute("title","ViewContact");
        model.addAttribute("contacts",contacts);
        return "user/viewcontacts";
    }

    @GetMapping("/viewcontact/{contactId}")
    public String viewContact(Model model,@PathVariable("contactId") int id,Principal principal){
        String name = principal.getName();
        User user = userRepository.getUserByName(name);
        Contact contact = contactRepository.findById(id).get();
        if(user.getId()==contact.getUser().getId()){
            model.addAttribute("title",contact.getName());
            model.addAttribute("contact",contact);
       }
        return "user/viewcontact";
    }

    @GetMapping("/deletecontact/{contactId}")
    public String deleteContact(@PathVariable("contactId") int id,Principal principal,Model model,HttpSession session) throws IOException {
        Contact contact = contactRepository.findById(id).get();
        String name = principal.getName();
        User user = userRepository.getUserByName(name);
        if(user.getId()==contact.getUser().getId()){
            contact.setUser(null);
            contactRepository.deleteById(id);
            File file = new ClassPathResource("/static/img").getFile();
            Path path = Paths.get(file + File.separator + contact.getImageUrl());
            Files.deleteIfExists(path);
            session.setAttribute("message",new Message("Contact Deleted Successfully","alert-success"));
        }else {
            session.setAttribute("message",new Message("You can't delete this contact","alert-danger"));
        }

        return "redirect:/user/viewcontacts/0";
    }

    @PostMapping("/updatecontactform/{contactId}")
    public String updateContactForm(@PathVariable("contactId") int id,Principal principal,Model model,HttpSession session){
        Contact contact = contactRepository.findById(id).get();
        model.addAttribute("contact",contact);
        return "user/updatecontact";
    }
    @PostMapping("/updatecontact")
    public String updateContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile multipartFile,Principal principal,Model model,HttpSession session) throws IOException {
        if(!multipartFile.isEmpty()){
            File file = new ClassPathResource("/static/img").getFile();
            Path path = Paths.get(file + File.separator + contact.getImageUrl());
            Files.deleteIfExists(path);
            contact.setImageUrl(multipartFile.getOriginalFilename());
            Files.copy(multipartFile.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
        }
        String name = principal.getName();
        User user = userRepository.getUserByName(name);
        contact.setUser(user);
        contactRepository.save(contact);
        return "redirect:/user/viewcontact/"+contact.getContactId();
    }
    @GetMapping("/profile")
    public String viewProfile(Model model,Principal principal){
        String name = principal.getName();
        User user = userRepository.getUserByName(name);
        model.addAttribute("user",user);
        model.addAttribute("title","Profile");
        return "user/profile";
    }

    @GetMapping("/settings")
    public String settings(){
        return "user/settings";
    }

    @PostMapping("/changepassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,Principal principal,HttpSession session){
        System.out.println("oldPassword: "+oldPassword);
        System.out.println("newpassword: "+newPassword);
        User user = userRepository.getUserByName(principal.getName());
        if(this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){
           user.setPassword(bCryptPasswordEncoder.encode(newPassword));
           userRepository.save(user);
           session.setAttribute("message",new Message("Password Changed Successfully","alert-success"));
        }else {
            session.setAttribute("message",new Message("Old Password is Incorrect","alert-danger"));
        }

        return "redirect:/user/settings";
    }
}















