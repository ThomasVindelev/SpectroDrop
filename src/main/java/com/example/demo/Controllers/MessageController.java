package com.example.demo.Controllers;

import org.springframework.ui.Model;
import com.example.demo.Models.Message;
import com.example.demo.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

//Lavet af Thomas Vindelev

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     *  Omdirigerer userId til messageService så den kan anvendes der
     */

    @PostMapping("/newMessage")
    public String newMessage(@ModelAttribute Message message, HttpSession session, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("messageError", messageService.newMessage(message));
        Integer userId = (Integer) session.getAttribute("id");
        Integer roleId = (Integer) session.getAttribute("role");
        if (roleId == 1) {
            return "redirect:/employeeMain/" + userId;
        } else {
            return "redirect:/customerMain/" + userId;
        }

    }

    /**
     * Omdirigerer user til viewAllMessages hvis success, ellers bliver de ført til index og får deres session invalidated
     */

    @GetMapping("/viewAllMessages")
    public String viewAllMessages(@ModelAttribute Message message, HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("id");
        if (userId == null) {
            session.invalidate();
            return "index";
        }
        model.addAttribute("messageList", messageService.getMessages(userId, false));
        return "viewAllMessages";
    }

    @GetMapping("/readMessage/{id}")
    public String readMessage(@PathVariable("id") int id, @ModelAttribute("isRead") boolean isRead, Model model, HttpSession session) {
        Integer roleId = (Integer) session.getAttribute("role");
        if (roleId == null) {
            session.invalidate();
            return "index";
        }
        model.addAttribute("message", messageService.getMessageById(id, isRead));
        return "viewMessage";
    }

}
