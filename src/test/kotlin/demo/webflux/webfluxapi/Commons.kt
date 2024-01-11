package demo.webflux.webfluxapi

import demo.webflux.webfluxapi.Commons.Companion.ITEM_ALTERNATIVE_ID
import demo.webflux.webfluxapi.Commons.Companion.ITEM_DEFAULT_ID
import demo.webflux.domain.template.Template
import demo.webflux.exception.Error
import demo.webflux.exception.ErrorCode
import demo.webflux.exception.ErrorResponse
import demo.webflux.webfluxapi.rest.ItemRequest
import org.springframework.context.MessageSource
import java.util.Locale

fun buildItem() = Template(ITEM_DEFAULT_ID, "Item $ITEM_DEFAULT_ID")
fun buildItemRequest() = ItemRequest("Item $ITEM_DEFAULT_ID")
fun buildModifiedItem() = Template(ITEM_DEFAULT_ID, "Item $ITEM_ALTERNATIVE_ID")
fun buildAlternativeItemRequest() = ItemRequest("Item $ITEM_ALTERNATIVE_ID")
fun buildNotFoundResponse(messageSource:MessageSource) = ErrorResponse(listOf(
    Error(code = ErrorCode.ITEM_NOT_FOUND, message = messageSource.getMessage(
        ErrorCode.ITEM_NOT_FOUND, null, Locale.getDefault()))
))

class Commons {
    companion object {
        const val ITEM_DEFAULT_ID: Long = 1
        const val ITEM_ALTERNATIVE_ID: Long = 2
    }
}