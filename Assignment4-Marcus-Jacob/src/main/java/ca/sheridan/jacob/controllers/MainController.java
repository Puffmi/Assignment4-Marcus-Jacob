package ca.sheridan.jacob.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridan.jacob.beans.User;
import ca.sheridan.jacob.database.DatabaseAccess;

@Controller
public class MainController {

    @Autowired
    private DatabaseAccess da;

    @GetMapping("/")
    public String index(Model model) {
        // index.html iterates over ${bookList}
        model.addAttribute("bookList", da.getBooks());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/permissionDenied")
    public String permissionDenied() {
        return "error/permissionDenied";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        if (da.findUserAccount(username) != null) {
            model.addAttribute("error", "User already exists!");
            return "register";
        }

        da.addUser(username, password);
        User user = da.findUserAccount(username);
        Long userId = user.getUserid();

        // Default to ROLE_USER for new registrations
        da.addRole(userId, 2L);

        return "redirect:/login";
    }
}
