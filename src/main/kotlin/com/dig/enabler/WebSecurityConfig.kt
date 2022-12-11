package com.dig.enabler

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.RequestEntity
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.net.URI
import java.util.*


@Configuration
class WebSecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.headers().frameOptions().disable()

        http.oauth2Login {
            it.tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient())
            it.successHandler { _, response, _ ->
                response?.sendRedirect("http://localhost:3000")
            }
            it.failureHandler { _, response, exception ->
                response?.sendRedirect("/?error=${exception.message}")
            }
        }

        http.logout {
            it.logoutUrl("/api/logout")
            it.logoutSuccessHandler { _, response, _ ->
                response?.status = 200
            }
            it.deleteCookies("JSESSIONID")
        }

        http.csrf().ignoringRequestMatchers("/api/**")

        http.cors()

        http.authorizeHttpRequests {
            it.requestMatchers("/api/**").authenticated()
        }

//        http.exceptionHandling().authenticationEntryPoint { _, response, _ ->
//            response?.status = HttpStatus.FORBIDDEN.value()
//        }
        return http.build()
    }

    @Bean
    fun accessTokenResponseClient(): OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
        val accessTokenResponseClient = DefaultAuthorizationCodeTokenResponseClient()
        accessTokenResponseClient.setRequestEntityConverter(CustomRequestEntityConverter())

        val tokenResponseHttpMessageConverter = OAuth2AccessTokenResponseHttpMessageConverter()
        tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(CustomTokenResponseConverter())
        val restTemplate = RestTemplate(
            Arrays.asList(
                FormHttpMessageConverter(), tokenResponseHttpMessageConverter
            )
        )

        restTemplate.errorHandler = OAuth2ErrorResponseErrorHandler()
        accessTokenResponseClient.setRestOperations(restTemplate)

        return accessTokenResponseClient
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000")
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}

class CustomRequestEntityConverter : Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<*>> {
    private val defaultConverter = OAuth2AuthorizationCodeGrantRequestEntityConverter()

    @Override
    override fun convert(req: OAuth2AuthorizationCodeGrantRequest): RequestEntity<*> {
        val entity = defaultConverter.convert(req)
        val params = entity!!.body as MultiValueMap<String, String>
        val code = params["code"]?.first()
        val clientId = params["client_id"]?.first()
        val clientSecret = "client secret"
        val uri = URI("${entity.url}?client_id=${clientId}&client_secret=${clientSecret}&code=${code}")
        return RequestEntity(null, entity.headers, entity.method, uri)
    }
}

class CustomTokenResponseConverter : Converter<Map<String, Any>, OAuth2AccessTokenResponse> {
    @Override
    override fun convert(tokenResponseParameters: Map<String, Any>): OAuth2AccessTokenResponse? {
        val accessToken = tokenResponseParameters["token"] as String
        val accessTokenType = OAuth2AccessToken.TokenType.BEARER

        return OAuth2AccessTokenResponse
            .withToken(accessToken)
            .tokenType(accessTokenType)
            .build()
    }
}