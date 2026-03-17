package com.runnershigh.domain.run.entity;

import com.runnershigh.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "runs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Run extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = true)
    private Long courseId;

    @Column(nullable = false)
    private double distance;

    public static Run create(Long memberId, Long courseId, double distance) {
        Run run = new Run();
        run.memberId = memberId;
        run.courseId = courseId;
        run.distance = distance;
        return run;
    }
}
