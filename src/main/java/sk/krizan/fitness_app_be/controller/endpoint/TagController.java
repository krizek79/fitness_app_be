package sk.krizan.fitness_app_be.controller.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.service.api.TagService;

@RestController
@RequiredArgsConstructor
@RequestMapping("tags")
public class TagController {

    private final TagService tagService;
}
