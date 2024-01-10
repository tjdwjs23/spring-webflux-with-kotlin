package demo.webflux.domain.template

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface TemplateRepository : ReactiveCrudRepository<Template, Long>