package com.ssafy.BackEnd.repository;//package com.ssafy.BackEnd.repository;
//
//import com.ssafy.BackEnd.dto.ChatRoom;
//import com.ssafy.BackEnd.service.RedisSubscriber;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.stereotype.Repository;
//
//import javax.annotation.PostConstruct;
//import java.util.*;
//
//@RequiredArgsConstructor
//@Repository
//public class ChatRoomRepository {
//    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
//    private final RedisMessageListenerContainer redisMessageListener;
//    // 구독 처리 서비스
//    private final RedisSubscriber redisSubscriber;
//    // Redis
//    private static final String CHAT_ROOMS = "CHAT_ROOM";
//    private final RedisTemplate<String, Object> redisTemplate;
//    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
//    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
//    private Map<String, ChannelTopic> topics;
//
//    @PostConstruct
//    private void init() {
//        opsHashChatRoom = redisTemplate.opsForHash();
//        topics = new HashMap<>();
//    }
//
//    public List<ChatRoom> findAllRoom() {
//        return opsHashChatRoom.values(CHAT_ROOMS);
//    }
//
//    public ChatRoom findRoomById(String id) {
//        return opsHashChatRoom.get(CHAT_ROOMS, id);
//    }
//
//    /**
//     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
//     */
//    public ChatRoom createChatRoom(String name) {
//        ChatRoom chatRoom = ChatRoom.create(name);
//        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
//        return chatRoom;
//    }
//
//    /**
//     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
//     */
//    public void enterChatRoom(String roomId) {
//        ChannelTopic topic = topics.get(roomId);
//        if (topic == null) {
//            topic = new ChannelTopic(roomId);
//            redisMessageListener.addMessageListener(redisSubscriber, topic);
//            topics.put(roomId, topic);
//        }
//    }
//
//    public ChannelTopic getTopic(String roomId) {
//        return topics.get(roomId);
//    }
//}

// 은서


//import com.ssafy.BackEnd.dto.UserIdDto;
//import com.ssafy.BackEnd.entity.ChatRoom;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
//
//    List<ChatRoom> findChatRoomByCustomer(UserIdDto customer);
//    List<ChatRoom> findChatRoomByStore(UserIdDto store);
//}

//package com.ssafy.BackEnd.repository;
//
//import com.ssafy.BackEnd.dto.ChatRoom;
//import com.ssafy.BackEnd.service.RedisSubscriber;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.stereotype.Repository;
//
//import javax.annotation.PostConstruct;
//import java.util.*;
//
//@RequiredArgsConstructor
//@Repository
//public class ChatRoomRepository {
//    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
//    private final RedisMessageListenerContainer redisMessageListener;
//    // 구독 처리 서비스
//    private final RedisSubscriber redisSubscriber;
//    // Redis
//    private static final String CHAT_ROOMS = "CHAT_ROOM";
//    private final RedisTemplate<String, Object> redisTemplate;
//    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
//    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
//    private Map<String, ChannelTopic> topics;
//
//    @PostConstruct
//    private void init() {
//        opsHashChatRoom = redisTemplate.opsForHash();
//        topics = new HashMap<>();
//    }
//
//    public List<ChatRoom> findAllRoom() {
//        return opsHashChatRoom.values(CHAT_ROOMS);
//    }
//
//    public ChatRoom findRoomById(String id) {
//        return opsHashChatRoom.get(CHAT_ROOMS, id);
//    }
//
//    /**
//     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
//     */
//    public ChatRoom createChatRoom(String name) {
//        ChatRoom chatRoom = ChatRoom.create(name);
//        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
//        return chatRoom;
//    }
//
//    /**
//     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
//     */
//    public void enterChatRoom(String roomId) {
//        ChannelTopic topic = topics.get(roomId);
//        if (topic == null) {
//            topic = new ChannelTopic(roomId);
//            redisMessageListener.addMessageListener(redisSubscriber, topic);
//            topics.put(roomId, topic);
//        }
//    }
//
//    public ChannelTopic getTopic(String roomId) {
//        return topics.get(roomId);
//    }
//}

import com.ssafy.BackEnd.entity.ChatRoom;
import com.ssafy.BackEnd.entity.Profile;
import com.ssafy.BackEnd.pubsub.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository{

//    // 채팅방(topic)에 발행되는 메시지를 처리할 Listener
//    private final RedisMessageListenerContainer redisMessageListener;
//    // 구독 처리 서비스
//    private final RedisSubscriber redisSubscriber;
    // Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
//    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보, 서버별로 채팅방에 매치되는 topic 정보를 Map에 넣어 roomId로 찾을 수 있도록 한다.
//    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
    }

    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    // 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
    public ChatRoom createChatRoom(Profile user1, Profile user2) {
        ChatRoom chatRoom = ChatRoom.create(user1, user2);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);

        return chatRoom;
    }



//    // 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 실행한다.
//    public void enterChatRoom(String roomId) {
//        ChannelTopic topic = topics.get(roomId);
//        if (topic == null) {
//            topic = new ChannelTopic(roomId);
//            redisMessageListener.addMessageListener(redisSubscriber, topic);
//            topics.put(roomId, topic);
//        }
//    }
//
//    public ChannelTopic getTopic(String roomId) {
//        return topics.get(roomId);
//    }
}