package `in`.neuw.oauth2.clients

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


@Service
class TestServiceClient {

    @Autowired
    private lateinit var testClient:WebClient;

    private val welcome: String = "Welcome";

    fun getTestMessage(name:String? = "User"): Mono<Object> {
        val message = "$welcome $name!";
        val queryParams: MultiValueMap<String, String> = LinkedMultiValueMap()
        queryParams.add("message", message)
        return testClient
                .get().uri {
                    t -> t
                        .queryParams(queryParams)
                        .path("/test").build()
                }.exchange().flatMap {
                    // TODO can map it to correct type
                    it.bodyToMono(Object::class.java)
                };
    }

}