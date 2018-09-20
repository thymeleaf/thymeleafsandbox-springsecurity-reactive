/*
 * =============================================================================
 *
 *   Copyright (c) 2011-2016, The THYMELEAF team (http://www.thymeleaf.org)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 * =============================================================================
 */
package thymeleafsandbox.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorityReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfig {




    public SpringSecurityConfig() {
        super();
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        return http
                .formLogin()
                // TODO * Apparently setting a login page the MVC way does not work in WebFlux, FIX THIS
//                .loginPage("/login.html")
//                .authenticationFailureHandler(
//                        new RedirectServerAuthenticationFailureHandler("/login-error.html"))
            .and()
                .logout()
                .logoutUrl("/logout")
            .and()
                .authorizeExchange()
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .pathMatchers("/user/**").hasRole("USER")
                // TODO * This duplicity would not be needed if matchers had a "hasAnyRole" method,
                // TODO   which it has in the Spring MVC version...
                .pathMatchers("/shared/**")
                    .access(
                        (auth, obj) ->
                            AuthorityReactiveAuthorizationManager.hasRole("USER").check(auth, obj)
                                    .flatMap(
                                            (decision) -> {
                                                if (decision.isGranted()) {
                                                    return Mono.just(decision);
                                                }
                                                return AuthorityReactiveAuthorizationManager.hasRole("ADMIN").check(auth, obj);
                                            }))
                .pathMatchers("/login.html").permitAll()
                .anyExchange().authenticated()
            .and()
                .exceptionHandling()
                // TODO * There doesn't seem to be a way to specify a page for an Access Denied-type exception...
                // .accessDeniedPage("/403.html");
            .and()
                .build();

    }


    @Bean
    protected ReactiveUserDetailsService userDetailsService() {

        // The "withDefaultPasswordEncoder" method is marked as @deprecated, but only to signal it inadequacy
        // for production environments. This is a sample/demo application, so it's OK to use it.
        return new MapReactiveUserDetailsService(
                User.withDefaultPasswordEncoder()
                        .username("jim").password("demo").roles("ADMIN").build(),
                User.withDefaultPasswordEncoder()
                        .username("bob").password("demo").roles("USER").build(),
                User.withDefaultPasswordEncoder()
                        .username("ted").password("demo").roles("USER","ADMIN").build());

    }


}
