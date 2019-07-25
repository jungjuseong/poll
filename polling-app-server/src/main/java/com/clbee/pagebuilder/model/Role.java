package com.clbee.pagebuilder.model;

import org.hibernate.annotations.NaturalId;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "poll_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoleName name;
}
