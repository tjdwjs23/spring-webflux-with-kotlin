package demo.webflux.domain.template

import demo.webflux.exception.ErrorCode
import demo.webflux.exception.ItemNotFoundException
import kotlinx.coroutines.reactive.awaitFirst
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TemplateService(private val templateRepository: TemplateRepository) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun save(template: Template): Template =
        templateRepository.save(template)
            .doOnSuccess { savedTemplate ->
                logger.debug("Processed save($savedTemplate).")
            }
            .doOnError { error ->
                logger.error("Error during save(): ${error.message}")
            }
            .awaitFirst()

    suspend fun update(id: Long, template: Template): Mono<Template> =
        findById(id)
            .flatMap {
                it.title = template.title
                templateRepository.save(it)
            }
            .also { logger.debug("Processed update($id, ${template}).") }

    suspend fun delete(id: Long): Mono<Void> =
        findById(id)
            .flatMap { templateRepository.deleteById(it.id!!).then() }
            .also { logger.debug("Processed delete($id).") }

    suspend fun findAll(): Flux<Template> =
        templateRepository.findAll()
            .doOnEach { logger.debug("Processed findAll().") }
            .onErrorResume { error ->
                logger.error("Error during findAll(): ${error.message}")
                Flux.error(error)
            }

    suspend fun findById(id: Long): Mono<Template> =
        templateRepository.findById(id)
            .also { logger.debug("Processed findById($id).") }
            .switchIfEmpty(
                Mono.error(ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND))
            )
}

