package com.example.portfolio;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.mail.SimpleMailMessage;

@Controller
public class PortfolioController {

    private final EmailService emailService;

    public PortfolioController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("messageForm", new MessageForm());
        return "index";
    }

    @PostMapping("/send-message")
    public String sendMessage(@Valid @ModelAttribute("messageForm") MessageForm form,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "index";
        }

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("psaibalaji121@gmail.com");
        mail.setSubject("New Message from Portfolio");
        mail.setText("Name: " + form.getName() +
                "\nEmail: " + form.getEmail() +
                "\n\nMessage:\n" + form.getMessage());

        // Send asynchronously
        emailService.sendEmail(mail);

        // Show instant success alert
        model.addAttribute("success", true);
        model.addAttribute("messageForm", new MessageForm()); // reset form
        return "index";
    }
}
