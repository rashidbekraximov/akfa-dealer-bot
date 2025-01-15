package uz.duol.akfadealerbot.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.duol.akfadealerbot.dto.UserDto;
import uz.duol.akfadealerbot.service.UserService;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public String getAllUsers() {
        return "Successfully retrieved all users";
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody UserDto user) {
        try {
            userService.create(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully saved");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the user: " + e.getMessage());
        }
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUserByRoleAndId(@RequestParam String role, @RequestParam Long id) {
        try {
            userService.deleteByRoleAndId(role, id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the user.");
        }
    }
}
