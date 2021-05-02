package com.game.controller;

import com.game.models.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

        @Autowired
        private final PlayerService playerService;

        public PlayerController(PlayerService playerService) {
            this.playerService= playerService;
        }

        @GetMapping("")
        public @ResponseBody ResponseEntity<List<Player>> getPlayersList(@RequestParam Map<String, String> params){
            if (params.isEmpty()) {
                return new ResponseEntity<>(playerService.findAll(PageRequest.of(0, 3)), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(playerService.findByParams(params), HttpStatus.OK);
            }
        }

    @GetMapping("/count")
    public @ResponseBody Integer getPlayerCount(@RequestParam Map<String, String> params){
        if (params.isEmpty()) {
            return playerService.count();
        } else {
            return playerService.countByParams(params);
        }
    }
        @PostMapping("")
        @ResponseStatus(HttpStatus.OK)
        public @ResponseBody ResponseEntity<Player> addPlayer(@RequestBody Map<String, String> params){
            if (!playerService.isAllParamsFound(params) || !playerService.isParamsValid(params)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            Player player = playerService.add(params);
            ResponseEntity<Player> result = player == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(player, HttpStatus.OK);
            return result;
        }

        @GetMapping("{id}")
        public @ResponseBody ResponseEntity<Player> getPlayer(
                @PathVariable Long id
        ){
            try {
                if (!playerService.isIdValid(id)) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                if (!playerService.existsById(id)) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                } else {
                    return new ResponseEntity<>(playerService.findById(id), HttpStatus.OK);
                }
            } catch (NullPointerException | IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        @PostMapping("{id}")
        public @ResponseBody
        ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Map<String, String> params){
            ResponseEntity<Player> badResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            ResponseEntity<Player> nfResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (!playerService.isIdValid(id) || !playerService.isParamsValid(params)) {
                return badResponse;
            }
            Player result = playerService.updatePlayer(id, params);
            if (result == null) {
                return nfResponse;
            } else {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }

        @DeleteMapping("{id}")
        public @ResponseBody
        ResponseEntity<Player> deletePlayer(@PathVariable Long id){
            ResponseEntity<Player> okResponse = new ResponseEntity<>(HttpStatus.OK);
            ResponseEntity<Player> badResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            ResponseEntity<Player> nfResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            try {
                if (!playerService.isIdValid(id)) return badResponse;
                String result = playerService.deleteById(id);
                if (result == null) return badResponse;
                if ("400".equals(result)) return nfResponse;
                if ("200".equals(result)) return okResponse;
            } catch (NullPointerException | IllegalArgumentException e) {
                return badResponse;
            }
            return badResponse;
        }
    }
