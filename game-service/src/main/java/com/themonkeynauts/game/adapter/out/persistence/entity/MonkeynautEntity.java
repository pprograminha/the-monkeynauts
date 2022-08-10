package com.themonkeynauts.game.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(schema = "themonkeynauts", name = "\"monkeynaut\"")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = { "user", "operator" }, callSuper = true)
@SuperBuilder
@ToString(exclude = { "user", "operator" }, callSuper = true)
public class MonkeynautEntity extends AbstractTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid", strategy = "uuid4")
    @Column(name = "id")
    private UUID id;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "class", nullable = false)
    private MonkeynautClassEnum clazz;

    @Enumerated(EnumType.STRING)
    @Column(name = "rank", nullable = false)
    private MonkeynautRankEnum rank;

    @Column(name = "skill", nullable = false)
    private Integer skill;

    @Column(name = "speed", nullable = false)
    private Integer speed;

    @Column(name = "resistance", nullable = false)
    private Integer resistance;

    @Column(name = "life", nullable = false)
    private Integer life;

    @Column(name = "current_energy", nullable = false)
    private Integer currentEnergy;

    @Column(name = "max_energy", updatable = false)
    private Integer maxEnergy;

    @Column(name = "number", nullable = false)
    private Long number;

    @Column(name = "ship_id")
    public UUID shipId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id", nullable = false)
    private UserEntity operator;

}
