package thymeleafsandbox.springsecurity.web.controller;

import java.util.Locale;
import java.util.Map;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import org.unbescape.html.HtmlEscape;
import reactor.core.publisher.Mono;

/**
 * Application home page and login.
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public Mono<String> root(Locale locale) {
        return Mono.just("redirect:/index.html");
    }

    /** Home page. */
    @RequestMapping("/index.html")
    public Mono<String> index() {
        return Mono.just("index");
    }

    /** User zone index. */
    @RequestMapping("/user/index.html")
    public Mono<String> userIndex() {
        return Mono.just("user/index");
    }

    /** Administration zone index. */
    @RequestMapping("/admin/index.html")
    public Mono<String> adminIndex() {
        return Mono.just("admin/index");
    }

    /** Shared zone index. */
    @RequestMapping("/shared/index.html")
    public Mono<String> sharedIndex() {
        return Mono.just("shared/index");
    }

    /** Login form. */
    @RequestMapping("/login.html")
    public Mono<String> login() {
        return Mono.just("login");
    }

    /** Login form with error. */
    @RequestMapping("/login-error.html")
    public Mono<String> loginError(Model model) {
        model.addAttribute("loginError", true);
        return Mono.just("login");
    }

    /** Simulation of an exception. */
    @RequestMapping("/simulateError.html")
    public void simulateError() {
        throw new RuntimeException("This is a simulated error message");
    }

    /** Error page. */
    @RequestMapping("/403.html")
    public Mono<String> forbidden() {
        return Mono.just("403");
    }


}
