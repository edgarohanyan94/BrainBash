package am.aca.quiz.software.service.intefaces;


import am.aca.quiz.software.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public interface UserService {
    //create
    boolean addUser(UserEntity user) throws SQLException;

    //read
    List<UserEntity> getAll() throws SQLException;

    //update
    boolean update(UserEntity user, Long id) throws SQLException;

    //delete
    UserEntity remove(UserEntity user) throws SQLException;


    boolean removeByid(Long id) throws SQLException;

    UserEntity getById(Long id) throws SQLException;
}
