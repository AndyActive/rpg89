package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.models.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class PlayerServiceImpl implements PlayerService{
    @Autowired
    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository= playerRepository;
    }

    @Override
    public List<Player> findAll(Pageable pageable) {
        return playerRepository.findAll(pageable).stream().collect(Collectors.toList());
    }

    @Override
    public Player add(Map<String, String> params) {
        try {
            String name = params.getOrDefault("name", null);
            String title = params.getOrDefault("title", null);
            Race race = Race.valueOf(params.get("race"));
            Profession profession = Profession.valueOf(params.get("profession"));
            Date birthday = new Date(Long.parseLong(params.get("birthday")));
            Boolean banned = params.containsKey("banned") && "false".equals(params.get("banned"));
            Integer experience = Integer.parseInt(params.get("experience"));
            Integer level = Integer.parseInt(params.get("level"));
            Player player = new Player(name, title, race, profession,experience,0,0,birthday,banned);
            player.updateExperience();
            player.setUntilNextLevel(player.toUpdateExperience(player));
            System.out.println(player);
            return playerRepository.save(player);
        } catch (NullPointerException | IllegalArgumentException | ClassCastException e) {
            return null;
        }
    }

    @Override
    public Player updatePlayer(Long id, Map<String, String> params) {
        if (!playerRepository.findById(id).isPresent() || params == null) return null;
        Player result = playerRepository.findById(id).get();
        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);
        Profession profession = params.containsKey("profession") ? Profession.valueOf(params.get("profession")) : null;
        Race race = params.containsKey("race") ? Race.valueOf(params.get("race")) : null;
        Date birthday = params.containsKey("prodDate") ? new Date(Long.parseLong(params.get("prodDate"))) : null;
        Boolean banned = params.containsKey("banned") ? "true".equals(params.get("banned")) : null;
        Integer level = params.containsKey("level") ? Integer.parseInt(params.get("level")) : null;
        Integer experience = params.containsKey("experience") ? Integer.parseInt(params.get("experience")) : null;
        if (name != null) result.setName(name);
        if (title != null) result.setTitle(title);
        if (profession != null) result.setProfession(profession);
        if (race != null) result.setRace(race);
        if (birthday != null) result.setBirthday(birthday);
        if (banned != null) result.setBanned(banned);
        if (level != null) result.setLevel(level);
        if (experience != null) result.setExperience(experience);
        playerRepository.saveAndFlush(result);
        return result;
    }

    @Override
    public String deleteById(Long id) {
        boolean isEXX = playerRepository.existsById(id);
        if (isEXX) {
            playerRepository.deleteById(id);
            return "200";
        } else {
            return "400";
        }
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).get();
    }


    @Override
    public List<Player> findByParams(Map<String, String> params) {
        String name = (String) params.getOrDefault("name", null);
        String title = (String) params.getOrDefault("title", null);
        Profession profession = params.containsKey("profession") ? Profession.valueOf((String) params.get("profession")) : null;
        Race race = params.containsKey("race") ? Race.valueOf((String) params.get("race")) : null;
        Calendar calendar = Calendar.getInstance();
        Date after = null;
        if (params.containsKey("after")) {
            after = new Date(Long.parseLong(params.get("after")));
        }
        Date before = null;
        if (params.containsKey("before")) {
            before = new Date(Long.parseLong(params.get("before")));
        }
        Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;
        Integer minExperience = params.containsKey("minExperience") ? Integer.parseInt(params.get("minExperience")) : null;
        Integer maxExperience = params.containsKey("maxExperience") ? Integer.parseInt(params.get("maxExperience")) : null;
        Integer minLevel = params.containsKey("minLevel") ? Integer.parseInt(params.get("minLevel")) : null;
        Integer maxLevel = params.containsKey("maxLevel") ? Integer.parseInt(params.get("maxLevel")) : null;
        Integer untilNextLevel = params.containsKey("untilNextLevel") ? Integer.parseInt(params.get("untilNextLevel")) : null;
        Pageable pageable;
        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));
        if (params.containsKey("order")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, (PlayerOrder.valueOf(params.get("order"))).getFieldName());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        return playerRepository.findAllByParams(name, title, race,profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel,untilNextLevel, pageable).stream().collect(Collectors.toList());

    }

    @Override
    public Integer countByParams(Map<String, String> params) {
        String name = (String) params.getOrDefault("name", null);
        String title = (String) params.getOrDefault("title", null);
        Profession profession = params.containsKey("profession") ? Profession.valueOf((String) params.get("profession")) : null;
        Race race = params.containsKey("race") ? Race.valueOf((String) params.get("race")) : null;
        Calendar calendar = Calendar.getInstance();
        Date after = null;
        if (params.containsKey("after")) {
            after = new Date(Long.parseLong(params.get("after")));
        }
        Date before = null;
        if (params.containsKey("before")) {
            before = new Date(Long.parseLong(params.get("before")));
        }
        Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;
        Integer minExperience = params.containsKey("minExperience") ? Integer.parseInt(params.get("minExperience")) : null;
        Integer maxExperience = params.containsKey("maxExperience") ? Integer.parseInt(params.get("maxExperience")) : null;
        Integer minLevel = params.containsKey("minLevel") ? Integer.parseInt(params.get("minLevel")) : null;
        Integer maxLevel = params.containsKey("maxLevel") ? Integer.parseInt(params.get("maxLevel")) : null;
        Integer untilNextLevel = params.containsKey("untilNextLevel") ? Integer.parseInt(params.get("untilNextLevel")) : null;
        return playerRepository.countByParams(name, title, race,profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel,untilNextLevel);
    }

    @Override
    public Integer count() {
        try {
            return Math.toIntExact(playerRepository.count());
        } catch (ArithmeticException e) {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean existsById(Long id) {
        return playerRepository.existsById(id);
    }

    @Override
    public boolean isIdValid(Long id) {
        return (id > 0);
    }

    @Override
    public boolean isParamsValid(Map<String, String> params) {
        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);
        Integer birthdayDateY = null;
        if (params.containsKey("birthday")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(params.get("birthday")));
            birthdayDateY = calendar.get(Calendar.YEAR);
        }
        Integer experience = params.containsKey("experience") ? Integer.parseInt(params.get("experience")) : null;
        Integer level = params.containsKey("level") ? Integer.parseInt(params.get("level")) : null;
        boolean result =
                (name == null || title == null || name.length() <= 12 && name.length() > 0 && title.length() <= 30 && title.length() > 0)
                        && (birthdayDateY == null || birthdayDateY >= 2000 && birthdayDateY <= 3000)
                        && (experience == null || experience >= 0 && experience < 10000000)
                        && (level == null || level >= 0 && level <= 9999);
        try {
            if (params.containsKey("profession")) {
                Profession profession = Profession.valueOf(params.get("profession"));
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            result = false;
        }
        try {
            if (params.containsKey("race")) {
                Race race = Race.valueOf(params.get("race"));
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            result = false;
        }
        return result;

    }

    @Override
    public boolean isAllParamsFound(Map<String, String> params) {
        return params.containsKey("name")
                && params.containsKey("title")
                && params.containsKey("race")
                && params.containsKey("profession")
                && params.containsKey("birthday")
                && params.containsKey("banned")
                && params.containsKey("experience")
                && params.containsKey("level")
                && params.containsKey("untilNextLevel");
    }
}
