package demo.kotlin.webflux.domain

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories(basePackages = ["demo.kotlin.webflux.domain"])
class R2dbcConfig : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): PostgresqlConnectionFactory {
        return PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host("127.0.0.1")
                .port(5432)
                .database("template")
                .username("postgres")
                .password("postgres")
                .build()
        )
    }
}