package demo.webflux.rest

import demo.webflux.domain.template.Template
import demo.webflux.domain.template.TemplateService
import demo.webflux.exception.ItemNotFoundException
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import reactor.core.publisher.Flux

@RestController
@RequestMapping("template")
class TemplateController(private val templateService: TemplateService) {

    @PostMapping
    suspend fun saveTemplate(@RequestBody template: Template): ResponseEntity<Template> {
        val savedTemplate = templateService.save(template)
        return ResponseEntity.ok(savedTemplate)
    }

    @PutMapping("/{id}")
    suspend fun updateTemplate(
        @PathVariable id: Long,
        @RequestBody template: Template
    ): ResponseEntity<Template> {
        val updatedTemplate = templateService.update(id, template).awaitFirst()
        return ResponseEntity.ok(updatedTemplate)
    }

    @DeleteMapping("/{id}")
    suspend fun deleteTemplate(@PathVariable id: Long): ResponseEntity<Any> {
        try {
            templateService.delete(id).awaitFirstOrNull()
            return ResponseEntity.ok().body("Template deleted successfully.")
        } catch (e: ItemNotFoundException) {
            return ResponseEntity.status(404).body("Template not found.")
        } catch (e: Exception) {
            return ResponseEntity.status(500).body("Error deleting template: ${e.message}")
        }
    }

    @GetMapping("/{id}")
    suspend fun getTemplate(@PathVariable id: Long): ResponseEntity<Template> {
        val template = templateService.findById(id).awaitFirst()
        return ResponseEntity.ok(template)
    }

    @GetMapping("/list")
    suspend fun listTemplates(): ResponseEntity<Flux<Template>> {
        val templates = templateService.findAll()
        return ResponseEntity.ok(templates)
    }
}