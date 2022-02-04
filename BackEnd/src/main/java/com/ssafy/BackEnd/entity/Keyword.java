package com.ssafy.BackEnd.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "keyword")
@Getter @Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Keyword {
    @Id @GeneratedValue
    long keyword_id;

    String name;

    int count;

    @OneToMany(mappedBy = "keyword")
    List<TeamKeyword> team_keyword = new ArrayList<>();

    @OneToMany(mappedBy = "keyword")
    List<TeamFeedKeyword> team_feed_keyword = new ArrayList<>();

    @OneToMany(mappedBy = "keyword")
    List<UserFeedKeyword> user_feed_keyword = new ArrayList<>();

    @OneToMany(mappedBy = "keyword")
    List<ProfileKeyword> profile_keyword = new ArrayList<>();

//    @OneToMany(mappedBy = "keyword")
//    List<ProfileKeyword> profile_keyword = new ArrayList<>();


}
