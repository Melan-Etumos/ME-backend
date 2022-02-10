package com.dsm.me.model.user.controller;

import com.dsm.me.model.user.dto.CodeCheckRequestDto;
import com.dsm.me.model.user.service.EmailService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.constraints.Email;

@RequiredArgsConstructor
@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping
    public void sendEmailCode(@RequestParam @Email String email) throws MessagingException {
        emailService.sendEmailCode(email);
    }

    @PostMapping("/{email}")
    public boolean verificationCodeCheck(@PathVariable @Email final String email, @RequestBody final CodeCheckRequestDto dto){
        return emailService.verificationCodeCheck(email, dto.getCode());
    }
}
