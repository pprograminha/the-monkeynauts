package com.themonkeynauts.game.adapter.out.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(schema = "themonkeynauts", name = "\"user\"")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = { "monkeynauts" })
@Builder
@ToString(exclude = { "monkeynauts" })
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid", strategy = "uuid4")
    @Column(name = "id")
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "password", updatable = false)
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "last_bounty_hunting")
    private LocalDateTime lastBountyHunting;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<MonkeynautEntity> monkeynauts;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user")
    private WalletEntity wallet;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "themonkeynauts", name = "user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<RoleEntity> roles;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "themonkeynauts", name = "user_equipment", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "equipment_id")})
    private Set<EquipmentEntity> equipments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(schema = "themonkeynauts", name = "user_account", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "account_id")})
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private AccountEntity account;

    public AccountEntity getTenant() {
        return this.account;
    }

    public void setTenant(AccountEntity tenant) {
        this.account = tenant;
    }
}
