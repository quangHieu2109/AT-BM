package com.bookshopweb.jdbiInterface;

import com.bookshopweb.beans.Log;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Log.class)
public interface LogJDBI {
    @SqlUpdate("insert into log(id, ip, levelLog, res, preValue, curValue, createAt, updateAt) " +
            "values(:id, :ip, :levelLog, :res, :preValue, :curValue, :createAt, :updateAt )"
    )
    int insertLog(@BindBean Log log);
    @SqlQuery("select count(*) from log")
    int getQuantity();
    @SqlQuery("select count(*) from log where levelLog=:levelLog")
    int getQuantityByLevel(@Bind("levelLog") int levelLog);
    @SqlUpdate("update log set levelLog=:levelLog where id=:id")
    int updateLog(@BindBean Log log);
    @SqlQuery("select * from log")
    List<Log> selectAll();
    @SqlQuery("select * from log limit :start,:length")
    List<Log> selectByPage(@Bind("start")int start,@Bind("length") int length );
    @SqlQuery("select * from log where levelLog=:levelLog limit :start,:length")
    List<Log> selectByLevelLimit(@Bind("levelLog")int levelLog,@Bind("start")int start,@Bind("length") int length );
    @SqlUpdate("delete from log where id=:id")
    int deleteLog(@BindBean Log log);
}
