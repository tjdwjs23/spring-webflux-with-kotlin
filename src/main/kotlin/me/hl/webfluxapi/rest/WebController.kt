package me.hl.webfluxapi.rest

import me.hl.webfluxapi.domain.ItemService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebController(private val itemService: ItemService) {

    @GetMapping("/")
    fun showIndexPage(model: Model): String {
        val items = itemService.findAll() // Assuming you have a method to fetch all items
        model.addAttribute("items", items)
        return "index"
    }
}