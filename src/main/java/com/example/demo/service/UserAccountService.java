@Bean
public UserAccountService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails user = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))
            .roles("ADMIN")
            .build();

    return new InMemoryUserDetailsManager(user);
}
