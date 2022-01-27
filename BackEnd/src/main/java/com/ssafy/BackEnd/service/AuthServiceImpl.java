package com.ssafy.BackEnd.service;

import com.ssafy.BackEnd.entity.Profile;
import com.ssafy.BackEnd.entity.Salt;
import com.ssafy.BackEnd.entity.User;
import com.ssafy.BackEnd.entity.UserIdentity;
import com.ssafy.BackEnd.repository.UserRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final RedisUtil redisUtil;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final SaltUtil saltUtil;
    private String key;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signUp(User user) {
        String password = user.getPassword();
        String salt = saltUtil.genSalt();
        user.setSalt(new Salt(salt));
        user.setPassword(saltUtil.encodePassword(salt, password));
        validateDuplicateUser(user);
        userRepository.save(user);
    }

    @Override
    public void validateDuplicateUser(User user) {
        User findUsers = userRepository.findByEmail(user.getEmail());
        if (findUsers != null) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Override
    public ResponseEntity<Profile> createProfile(User user) {
        Profile profile = new Profile();
        profile.setName(user.getName());
        profile.setEmail(user.getEmail());
        profile.setPosition(null);
        profile.setStack(null);
        profile.setImage(null);
        user.setProfile(profile);
        userRepository.save(user);

        return new ResponseEntity<Profile>(profile, HttpStatus.OK);
    }

    @Override
    public User signIn(String email, String password) throws Exception{
        User findUser = userRepository.findByEmail(email);
        System.out.println("find "+findUser.getEmail());
        if(findUser == null) throw new Exception ("멤버가 조회되지 않습니다.");
        String salt = findUser.getSalt().getSalt();
        password = saltUtil.encodePassword(salt, password);
        System.out.println("pass : "+password);
        System.out.println("u pass : "+findUser.getPassword());
        if(!findUser.getPassword().equals(password))
            throw new Exception("비밀번호가 틀립니다.");
        return findUser;
    }

    @Override
    public User findByEmail(String email) throws NotFoundException {
        User findUser = userRepository.findByEmail(email);
        if(findUser == null) throw new NotFoundException("멤버가 조회되지 않습니다.");
        return findUser;
    }

    @Override
    public User findByName(String name) throws NotFoundException {
        User user = userRepository.findByName(name);
        if(user == null) throw new NotFoundException("멤버가 조회되지 않습니다.");
        return user;
    }

    @Override
    public void requestChangePassword(User user) throws NotFoundException {
        String CHANGE_PASSWORD_LINK = "http://localhost:8080/user/password/";
        if(user == null) throw new NotFoundException("멤버가 조회되지 않습니다.");
        String key = REDIS_CHANGE_PASSWORD_PREFIX+ UUID.randomUUID();
        redisUtil.setDataExpire(key, user.getName(), 60*30L);
        emailService.sendMail(user.getEmail(), "사용자 비밀번호 안내 메일", CHANGE_PASSWORD_LINK+key);
    }

    @Override
    public boolean isPasswordUuidValidate(String key) {
        String userId = redisUtil.getData(key);
        return !userId.equals("");
    }

    @Override
    public void changePassword(User user, String password) throws NotFoundException {
        if(user==null) throw new NotFoundException("changePassword(), 멤버가 조회되지 않습니다.");
        String salt = saltUtil.genSalt();
        user.setSalt(new Salt(salt));
        user.setPassword(saltUtil.encodePassword(salt, password));
        userRepository.save(user);

    }

    public void sendVerificationMail(User user) throws NotFoundException {
        String VERIFICATION_LINK = "http://localhost:8080/user/verify/";
        if(user==null) throw new NotFoundException("멤버가 조회되지 않음");
        UUID uuid = UUID.randomUUID();
        System.out.println("key : " + uuid);
        redisUtil.setDataExpire(uuid.toString(),user.getEmail(), 60 * 30L);
        emailService.sendMail(user.getEmail(),"[팀 조이] 회원가입 인증메일입니다.",VERIFICATION_LINK+uuid.toString());
    }

    public ResponseEntity<User> verifyEmail(String key) throws NotFoundException {
        String userId = redisUtil.getData(key);
        User user = userRepository.findByEmail(userId);
        if(user==null) throw new NotFoundException("멤버가 조회되지않음");
        modifyUserRole(user, UserIdentity.USER); //enum으로 수정
        redisUtil.deleteData(key);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    public void modifyUserRole(User user, UserIdentity role){
        user.setIdentity(role);
        userRepository.save(user);
    }
}