package demo.webflux.domain.template

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("TEMPLATE_TABLE")
data class Template(
    @Id
    @Column("ID")
    val id: Long?,
    var title: String,
    var content: String,
    var author: String,
    @Column("FILE_NAME")
    var fileName: String? = null,
    var create_date: LocalDate? = LocalDate.now(),
    var modified_date: LocalDate? = null
)
