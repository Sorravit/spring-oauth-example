package sorravit.example.spring.controller

import sorravit.example.spring.jwt.JwtTokenProvider
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
import java.util.HashMap

@RestController
@RequestMapping("/auth")
class AuthController(internal var jwtTokenProvider: JwtTokenProvider) {
    @PostMapping("/signin")
    fun signin(@RequestBody data: AuthenticationRequest): ResponseEntity<*> {
        if (data.username == "admin" && data.password == "password") {
            val token = jwtTokenProvider.createToken(data.username, listOf("admin"))
            val model = HashMap<Any, Any>()
            model["username"] = data.username
            model["token"] = token
            return ok<Map<Any, Any>>(model)
        }
        return ok("YOU SHALL NOT PASS!")
    }
}

class AuthenticationRequest : Serializable {
    val username: String = ""
    val password: String = ""
}
