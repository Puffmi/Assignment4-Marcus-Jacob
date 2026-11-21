package ca.sheridan.jacob.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridan.jacob.beans.Book;
import ca.sheridan.jacob.database.DatabaseAccess;
import jakarta.servlet.http.HttpSession;

@Controller
public class BookController {

    @Autowired
    private DatabaseAccess da;

    // index.html links to /viewBook/{id}
    @GetMapping("/viewBook/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        Book book = da.getBookById(id);
        // view-book.html expects ${title} and ${reviews} as separate model attributes
        model.addAttribute("title", book.getTitle());
        model.addAttribute("reviews", book.getReviews());
        model.addAttribute("book", book);
        return "view-book";
    }

    // index.html links to /addBook, add-book.html form posts to /addBook
    @GetMapping("/addBook")
    public String getAddBook() {
        return "admin/add-book";
    }

    @PostMapping("/addBook")
    public String postAddBook(@RequestParam String title, @RequestParam String author) {
        da.addBook(title, author);
        return "redirect:/";
    }

    // view-book.html links to /addReview/{id}
    @GetMapping("/addReview/{id}")
    public String getAddReview(@PathVariable Long id, Model model, HttpSession session) {
        Book book = da.getBookById(id);
        model.addAttribute("book", book);
        // Store bookId in session since add-review.html doesn't send it as a form param
        session.setAttribute("bookId", id);
        return "user/add-review";
    }

    // add-review.html form posts to /reviewAdded with param "reviewText"
    @PostMapping("/reviewAdded")
    public String postAddReview(@RequestParam String reviewText, HttpSession session) {
        Long bookId = (Long) session.getAttribute("bookId");
        da.addReview(bookId, reviewText);
        session.removeAttribute("bookId");
        return "redirect:/viewBook/" + bookId;
    }
}
