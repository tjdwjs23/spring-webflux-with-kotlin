package demo.webflux.webfluxapi.rest

import demo.webflux.webfluxapi.Commons.Companion.ITEM_DEFAULT_ID
import demo.webflux.webfluxapi.buildAlternativeItemRequest
import demo.webflux.webfluxapi.buildItem
import demo.webflux.webfluxapi.buildItemRequest
import demo.webflux.webfluxapi.buildModifiedItem
import demo.webflux.webfluxapi.buildNotFoundResponse
import demo.webflux.domain.template.Template
import demo.webflux.exception.ErrorCode
import demo.webflux.exception.ErrorResponse
import demo.webflux.exception.ItemNotFoundException
import demo.webflux.domain.template.TemplateService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.MessageSource
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RunWith(SpringRunner::class)
@WebFluxTest
class TemplateControllerTest {
    @MockBean
    private lateinit var itemService: TemplateService

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var messageSource: MessageSource

    @Test
    fun `Should return Item When Item exists`() {
        val item = buildItem()
        given(itemService.findById(ITEM_DEFAULT_ID)).willReturn(Mono.just(item))

        webTestClient.get()
            .uri("/items/$ITEM_DEFAULT_ID")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<Template>()
            .isEqualTo(item)
    }

    @Test
    fun `Should return Item list When at least one Item exists`() {
        val item = buildItem()
        given(itemService.findAll()).willReturn(Flux.just(item))

        webTestClient.get()
            .uri("/items")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<List<Template>>()
            .isEqualTo(listOf(item))
    }

    @Test
    fun `Should return empty list When no Item exists`() {
        given(itemService.findAll()).willReturn(Flux.empty())

        webTestClient.get()
            .uri("/items")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<List<Template>>()
            .isEqualTo(listOf())
    }

    @Test
    fun `Should return NotFound When search for an Item that does not exists`() {
        given(itemService.findById(ITEM_DEFAULT_ID)).willReturn(Mono.error(ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND)))

        webTestClient.get()
            .uri("/items/$ITEM_DEFAULT_ID")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound
            .expectBody<ErrorResponse>()
            .isEqualTo(buildNotFoundResponse(messageSource))
    }

    @Test
    fun `Should create Item`() {
        val item = buildItem()
        val itemRequest = buildItemRequest().toDomain()
        given(itemService.create(itemRequest)).willReturn(Mono.just(item))

        webTestClient.post()
            .uri("/items")
            .body(BodyInserters.fromValue(itemRequest))
            .exchange()
            .expectStatus().isCreated
            .expectBody<Template>()
            .isEqualTo(item)
    }

    @Test
    fun `Should update Item When Item exists`() {
        val item = buildModifiedItem()
        val itemRequest = buildAlternativeItemRequest().toDomain()
        given(itemService.update(ITEM_DEFAULT_ID, itemRequest)).willReturn(Mono.just(item))

        webTestClient.put()
            .uri("/items/$ITEM_DEFAULT_ID")
            .body(BodyInserters.fromValue(itemRequest))
            .exchange()
            .expectStatus().isOk
            .expectBody<Template>()
            .isEqualTo(item)
    }

    @Test
    fun `Should return NotFound When try to update an Item that does not exists`() {
        val itemRequest = buildAlternativeItemRequest()
        given(itemService.update(ITEM_DEFAULT_ID, itemRequest.toDomain())).willReturn(
            Mono.error(
                ItemNotFoundException(
                    ErrorCode.ITEM_NOT_FOUND
                )
            )
        )

        webTestClient.put()
            .uri("/items/$ITEM_DEFAULT_ID")
            .body(BodyInserters.fromValue(itemRequest))
            .exchange()
            .expectStatus().isNotFound
            .expectBody<ErrorResponse>()
            .isEqualTo(buildNotFoundResponse(messageSource))
    }

    @Test
    fun `Should delete Item When Item exists`() {
        given(itemService.delete(ITEM_DEFAULT_ID)).willReturn(Mono.empty())

        webTestClient.delete()
            .uri("/items/$ITEM_DEFAULT_ID")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun `Should return NotFound When try to delete an Item that does not exists`() {
        given(itemService.delete(ITEM_DEFAULT_ID)).willReturn(Mono.error(ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND)))

        webTestClient.delete()
            .uri("/items/$ITEM_DEFAULT_ID")
            .exchange()
            .expectStatus().isNotFound
            .expectBody<ErrorResponse>()
            .isEqualTo(buildNotFoundResponse(messageSource))
    }

}