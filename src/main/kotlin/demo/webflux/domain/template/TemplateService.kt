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
        try {
            templateRepository.save(template).awaitFirst()
        } catch (e: Exception) {
            logger.error("Error during save(): ${e.message}", e)
            throw e
        }

    suspend fun update(id: Long, template: Template): Mono<Template> =
        findById(id)
            .flatMap {
                val updatedTemplate = it.copy(
                    title = template.title,
                    content = template.content,
                    author = template.author,
                )
                templateRepository.save(updatedTemplate)
            }
            .onErrorMap { error ->
                logger.error("Error during update($id, $template): ${error.message}", error)
                error
            }

    suspend fun delete(id: Long): Mono<Void> =
        findById(id)
            .flatMap { templateRepository.deleteById(it.id!!).then() }
            .onErrorMap { error ->
                logger.error("Error during delete($id): ${error.message}", error)
                error
            }

    suspend fun findAll(): Flux<Template> =
        templateRepository.findAll()
            .onErrorMap { error ->
                logger.error("Error during findAll(): ${error.message}", error)
                error
            }

    suspend fun findById(id: Long): Mono<Template> =
        templateRepository.findById(id)
            .switchIfEmpty(Mono.error(ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND)))
            .onErrorMap { error ->
                logger.error("Error during findById($id): ${error.message}", error)
                error
            }
}
