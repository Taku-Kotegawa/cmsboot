package jp.co.stnet.cms.base.domain.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * パーミッション・ロール　エンティティ
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PermissionRolePK.class)
public class PermissionRole {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission permission;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
