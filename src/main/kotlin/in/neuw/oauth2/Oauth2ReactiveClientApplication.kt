package `in`.neuw.oauth2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Oauth2ReactiveClientApplication

fun main(args: Array<String>) {
    runApplication<Oauth2ReactiveClientApplication>(*args)
}
