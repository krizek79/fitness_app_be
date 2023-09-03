package sk.krizan.fitness_app_be.controller.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.service.api.ProfileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("profiles")
public class ProfileController {

    private final ProfileService profileService;
}

