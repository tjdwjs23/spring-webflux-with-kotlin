package demo.kotlin.webflux.domain

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TemplateRepository : ReactiveCrudRepository<Template, Long>