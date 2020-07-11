package `in`.neuw.oauth2.services

import `in`.neuw.oauth2.clients.TestServiceClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TestService {

    @Autowired
    private lateinit var testServiceClient: TestServiceClient;

    fun getTestContent(name: String): Mono<Object> {
        return testServiceClient.getTestMessage(name);
    }

}