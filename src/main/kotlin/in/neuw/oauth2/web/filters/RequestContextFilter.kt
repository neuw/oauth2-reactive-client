package `in`.neuw.oauth2.web.filters

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class RequestContextFilter: WebFilter {

    private val log: Logger = LoggerFactory.getLogger(RequestContextFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val startTime = System.currentTimeMillis()
        val path = exchange.request.uri.path
        log.info("Starting the execution of '{}'", path)
        return chain.filter(exchange).doAfterTerminate {
            //exchange.response.headers.entries.forEach(Consumer<Map.Entry<String, List<String>>> { e: Map.Entry<String?, List<String?>?> -> LOGGER.info("Response header '{}': {}", e.key, e.value) })
            log.info("Finished the execution for '{}' with {} in {} ms",
                    path,
                    exchange.response.statusCode,
                    System.currentTimeMillis() - startTime)
        }
    }
}