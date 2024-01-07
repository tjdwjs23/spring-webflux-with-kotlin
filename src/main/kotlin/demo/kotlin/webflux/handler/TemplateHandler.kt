package demo.kotlin.webflux.handler

import demo.kotlin.webflux.domain.Template
import demo.kotlin.webflux.domain.TemplateRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class TemplateHandler(private val repo: TemplateRepository) {

    fun getAll(): Mono<List<Template>> =
        repo.findAll()
            .filter(Objects::nonNull)
            .collectList()
}