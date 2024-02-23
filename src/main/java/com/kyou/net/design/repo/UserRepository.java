package com.kyou.net.design.repo;

import com.kyou.net.design.pojo.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo,Integer> {

    UserInfo findByUserName(String username);

    UserInfo findByUserNameAndUserPassword(String username, String password);

}
