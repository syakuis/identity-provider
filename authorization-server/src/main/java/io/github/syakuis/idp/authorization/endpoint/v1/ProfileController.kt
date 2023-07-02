package io.github.syakuis.idp.authorization.endpoint.v1

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/profiles")
class ProfileController {
    @GetMapping(path = ["/me"])
    fun profile(model: Model, @AuthenticationPrincipal user: UserDetails ): String {
        model.addAttribute("user", user)
        return "/authorization/profile"
    }
}