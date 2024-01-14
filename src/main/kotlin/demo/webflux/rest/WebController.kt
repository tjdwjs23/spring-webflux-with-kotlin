package demo.webflux.rest

import demo.webflux.domain.template.Template
import demo.webflux.domain.template.TemplateService
import demo.webflux.exception.ItemNotFoundException
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
@RequestMapping("template")
class TemplateController(private val templateService: TemplateService) {

    private val logger = LoggerFactory.getLogger(TemplateController::class.java)

    @PostMapping
    suspend fun saveTemplate(@RequestBody template: Template): ResponseEntity<Any> {
        return try {
            val savedTemplate = templateService.save(template)
            ResponseEntity.status(HttpStatus.CREATED).body(savedTemplate)
        } catch (e: Exception) {
            logger.error("Error saving template", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving template: ${e.message}")
        }
    }

    @PutMapping("/{id}")
    suspend fun updateTemplate(
        @PathVariable id: Long,
        @RequestBody template: Template
    ): ResponseEntity<Any> {
        return try {
            val updatedTemplate = templateService.update(id, template).awaitFirst()
            ResponseEntity.ok(updatedTemplate)
        } catch (e: ItemNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Template not found.")
        } catch (e: Exception) {
            logger.error("Error updating template", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating template: ${e.message}")
        }
    }

    @DeleteMapping("/{id}")
    suspend fun deleteTemplate(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            templateService.delete(id).awaitFirstOrNull()
            ResponseEntity.ok().body("Template deleted successfully.")
        } catch (e: ItemNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Template not found.")
        } catch (e: Exception) {
            logger.error("Error deleting template", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting template: ${e.message}")
        }
    }

    @GetMapping("/{id}")
    suspend fun getTemplate(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val template = templateService.findById(id).awaitFirst()
            ResponseEntity.ok(template)
        } catch (e: ItemNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Template not found.")
        } catch (e: Exception) {
            logger.error("Error getting template", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting template: ${e.message}")
        }
    }

    @GetMapping("/list")
    suspend fun listTemplates(): ResponseEntity<Any> {
        return try {
            val templates = templateService.findAll().collectList().awaitFirst()
            ResponseEntity.ok(templates)
        } catch (e: Exception) {
            logger.error("Error listing templates", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error listing templates: ${e.message}")
        }
    }
}
