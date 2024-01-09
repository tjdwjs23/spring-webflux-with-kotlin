package me.hl.webfluxapi.rest

import kotlinx.coroutines.reactive.awaitFirstOrNull
import me.hl.webfluxapi.domain.ItemService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebController(private val itemService: ItemService) {

    @GetMapping("/")
    suspend fun showIndexPage(model: Model): String {
        val items = itemService.findAll().collectList().awaitFirstOrNull() ?: emptyList()
        model.addAttribute("items", items)
        return "index"
    }
}