package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.Role;

@MapperAdapter
public class InRoleMapper {

    public com.themonkeynauts.proto.common.messages.Role toProto(Role domain) {
        return com.themonkeynauts.proto.common.messages.Role.valueOf(domain.name());
    }
}
