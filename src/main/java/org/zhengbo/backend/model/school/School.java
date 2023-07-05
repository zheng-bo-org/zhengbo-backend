package org.zhengbo.backend.model.school;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity(name = "school")
@Data
@RequiredArgsConstructor
@SequenceGenerator(name = "school_seq")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_seq")
    private Long id;

    private String name;

    private String address;

    private Integer minGrade;

    private Integer maxGrade;

    @Enumerated(EnumType.STRING)
    @Column(name = "school_level")
    private SchoolLevel schoolLevel;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;
}
