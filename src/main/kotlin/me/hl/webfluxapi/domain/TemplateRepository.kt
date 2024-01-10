package me.hl.webfluxapi.domain

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface TemplateRepository : ReactiveCrudRepository<Template, Long>