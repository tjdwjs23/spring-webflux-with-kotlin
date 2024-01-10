package me.hl.webfluxapi.domain

import me.hl.webfluxapi.exception.ErrorCode
import me.hl.webfluxapi.exception.ItemNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TemplateService(private val templateRepository: TemplateRepository) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun create(template: Template): Mono<Template> =
        templateRepository.save(template)
            .also { logger.debug("Processed create(${template}).") }

    suspend fun update(id: Long, template: Template): Mono<Template> =
        findById(id)
            .flatMap {
                it.title = template.title
                templateRepository.save(it)
            }
            .also { logger.debug("Processed update($id, ${template}).") }

    suspend fun delete(id: Long) =
        findById(id)
            .flatMap { templateRepository.deleteById(it.id!!) }
            .also { logger.debug("Processed delete($id).") }

    suspend fun findAll(): Flux<Template> =
        templateRepository.findAll()
            .doOnEach { logger.debug("Processed findAll().") }
            .onErrorResume { error ->
                logger.error("Error during findAll(): ${error.message}")
                Flux.error(error) // 또는 다른 방식으로 에러 처리
            }


    suspend fun findById(id: Long): Mono<Template> =
        templateRepository.findById(id)
            .also { logger.debug("Processed findById($id).") }
            .switchIfEmpty(
                Mono.error(ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND))
            )
}
