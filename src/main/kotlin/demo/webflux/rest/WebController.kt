package demo.webflux.rest

import demo.webflux.domain.template.TemplateService
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Controller
class WebController(private val templateService: TemplateService) {

    @GetMapping("/")
    suspend fun showIndexPage(model: Model): String {
        val templates = templateService.findAll().collectList().awaitFirstOrNull() ?: emptyList()
        model.addAttribute("templates", templates)
        return "index"
    }

    @GetMapping("/posts/save")
    suspend fun postsSavePage(model: Model): String {
        return "posts-save"
    }

    @Value("\${upload.dir}") // You can define the upload directory in application.properties
    private lateinit var uploadDir: String

    @PostMapping("/upload/file/single")
    @ResponseBody
    suspend fun handleFileUpload(
        @RequestPart("fileToUpload") filePart: FilePart
    ): ResponseEntity<String> {

        val fileName = "uploaded_file_${System.currentTimeMillis()}_${filePart.filename()}"
        val filePath: Path = Paths.get(uploadDir, fileName)

        return try {
            filePart.transferTo(filePath.toFile()).awaitFirstOrNull() // 비동기로 처리
            ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", "/").body("File uploaded successfully")
        } catch (e: Exception) {
            // Handle errors, if any, during file processing
            println("Error uploading file: ${e.message}")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: ${e.message}")
        }
    }
}