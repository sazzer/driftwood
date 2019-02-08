package uk.co.grahamcox.driftwood.service.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

/**
 * The Web MVC Config to use
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {
    /** The list of argument resolvers to add */
    @Autowired
    private lateinit var argumentResolvers: List<HandlerMethodArgumentResolver>

    /** The list of interceptors to add */
    @Autowired
    private lateinit var interceptors: List<HandlerInterceptorAdapter>

    /**
     * Add resolvers to support custom controller method argument types.
     *
     * This does not override the built-in support for resolving handler
     * method arguments. To customize the built-in support for argument
     * resolution, configure [RequestMappingHandlerAdapter] directly.
     * @param resolvers initially an empty list
     */
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.addAll(argumentResolvers)
    }

    /**
     * Add all of the interceptors
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        interceptors.forEach { registry.addInterceptor(it) }
    }

    /**
     * Allows cross-origin calls to any API handler
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .exposedHeaders("Link")
    }

}
