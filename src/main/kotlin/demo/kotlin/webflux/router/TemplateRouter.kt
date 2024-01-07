package demo.kotlin.webflux.router

import demo.kotlin.webflux.handler.TemplateHandler
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TemplateRouter(private val handler: TemplateHandler) {

    @GetMapping("/")
    suspend fun index(model: Model): String {
        val templates = handler.getAll().awaitFirstOrNull()
        model.addAttribute("templates", templates)
        return "index"
    }
}
