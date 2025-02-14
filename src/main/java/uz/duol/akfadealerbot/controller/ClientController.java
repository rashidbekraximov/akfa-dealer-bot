package uz.duol.akfadealerbot.controller;


import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.duol.akfadealerbot.model.request.AuthRequest;
import uz.duol.akfadealerbot.model.response.AuthResponse;
import uz.duol.akfadealerbot.model.response.TgUserInfo;
import uz.duol.akfadealerbot.service.ClientService;
import uz.duol.akfadealerbot.service.UserService;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    private final UserService userService;

    @PostMapping("/validate")
    public ResponseEntity<?> createUser(@RequestBody AuthRequest authRequest) {
        try {
            AuthResponse authResponse = clientService.validateByCode(authRequest.getCode());
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the user: " + e.getMessage());
        }
    }

    @GetMapping("/client")
    public ResponseEntity<?> getClientByUserId(@RequestParam String role, @RequestParam Long id) {
        try {
            TgUserInfo tgInfo = userService.getTgInfo(role,id);
            return ResponseEntity.ok(tgInfo);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
