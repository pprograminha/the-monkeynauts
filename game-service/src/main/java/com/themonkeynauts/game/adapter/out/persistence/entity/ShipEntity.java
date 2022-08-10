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
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(schema = "themonkeynauts", name = "\"ship\"")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = { "user", "operator" }, callSuper = true)
@SuperBuilder
@ToString(exclude = { "user", "operator" }, callSuper = true)
public class ShipEntity extends AbstractTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid", strategy = "uuid4")
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "class")
    private ShipClassEnum clazz;

    @Enumerated(EnumType.STRING)
    @Column(name = "rank")
    private ShipRankEnum rank;

    @Column(name = "durability")
    private Integer durability;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id", nullable = false)
    private UserEntity operator;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "themonkeynauts", name = "ship_monkeynaut", joinColumns = {@JoinColumn(name = "ship_id")}, inverseJoinColumns = {@JoinColumn(name = "monkeynaut_id")})
    @OrderBy(value = "number ASC")
    private Set<MonkeynautEntity> monkeynauts;
}
