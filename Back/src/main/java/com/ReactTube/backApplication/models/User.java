package com.ReactTube.backApplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    @ManyToMany(mappedBy = "visit")
    @JsonIgnore
    private Set<Video> videoVisits;

    @ManyToMany(mappedBy = "comment")
    @JsonIgnore
    private List<Video> videosCommented;

    @OneToMany(mappedBy = "uploadedBy")
    @JsonIgnore
    private Set<Video> videosUploaded;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    //Personal information (not required to register)

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = role.getPermissions().stream()
                .map(
                        permissionEnum -> new SimpleGrantedAuthority(permissionEnum.name())
                ).collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_ " + role.name()));

        return authorities;
    }
}
