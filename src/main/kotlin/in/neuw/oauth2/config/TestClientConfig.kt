package `in`.neuw.oauth2.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.function.Consumer


@Configuration
class TestClientConfig {

    private val testWebClientLogger:Logger = LoggerFactory.getLogger("TEST_WEB_CLIENT")

    /*@Bean
    fun authorizedClientManager(
            clientRegistrationRepository: ReactiveClientRegistrationRepository?,
            authorizedClientService: ReactiveOAuth2AuthorizedClientService?): ReactiveOAuth2AuthorizedClientManager? {
        val authorizedClientProvider: ReactiveOAuth2AuthorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build()
        val authorizedClientManager = AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientService)
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider)
        return authorizedClientManager
    }

    @Bean
    fun webClient(authorizedClientManager: ReactiveOAuth2AuthorizedClientManager?): WebClient? {
        val oauth = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        return WebClient.builder().filter(oauth).build()
    }*/

    /*@Bean
    fun webClient(clientRegistrationRepo: ReactiveClientRegistrationRepository?, authorizedClientRepo: ServerOAuth2AuthorizedClientRepository?): WebClient? {
        val filter = ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepo, authorizedClientRepo)
        return WebClient.builder()
                .filter(filter)
                .build()
    }*/

    @Bean
    fun authorizedClientManager(
            clientRegistrationRepository: ReactiveClientRegistrationRepository?,
            authorizedClientRepository: ServerOAuth2AuthorizedClientRepository,
            authorizedClientService: ReactiveOAuth2AuthorizedClientService?): ReactiveOAuth2AuthorizedClientManager? {

        val authorizedClientProvider: ReactiveOAuth2AuthorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build()

        val authorizedClientManager = DefaultReactiveOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository)

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider)
        return authorizedClientManager
    }

    @Bean(name = ["testClient"])
    fun webClient(authorizedClientManager: ReactiveOAuth2AuthorizedClientManager?,
                  @Value("\${test.client.base.url}") baseUrl:String): WebClient? {
        val oauth = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        oauth.setDefaultClientRegistrationId("local")
        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter(oauth)
                .filter(logRequest())
                .build()
    }

    private fun logRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
            testWebClientLogger.info("Request: {} {}", clientRequest.method(), clientRequest.url())
            // can log the request body query params etc.
            clientRequest.headers().forEach { name: String?, values: List<String?> -> values.forEach(Consumer { value: String? -> testWebClientLogger.info("{}={}", name, value) }) }
            Mono.just(clientRequest)
        }
    }

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain? {
        http.oauth2Client();
        return http.build()
    }

}