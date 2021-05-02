package com.game.models;

import com.game.entity.Profession;
import com.game.entity.Race;



import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;


@Entity
@Table(name = "Player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String title;
    private Race race;
    private Profession profession;
    private Integer experience;
    private Integer untilNextLevel;
    private Date birthday;
    private Boolean banned;
    private Integer level;
    //@Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")

    public Player() {
    }

    public Player( String name, String title, Race race, Profession profession, Integer experience, Integer level, Integer untilNextLevel, Date birthday, Boolean banned) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.level=level;
        this.untilNextLevel = untilNextLevel;
        this.birthday = birthday;
        this.banned=banned;

    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", experience=" + experience +
                ", untilNextLevel=" + untilNextLevel +
                ", birthday=" + birthday +
                ", banned=" + banned +
                ", level=" + level +
                '}';
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
   // @Column(name = "race")
    @Enumerated(EnumType.STRING)
    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
   // @Column(name = "profession")
    @Enumerated(EnumType.STRING)
    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }



   public void updateExperience() {
       Integer rating =(int) (Math.sqrt(2500+200*getExperience())-50)/ 100;
       setExperience(rating);
    }
    public int toUpdateExperience(Player player) {
        Integer upLevel = 50*(player.level+1)*(player.level+2)-player.getExperience();
        return upLevel;
    }


    public static void main(String[] args) {
//        Calendar prodDateValue = Calendar.getInstance();
//        prodDateValue.setTimeInMillis((new Date()).getTime());
//        prodDateValue.add(Calendar.YEAR, 1000);
//        Race race = Race.ELF;
//        Profession profession = Profession.CLERIC;
//        Boolean isBanned = false;
//        Player player = new Player("ANDY", "BLA", race,profession,0, 0,0 ,prodDateValue.getTime());
//        Integer upLevel = 50*(player.level+1)*(player.level+2)-player.getExperience();
//        player.setLevel(upLevel);
//
    }
}
