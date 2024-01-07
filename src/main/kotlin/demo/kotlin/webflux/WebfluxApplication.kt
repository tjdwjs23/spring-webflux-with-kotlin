package demo.kotlin.webflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class WebfluxApplication

fun main(args: Array<String>) {
    runApplication<WebfluxApplication>(*args)
}
