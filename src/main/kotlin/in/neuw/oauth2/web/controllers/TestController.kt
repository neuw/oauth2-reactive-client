package `in`.neuw.oauth2.web.controllers

import `in`.neuw.oauth2.services.TestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class TestController {

    @Autowired
    private lateinit var testService:TestService;

    @GetMapping("test")
    fun response(@RequestParam(required = false, defaultValue = "User") name: String): Mono<Object> {
        return testService.getTestContent(name);
    }

}