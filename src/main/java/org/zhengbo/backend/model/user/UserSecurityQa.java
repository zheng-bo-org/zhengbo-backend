package org.zhengbo.backend.model.user;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@RequiredArgsConstructor
@Entity(name = "user_security_qa")
@SequenceGenerator(name = "user_security_qa_seq")
public class UserSecurityQa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_security_qa_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String question;

    private String answer;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;
}
