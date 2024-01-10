package demo.webflux.rest

import demo.webflux.domain.template.TemplateService
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

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
}