package org.kshrd.gamifiedhabittracker.repository;

import org.apache.ibatis.annotations.*;
import org.kshrd.gamifiedhabittracker.model.HabitLogEntity;
import org.kshrd.gamifiedhabittracker.model.dto.request.HabitLogRequest;


import java.time.LocalDate;

import java.util.List;
import java.util.UUID;

@Mapper
public interface HabitLogRepository {

    @Results(id = "habitLogMapper", value = {
            @Result(property = "habitLogId", column = "habit_log_id"),
            @Result(property = "logDate", column = "log_date"),
            @Result(property = "status", column = "status"),
            @Result(property = "xpEarned", column = "xp_earned"),
            @Result(property = "habits", column = "habit_id",
                    one = @One(select = "org.kshrd.gamifiedhabittracker.repository.HabitRepository.getHabitById"))
    })

    // --- Insert

    @Select("""
        INSERT INTO habit_logs (log_date, status, xp_earned, habit_id)
        VALUES (#{logDate}, CAST(#{req.status} AS habit_status), #{xpEarned}, #{req.habitId})
        RETURNING *;
    """)
    HabitLogEntity createNewHabitLogRepo(@Param("req") HabitLogRequest habitLogRequest,
                                         @Param("logDate") LocalDate logDate,
                                         @Param("xpEarned") int xpEarned);


    //get by id
    @ResultMap("habitLogMapper")
    @Select(""" 
        SELECT  * FROM habit_logs 
        WHERE habit_id = #{habitId}  
        OFFSET #{size} * (#{page} - 1)  
        LIMIT #{size}; 
    """)
    List<HabitLogEntity> getHabitLogByIdRepo(UUID habitId, Integer page, Integer size );

//    @Select("SELECT COUNT(*) FROM habit_logs WHERE habit_id = CAST(#{habitId} AS UUID)")
//    int getCompletionCount(String habitId);


    //---- count
//    @Select("SELECT COUNT(*) FROM habit_logs WHERE habit_id = #{habitId}")
//    int countLogsByHabitId(UUID habitId);
}