package demo.webflux.webfluxapi.service

import demo.webflux.webfluxapi.Commons.Companion.ITEM_DEFAULT_ID
import demo.webflux.webfluxapi.buildAlternativeItemRequest
import demo.webflux.webfluxapi.buildItem
import demo.webflux.webfluxapi.buildItemRequest
import demo.webflux.webfluxapi.buildModifiedItem
import demo.webflux.exception.ErrorCode
import demo.webflux.exception.ItemNotFoundException
import demo.webflux.domain.template.TemplateRepository
import demo.webflux.domain.template.TemplateService
import demo.webflux.webfluxapi.rest.toDomain
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@WebFluxTest
class TemplateServiceTest {

    private val itemRepository = Mockito.mock(TemplateRepository::class.java)
    private val itemService = TemplateService(itemRepository)

    @Test
    fun `Should return Item When Item exists`() {
        val item = buildItem()
        given(itemRepository.findById(ITEM_DEFAULT_ID)).willReturn(Mono.just(item))

        StepVerifier.create(itemService.findById(ITEM_DEFAULT_ID))
            .expectNext(item)
            .expectComplete()
            .verify()
    }

    @Test
    fun `Should return Item list When at least one Item exists`() {
        val item = buildItem()
        given(itemRepository.findAll()).willReturn(Flux.just(item))

        StepVerifier.create(itemService.findAll())
            .expectNext(item)
            .expectComplete()
            .verify()
    }

    @Test
    fun `Should return empty list When no Item exists`() {
        given(itemRepository.findAll()).willReturn(Flux.empty())

        StepVerifier.create(itemService.findAll())
            .expectNextCount(0)
            .verifyComplete()
    }

    @Test
    fun `Should return ItemNotFoundException When search for an Item that does not exists`() {
        given(itemRepository.findById(ITEM_DEFAULT_ID)).willReturn(Mono.empty())

        StepVerifier.create(itemService.findById(ITEM_DEFAULT_ID))
            .expectNextCount(0)
            .expectErrorMatches {
                it is ItemNotFoundException && it.message == ErrorCode.ITEM_NOT_FOUND
            }
            .verify()
    }

    @Test
    fun `Should create Item`() {
        val item = buildItem()
        val itemRequest = buildItemRequest().toDomain()
        given(itemRepository.save(itemRequest)).willReturn(Mono.just(item))

        StepVerifier.create(itemService.create(itemRequest))
            .expectNext(item)
            .expectComplete()
            .verify()
    }


    @Test
    fun `Should update Item When Item exists`() {
        val item = buildItem()
        val itemRequest = buildAlternativeItemRequest().toDomain()
        val modifiedItem = buildModifiedItem()
        given(itemRepository.findById(ITEM_DEFAULT_ID)).willReturn(Mono.just(item))
        given(itemRepository.save(modifiedItem)).willReturn(Mono.just(modifiedItem))

        StepVerifier.create(itemService.update(ITEM_DEFAULT_ID, itemRequest))
            .expectNext(modifiedItem)
            .expectComplete()
            .verify()

    }

    @Test
    fun `Should return ItemNotFoundException When try to update an Item that does not exists`() {
        val itemRequest = buildAlternativeItemRequest().toDomain()
        given(itemRepository.findById(ITEM_DEFAULT_ID)).willReturn(Mono.empty())

        StepVerifier.create(itemService.update(ITEM_DEFAULT_ID, itemRequest))
            .expectNextCount(0)
            .expectErrorMatches {
                it is ItemNotFoundException && it.message == ErrorCode.ITEM_NOT_FOUND
            }
            .verify()
    }

    @Test
    fun `Should delete Item When Item exists`() {
        val item = buildItem()
        given(itemRepository.findById(ITEM_DEFAULT_ID)).willReturn(Mono.just(item))
        given(itemRepository.deleteById(ITEM_DEFAULT_ID)).willReturn(Mono.empty())

        StepVerifier.create(itemService.delete(ITEM_DEFAULT_ID))
            .expectNextCount(0)
            .verifyComplete()
    }

    @Test
    fun `Should return ItemNotFoundException When try to delete an Item that does not exists`() {
        given(itemRepository.findById(ITEM_DEFAULT_ID)).willReturn(Mono.empty())

        StepVerifier.create(itemService.delete(ITEM_DEFAULT_ID))
            .expectNextCount(0)
            .expectErrorMatches {
                it is ItemNotFoundException && it.message == ErrorCode.ITEM_NOT_FOUND
            }
            .verify()
    }

}