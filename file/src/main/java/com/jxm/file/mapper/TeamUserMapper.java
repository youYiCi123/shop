package com.jxm.file.mapper;

import com.jxm.file.entity.TeamUser;

public interface TeamUserMapper {
    TeamUser select(Long teamId);
    int insert(TeamUser teamUser);
    int update(TeamUser teamUser);
}
