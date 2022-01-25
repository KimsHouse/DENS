package com.ssafy.BackEnd.service;

import com.ssafy.BackEnd.entity.User;
import com.ssafy.BackEnd.entity.UserFeed;
import com.ssafy.BackEnd.repository.UserFeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFeedServiceImpl implements UserFeedService{

    private final UserFeedRepository userFeedRepository;

    @Override
    public UserFeed createUserFeed(UserFeed userFeed) {
        userFeedRepository.save(userFeed);
        // 키워드 알고리즘 넣기

        return userFeed;
    }

    @Override
    public UserFeed modifyUserFeed(long userfeed_id, UserFeed userFeed) {
        UserFeed old_userfeed = userFeedRepository.findByFeedId(userfeed_id);
        old_userfeed.setContent(userFeed.getContent());

        userFeedRepository.save(userFeed);

        return userFeed;
    }

    @Override
    public void deleteUserFeed(long userfeed_id) {
        userFeedRepository.deleteById(userfeed_id);
    }

    @Override
    public List<UserFeed> showFindUserFeedList() {
        List<UserFeed> userFeeds = new ArrayList<>();
        userFeedRepository.findAll().forEach(userfeed -> userFeeds.add(userfeed));

        // 조회되지 않는 경우 처리하기
        return userFeeds;
    }

    @Override
    public List<String> checkHashTag(String content) { // 키워드 분리 알고리즘
        return null;
    }
}
