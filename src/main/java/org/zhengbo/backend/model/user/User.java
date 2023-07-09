package org.zhengbo.backend.model.user;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@RequiredArgsConstructor
@Entity(name = "user")
@SequenceGenerator(name = "user_seq")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;
    
    private String username;

    private String pwd;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private TypeOfUser userType;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Student student;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserSecurityQa> securityQas;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;
}
