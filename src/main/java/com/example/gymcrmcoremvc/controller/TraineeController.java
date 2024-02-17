    package com.example.gymcrmcoremvc.controller;

    import com.example.gymcrmcoremvc.entity.trainee.*;
    import com.example.gymcrmcoremvc.service.RegistrationLoginService;
    import com.example.gymcrmcoremvc.service.TraineeService;
    import com.example.gymcrmcoremvc.service.TrainerService;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/trainees")
    public class TraineeController {

        private final TraineeService traineeService;
        private final TrainerService trainerService;

        private final RegistrationLoginService registrationLoginService;

        public TraineeController(TraineeService traineeService, TrainerService trainerService, RegistrationLoginService registrationLoginService) {
            this.traineeService = traineeService;
            this.trainerService = trainerService;
            this.registrationLoginService = registrationLoginService;
        }

        @GetMapping
        public ResponseEntity<List<Trainee>> getAllTrainees() {
            List<Trainee> trainees = traineeService.getAllTrainees();
            if (!trainees.isEmpty()) {
                return ResponseEntity.ok(trainees);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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



        @DeleteMapping("/profile")
        public ResponseEntity<String> deleteTraineeProfile(@RequestParam String username) {
            boolean deleted = traineeService.deleteTraineeProfile(username);
            if (deleted) {
                return ResponseEntity.ok("Trainee profile deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username");
            }
        }


    }
