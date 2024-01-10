package demo.webflux.domain.template

import demo.webflux.exception.ErrorCode
import demo.webflux.exception.ItemNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.file.Path
import java.nio.file.Paths

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
                Flux.error(error)
            }


    suspend fun findById(id: Long): Mono<Template> =
        templateRepository.findById(id)
            .also { logger.debug("Processed findById($id).") }
            .switchIfEmpty(
                Mono.error(ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND))
            )

    suspend fun save(template: Template, filePart: FilePart?): Mono<Template> {
        val savedTemplateMono = templateRepository.save(template)

        return savedTemplateMono.flatMap { savedTemplate ->
            // Handle file upload if FilePart is provided
            filePart?.let {
                val fileName = "uploaded_file_${System.currentTimeMillis()}_${it.filename()}"
                val uploadPath: Path = Paths.get("src/main/resources/static/assets/metadata")

                // Save the file to the specified path
                try {
                    it.transferTo(uploadPath.resolve(fileName).toFile())
                    logger.info("File saved successfully: $fileName")
                } catch (e: Exception) {
                    logger.error("Error saving file: ${e.message}", e)
                    return@flatMap Mono.error<Template>(e)
                }

                // Update the template entity with the file name
                savedTemplate.fileName = fileName
            }

            // Update the template entity in the database
            try {
                templateRepository.save(savedTemplate)
                logger.info("Template saved successfully: $savedTemplate")
                Mono.just(savedTemplate)
            } catch (e: Exception) {
                logger.error("Error saving template: ${e.message}", e)
                Mono.error(e)
            }
        }
    }



}
