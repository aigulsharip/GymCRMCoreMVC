    package com.example.gymcrmcoremvc.controller;

    import com.example.gymcrmcoremvc.entity.trainee.Trainee;
    import com.example.gymcrmcoremvc.entity.trainee.TraineeProfileResponse;
    import com.example.gymcrmcoremvc.entity.trainee.TraineeRegistrationRequest;
    import com.example.gymcrmcoremvc.entity.trainee.TraineeRegistrationResponse;
    import com.example.gymcrmcoremvc.service.TraineeService;
    import com.example.gymcrmcoremvc.service.TrainerService;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.Optional;

    @RestController
    @RequestMapping("/trainees")
    public class TraineeController {

        private final TraineeService traineeService;

        private final TrainerService trainerService;

        public TraineeController(TraineeService traineeService, TrainerService trainerService) {
            this.traineeService = traineeService;
            this.trainerService = trainerService;
        }

        @PostMapping("/register")
        public TraineeRegistrationResponse registerTrainee(@RequestBody TraineeRegistrationRequest traineeRegistrationRequest) {
            return traineeService.registerTrainee(traineeRegistrationRequest);
        }

        @GetMapping("/login")
        public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
            // Perform login logic here
            Optional<Trainee> authenticatedTrainee = traineeService.authenticateTrainee(username, password);
            if (authenticatedTrainee.isPresent()) {
                return ResponseEntity.ok("Login successful"); // Return 200 OK if login successful
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password"); // Return 401 Unauthorized if login fails
            }
        }

        @GetMapping("/profile")
        public ResponseEntity<TraineeProfileResponse> getTraineeProfile(@RequestParam String username) {
            TraineeProfileResponse profileResponse = traineeService.getTraineeProfile(username);

            if (profileResponse != null) {
                return ResponseEntity.ok(profileResponse); // Return 200 OK with profile information
            } else {
                return ResponseEntity.notFound().build(); // Return 404 Not Found if trainee not found
            }
        }

    }
